package com.bootdang.lucene;

import com.alibaba.druid.sql.visitor.functions.Trim;
import com.bootdang.system.entity.Article;
import com.bootdang.util.DateTimeFormatUtils;
import com.bootdang.util.FileAdd;
import com.github.pagehelper.util.StringUtil;

import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;


@Component
public class luceneUtils {
    private static FileAdd fileAdd;

    private  static  final DateTimeFormatUtils dateTimeFormatUtils = new DateTimeFormatUtils("yyyy-MM-dd HH:mm:ss");
    @Autowired
    public luceneUtils(FileAdd fileAdd){
        this.fileAdd=fileAdd;
    }
    luceneUtils(){};

    public IndexWriter getIndexWriter() throws IOException {

            File file = new File(fileAdd.getLuceneIndexPath());
            Directory open = FSDirectory.open(file.toPath());
            IKAnalyzer ikAnalyzer = new IKAnalyzer();
            IndexWriterConfig indexWriterConfig = new IndexWriterConfig(ikAnalyzer);
            IndexWriter  indexWriter = new IndexWriter(open, indexWriterConfig);
        return indexWriter;
    }

    public boolean insert(Article arc) {
        ReentrantLock reentrantLock = new ReentrantLock();
        reentrantLock.lock();
        IndexWriter indexWriter=null;
           try {
               indexWriter = getIndexWriter();
               Document insertdocument = insertdocument(arc);
               indexWriter.addDocument(insertdocument);
               indexWriter.close();
               return true;
           }catch (IOException e){
               e.getMessage();
               return false;
           }finally {
               if(indexWriter!=null) {
                   try {
                       indexWriter.close();
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
               }
               reentrantLock.unlock();

           }
    }

    public  boolean delete(String id) throws IOException {
        IndexWriter indexWriter = null;
        try {
            indexWriter = getIndexWriter();
            TermQuery  arId= new TermQuery(new Term("id", id));
            indexWriter.deleteDocuments(arId);
            indexWriter.forceMergeDeletes();//强制删除
            indexWriter.commit();
            return true;
        }catch (Exception e){
            e.getMessage();
            return false;
        }finally {
            if(indexWriter!=null) {
                indexWriter.close();
            }
        }
    }
    public boolean deleteAll(String[] ids) throws IOException{
        IndexWriter indexWriter=null;
        try {
            indexWriter = getIndexWriter();
            Query[] qs={};
            for(int i=0;i<ids.length;i++) {
                TermQuery arId = new TermQuery(new Term("id", ids[i]));
                qs[i]=arId;
            }
            indexWriter.deleteDocuments(qs);
            indexWriter.forceMergeDeletes();
            indexWriter.commit();
          /*  indexWriter.deleteAll();
            indexWriter.forceMergeDeletes();//强制删除
            indexWriter.commit();//提交*/
            return true;
        }catch (Exception e){
            e.getMessage();
            return false;
        }finally {
            if(indexWriter!=null){
                indexWriter.close();
            }
        }

    }

    public boolean deleteBatch() throws IOException{
        IndexWriter indexWriter=null;
        try {
            indexWriter = getIndexWriter();
            indexWriter.deleteAll();
            indexWriter.forceMergeDeletes();
            indexWriter.commit();
            return true;
        }catch (Exception e){
            e.getMessage();
            return false;
        }finally {
            if(indexWriter!=null){
                indexWriter.close();
            }
        }

    }

    public boolean update(Article arc) throws IOException {
        ReentrantLock reentrantLock = new ReentrantLock();
        reentrantLock.lock();
        IndexWriter indexWriter = null;
        try {
            indexWriter = getIndexWriter();
            indexWriter.updateDocument(new Term("id",String.valueOf(arc.getArId())),insertdocument(arc));
            indexWriter.commit();
             return true;
         }catch (Exception e){
             return false;
         }finally {
            if(indexWriter!=null) {
                indexWriter.close();
            }
             reentrantLock.unlock();
         }


    }

    /**
     * 没有高亮查询
     * @param serach
     * @return
     * @throws IOException
     * @throws ParseException
     */
    public  Map<String, Object> select(String serach) throws IOException, ParseException {

        FSDirectory open = FSDirectory.open(new File("F:\\propty\\upload\\index").toPath());
        IndexReader indexReader= DirectoryReader.open(open);
        IndexSearcher indexSearcher=new IndexSearcher(indexReader);
       // Query query=new TermQuery(new Term("id","10"));
        String[] fields = { "title", "context","description"};//三个默认域
        MultiFieldQueryParser queryParser = new MultiFieldQueryParser(fields, new IKAnalyzer());
        Query query = queryParser.parse(serach);
        Map<String, Object> println = printlncs(indexSearcher, query);
        indexReader.close();
        return println;
    }
   //没有高亮查询
    public  Map<String,Object> printlncs(IndexSearcher indexSearcher, Query query) throws IOException {
        TopDocs search = indexSearcher.search(query, 100);
        ScoreDoc[] scoreDocs = search.scoreDocs;
        HashMap<String, Object> map = new HashMap<>();
        map.put("count",search.totalHits);
        LinkedList<Article> linklist = new LinkedList<>();
        for(ScoreDoc score:scoreDocs){
            Document doc = indexSearcher.doc(score.doc);
            Article article = new Article();
            article.setArId(Integer.parseInt(doc.get("id")));
            article.setArttypeId(Integer.parseInt(doc.get("arttypeId")));
            article.setTitle(doc.get("title"));
            article.setLitpic(doc.get("litpic"));
            article.setContext(doc.get("context"));
            article.setDescription(doc.get("description"));
            article.setCreatetime(dateTimeFormatUtils.parse(doc.get("createtime")));
            article.setCreateuserid(Integer.parseInt(doc.get("createuserid")));
            article.setCommentcount(Integer.parseInt(doc.get("commentcount")));
            article.setClickcount(Integer.parseInt(doc.get("clickcount")));
            article.setTopstate(Integer.parseInt(doc.get("topstate")));
            article.setIsFree(Integer.parseInt(doc.get("isFree")));
            article.setJf(Integer.parseInt(doc.get("jf")));
            article.setIsHot(Integer.parseInt(doc.get("isHot")));
            article.setType(Integer.parseInt(doc.get("type")));

            linklist.add(article);
        }
        map.put("articles",linklist);
        return map;
    }

    //有高亮查询
    public  Map<String, Object> lighlighterSelect(String serach) throws ParseException, IOException, InvalidTokenOffsetsException {
        FSDirectory open = FSDirectory.open(new File("F:\\propty\\upload\\index").toPath());
        IndexReader indexReader= DirectoryReader.open(open);
        IndexSearcher indexSearcher=new IndexSearcher(indexReader);
        // Query query=new TermQuery(new Term("id","10"));
        String[] fields = { "title", "context","description"};//三个默认域
        MultiFieldQueryParser queryParser = new MultiFieldQueryParser(fields, new IKAnalyzer());
        Query query = queryParser.parse(serach);


        Map<String, Object> println = lighlighterPrint(indexSearcher, query);
        indexReader.close();
        return println;
    }

    //高亮查询
    public Map<String,Object> lighlighterPrint(IndexSearcher indexSearcher,Query query) throws IOException, InvalidTokenOffsetsException {
      //高亮
        Formatter simpleHTMLFormatter = new SimpleHTMLFormatter("<font color='red'>", "</font>");
        QueryScorer queryScorer = new QueryScorer(query);
        SimpleSpanFragmenter simpleSpanFragmenter = new SimpleSpanFragmenter(queryScorer);//声明片段
        Highlighter highlighter = new Highlighter(simpleHTMLFormatter, queryScorer);
        highlighter.setTextFragmenter(simpleSpanFragmenter);

        TopDocs search = indexSearcher.search(query, 100);
        HashMap<String, Object> map = new HashMap<>();
        map.put("count",search.totalHits);
        LinkedList<Article> linklist = new LinkedList<>();
        for(ScoreDoc score:search.scoreDocs){
            Document doc = indexSearcher.doc(score.doc);
            Article article = new Article();
            article.setArId(Integer.parseInt(doc.get("id")));
            article.setArttypeId(Integer.parseInt(doc.get("arttypeId")));
            String title = doc.get("title");
            if(title!=null){
                String bestcontext = highlighter.getBestFragment(new IKAnalyzer().tokenStream("title", new StringReader(title)), title);
                if(StringUtil.isEmpty(bestcontext)){
                    article.setTitle(title);
                }else{
                    article.setTitle(bestcontext);
                }
            }

            article.setLitpic(doc.get("litpic"));

            String context = doc.get("context");
            if(context!=null){
                String bestcontext = highlighter.getBestFragment(new IKAnalyzer().tokenStream("context", new StringReader(context)), context);
                if(StringUtil.isEmpty(bestcontext)){
                    article.setContext(context);
                }else{
                    article.setContext(bestcontext);
                }
            }

            String description = doc.get("description");
            if(description!=null){
                String bestcontext = highlighter.getBestFragment(new IKAnalyzer().tokenStream("description", new StringReader(description)),description);
                if(StringUtil.isEmpty(bestcontext)){
                    article.setDescription(description);
                }else{
                    article.setDescription(bestcontext);
                }
            }
            //article.setDescription(highlighter.getBestFragment(new IKAnalyzer().tokenStream("desctiption",new StringReader(doc.get("description"))),doc.get("description")));
            article.setCreatetime(dateTimeFormatUtils.parse(doc.get("createtime")));
            article.setCreateuserid(Integer.parseInt(doc.get("createuserid")));
            article.setCommentcount(Integer.parseInt(doc.get("commentcount")));
            article.setClickcount(Integer.parseInt(doc.get("clickcount")));
            article.setTopstate(Integer.parseInt(doc.get("topstate")));
            article.setIsFree(Integer.parseInt(doc.get("isFree")));
            article.setJf(Integer.parseInt(doc.get("jf")));
            article.setIsHot(Integer.parseInt(doc.get("isHot")));
            article.setType(Integer.parseInt(doc.get("type")));

            linklist.add(article);
        }
        map.put("articles",linklist);
        return map;
    }



    //公用document
    public Document insertdocument(Article arc){
        Document document = new Document();
        StringField arId = new StringField("id",String.valueOf(arc.getArId()), Field.Store.YES);
        StringField typeid = new StringField("arttypeId", String.valueOf(arc.getArttypeId()), Field.Store.YES);
        TextField textField = new TextField("title", arc.getTitle(), Field.Store.YES);
        StoredField litpic = new StoredField("litpic", arc.getLitpic());
        TextField context = new TextField("context", String.valueOf(arc.getContext()+""), Field.Store.YES);
        TextField description = new TextField("description", arc.getDescription(), Field.Store.YES);
        StringField createuserid = new StringField("createuserid", String.valueOf(arc.getCreateuserid()), Field.Store.YES);
        TextField cteatetime = new TextField("createtime", String.valueOf(dateTimeFormatUtils.format(arc.getCreatetime())), Field.Store.YES);
        StoredField commentcount = new StoredField("commentcount", String.valueOf(arc.getCommentcount()));
        StringField clickcount = new StringField("clickcount", String.valueOf(arc.getClickcount()), Field.Store.YES);
        StringField topstate = new StringField("topstate", String.valueOf(arc.getTopstate()), Field.Store.YES);
        StringField isFree = new StringField("isFree", String.valueOf(arc.getIsFree()), Field.Store.YES);
        StringField jf = new StringField("jf", String.valueOf(arc.getJf()), Field.Store.YES);
        StoredField isHot = new StoredField("isHot", String.valueOf(arc.getIsHot()));
        StringField type = new StringField("type",String.valueOf( arc.getType()), Field.Store.YES);
        document.add(arId);
        document.add(typeid);
        document.add(textField);
        document.add(litpic);
        document.add(context);
        document.add(description);
        document.add(createuserid);
        document.add(cteatetime);
        document.add(commentcount);
        document.add(clickcount);
        document.add(topstate);
        document.add(isFree);
        document.add(jf);
        document.add(isHot);
        document.add(type);
       return document;
    }

    public static void main (String[] args) throws IOException, ParseException, InvalidTokenOffsetsException {
     /*   //System.out.println(trimHtmlFiler.trimHtml2Txt("<p><span style=\"color: rgb(51, 51, 51); font-family: monospace; font-size: 14px; background-color: rgb(255, 255, 255);\">1&lt;a href=\"http://www.baidu.com/a\" onclick=\"alert(\"模拟XSS攻击\");\"&gt;sss&lt;/a&gt;&lt;script&gt;alert(0);&lt;/script&gt;sss</span></p>",new String[]{"<span>","script"}));
        Map<String, Object> select = lighlighterSelect("测试");
        System.out.println((int)select.get("count"));
        List<Article> articles = (List<Article>) select.get("articles");
        articles.forEach((a)->{
            System.out.println(a.getTitle());
        });
*/

        System.out.println(dateTimeFormatUtils.parse("2019-12-23 18:16:03"));
    }

}
