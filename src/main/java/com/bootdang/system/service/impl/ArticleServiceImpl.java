package com.bootdang.system.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bootdang.lucene.luceneUtils;
import com.bootdang.system.entity.*;
import com.bootdang.system.mapper.ArticleMapper;
import com.bootdang.system.mapper.ArttypeMapper;
import com.bootdang.system.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bootdang.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <p>
 * 资源表 服务实现类
 * </p>
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {

    @Autowired
    ArticleMapper articleMapper;

    @Resource
    luceneUtils luceneUtils;

    @Autowired
    IArttypeService arttypeService;
    @Autowired
    IWheelService wheelService;
    @Autowired
    ITagService tagService;

    @Autowired
    IArttypeService iArttypeService;
    @Autowired
    IUserService userService;
    @Autowired
    ILinksService linksService;

    @Autowired
    FileAdd fileAdd;

    @Autowired
    IArticleTagService articletagService;

    @Override
    @Transactional
    public List<Article> selectArticleAll (Article article) {
        return articleMapper.selectArticleAll(article);
    }

    @Override
    public String upload (MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        int i = FileTypeUtils.typeName(originalFilename);
        if(i==500){
            return "0";
        }
        String filetype=originalFilename.substring(originalFilename.lastIndexOf(".")+1,originalFilename.length()).toLowerCase();
        String fileproname=(new SimpDataTimeUtils("yyyy-MM").format(new Date()))+"/"+new Date().getTime()+"."+filetype;
        String filename=fileAdd.getPath()+fileproname;

        File file1 = new File(filename);
        if(!file1.exists()){
            file1.mkdirs();
        }
        try {
            file.transferTo(file1);
        } catch (IOException e) {
            e.printStackTrace();
            return "0";
        }
        return "/upload/"+fileproname;
    }

    @Transactional
    @Override
    public Map<String,Object> insert(Article article) throws IOException {
      Map<String, Object> map = new HashMap<>();
        if(article.getLitpic().equals("")){
          map.put("code",500);
          map.put("msg","缩略图不能为空");
          return map;
        }
        if(article.getType()>4){
            map.put("code",500);
            map.put("msg","类型错误");
            return map;
        }
        if(article.getType()>1) {//大于1代表不是文章

            if (!UrlYZhttpclientutile.postOpen(article.getDownloadurl())) {
                map.put("code", 500);
                map.put("msg", "百度云分享连接无效 请重新添加");
                return map;
            }
        if(article.getJf()>10){
            map.put("code",500);
            map.put("msg","别搞哦，积分设置过高");
            return map;
          }
        }

        if(article.getWeight()==null){
           article.setWeight(99);
        }
        if(article.getJf()==null||article.getJf().equals(0)){
            article.setIsFree(1);
        }else{
            article.setIsFree(0);
        }
        if(article.getClickcount()==null){
          article.setClickcount(new Random().nextInt(200));
        }
        article.setCreatetime(LocalDateTime.now()).setCreateuserid(ShiroUtils.getUserId());
        if(ShiroUtils.getUser().getIsAdmin().equals(1)){//判断是否是后台管理员如果是就不需要审核
            article.setState(1);
        }
        article.setJf(0);
        boolean save = save(article);
        if(article.getTag()!=null) {
            Arrays.stream(article.getTag()).forEach((a) -> {
                articletagService.save(new ArticleTag().setTagId(Integer.parseInt(a)).setArticleId(article.getArId()));
            });//添加文章对应的tag标签
        }else{
            map.put("code",500);
            map.put("msg","没有标签插入失败");
            return map;
        }
        if(ShiroUtils.getUser().getIsAdmin().equals(1)){//判断是否是后台管理员如果是就添加索引并且直接添加积分
            Article byId = getById(article.getArId());
            userService.updateJfADD(ShiroUtils.getUserId(),JfFinalUtils.CODEFIVE.getCode());
            luceneUtils.insert(byId);
        }

        if(save){
            map.put("code",200);
            map.put("msg","添加成功");
            return map;
        }else {
            map.put("code",500);
            map.put("msg","添加失败");
            return map;
        }
    }


    @Override
    public Boolean shall (String[] ids) {
       try {
           Arrays.stream(ids).forEach((a) -> {
               Article art = getById(a);
               if(art.getState()==0) {
                   boolean b = saveOrUpdate(new Article().setArId(Integer.parseInt(a)).setState(1));//批量审核通过
                     if(b){
                         luceneUtils.insert(art);
                         userService.updateJfADD(art.getCreateuserid(),JfFinalUtils.CODEFIVE.getCode());//添加积分
                     }
               }
           });
       }catch (Exception e){
           return false;
       }

        return true;
    }

    @Override
    @Transactional
    public boolean delete(Integer id) throws IOException {
       luceneUtils.delete(String.valueOf(id));//删除索引
        boolean b = removeById(id);
        articletagService.remove(new QueryWrapper<ArticleTag>().eq("article_id",id));//删除文章前先删除对应的tag
        return b ;
    }

    @Override
    public Boolean deleteall(String[] ids){
        try {
            luceneUtils.deleteAll(ids);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        boolean b = removeByIds(Arrays.asList(ids));
        return b;
    }

    @Override
    public Map<String,Object> indexAll()  {
       Map<String, Object> map = new HashMap<>();
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("state",1);
        List<Article> list = list(queryWrapper);
        AtomicBoolean iscg= new AtomicBoolean(true);
        list.forEach((article)->{
                boolean insert = luceneUtils.insert(article);
                if(insert==false){
                   iscg.set(false);
                }
        });

        if(iscg.get()){
           map.put("code",200);
           map.put("msg","生成成功");
           return map;
        }
        map.put("code",500);
        map.put("msg","生成失败");
        return map;
    }

    @Override
    public Boolean deleteIndexAll(){
        try {
            luceneUtils.deleteBatch();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
     return true;
    }

    public Map<String,Object> update(Article article){
        HashMap<String, Object> map = new HashMap<>();
        if(getById(article.getArId()).getState()!=1){
            map.put("code",500);
            map.put("msg","未审核过的资源不能修改");
            return map;
        };
        boolean b = saveOrUpdate(article);
        if(b) {
             articletagService.remove(new QueryWrapper<ArticleTag>().eq("article_id",article.getArId()));//先删除文章对应的tag标签
            if (article.getTag() != null) {
                Arrays.stream(article.getTag()).forEach((tagid)->{
                    articletagService.save(new ArticleTag().setTagId(Integer.parseInt(tagid)).setArticleId(article.getArId()));
                });
            }
            try {
                luceneUtils.update(getById(article.getArId()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            map.put("code",200);
            map.put("msg","修改成功");
            return map;

        }
        map.put("code",500);
        map.put("msg","修改失败");
        return map;
    }

    @Override
    public Model indexModel(Model model){
        List<Arttype> url =arttypeService.list(new QueryWrapper<Arttype>().eq("url", "/"));
        List<Wheel> wheel = wheelService.list(new QueryWrapper<Wheel>().eq("state",1).eq("arttype_id", url.get(0).getActtypeId()).orderByAsc("num"));
        model.addAttribute("lunbo",wheel);//查询轮播
        List<Article> list = list(new QueryWrapper<Article>().eq("topstate", 1).eq("state",1).orderByAsc("weight").last("limit 0,2"));
        model.addAttribute("dingbu",list);//轮播旁边的两个顶置

        List<Arttype> Arttypes = iArttypeService.list(new QueryWrapper<Arttype>().eq("parentid", url.get(0).getActtypeId()));
        List<Map<String,Object>> listart= new LinkedList<>();
        Arttypes.forEach((a)->{
            HashMap<String, Object> map = new HashMap<>();
            List<Article> parentid = articleMapper.selectList(new QueryWrapper<Article>()
                    .eq("arttype_id", a.getActtypeId()).eq("state",1)
                    .orderByAsc("weight")
                    .last("limit 0,7"));
            if(parentid.size()>=2){
                map.put("list1",parentid.subList(0,2));
                if(parentid.size()>=3){
                    map.put("list2",parentid.subList(2,parentid.size()));
                }
            }else{
                map.put("list1",parentid.subList(0,parentid.size()));
            }
                    listart.add(map);
        });

        List<Article> articles = articleMapper.selectList(new QueryWrapper<Article>().eq("is_hot", 1).eq("type",1).eq("state",1).orderByDesc("clickcount"));
        List<Article> articlestop = articleMapper.selectArticleAll(new Article().setState(1).setTopstate(1));

      /*  List<Article> artnozy = articleMapper.selectList(new QueryWrapper<Article>().ne("type", 1).eq("state", 1).orderByDesc("clickcount").last("limit 0,20"));
        List<Article> artzy = articleMapper.selectList(new QueryWrapper<Article>().eq("type", 1).eq("state", 1).orderByDesc("clickcount").last("limit 0,20"));*/
        List<Links> links = linksService.list(new QueryWrapper<Links>().eq("arttypeid", url.get(0).getActtypeId()).orderByAsc("num"));
        model.addAttribute("links",links);
      /*  model.addAttribute("artnozy",artnozy);//推荐资源
        model.addAttribute("artzy",artzy);//推荐文章*/
        User byId = userService.getById(1);
        model.addAttribute("admin",byId);//开发者信息
        model.addAttribute("arttypestop",articlestop);//顶置资源
        model.addAttribute("arttypes",Arttypes);//首页的栏目
        model.addAttribute("listart",listart);//首页资源
        model.addAttribute("ishotclick",articles);//热门资源
        return model;
    }

    @Override
    public int updateCount (Integer id) {
        return articleMapper.updateCount(id);
    }
}
