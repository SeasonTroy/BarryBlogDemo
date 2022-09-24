package com.bootdang.tv;/*
package com.bootdang.tv;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bootdang.system.entity.Tv;
import com.bootdang.system.entity.Tvtype;
import com.bootdang.system.mapper.TvtypeMapper;
import com.bootdang.system.service.ITvService;
import com.bootdang.system.service.ITvtypeService;
import com.mysql.cj.xdevapi.JsonArray;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

*/
/**
 *
 *电影采集
 *//*

@Component
public class JsoupTv {

    @Autowired
    ITvtypeService iTvtypeService;
    @Autowired
    ITvService iTvService;

    public static Document  getDocument(String url) throws IOException {
        Connection connect = Jsoup.connect(url);
        Document document = connect.get();
        return document;
    }

    */
/**
     * 爱奇艺类型批量采集
     * @param url
     *//*

    @Transactional
    public Boolean aqytypecj(String url){

        try {
            Document document = getDocument(url);
            Element elementById = document.getElementById("block-B");
            String attr = elementById.attr(":channel-list");
            JSONArray jsonArray = JSON.parseArray(attr);
            Iterator<Object> iterator = jsonArray.iterator();
            LinkedList<Tvtype> tvtypes = new LinkedList<>();
            while (iterator.hasNext()){
                JSONObject next = (JSONObject) iterator.next();
                for(int i=1;i<31;i++) {//默认采集30页
                    Tvtype tvtype = new Tvtype();
                    tvtype.setCreatetime(LocalDateTime.now());
                    tvtype.setTitle(next.getString("name"));
                    Integer cid = next.getInteger("cid");
                    String substring = url.substring(0, url.indexOf("www/"));
                    tvtype.setIdentification(substring + "www/" + cid + "/" + "-------------" + "24-"+i+"-1-iqiyi--.html");
                    tvtype.setState("0");//是否采集默认没有
                    tvtype.setClick(1);//点击量
                    tvtypes.add(tvtype);

                }
            }
          boolean b = iTvtypeService.saveBatch(tvtypes,tvtypes.size());
            new Thread(()->{
                aiqiyitv();
            }).start();//开辟一个线程采集电影

           return b;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public  boolean aqydt(String url){
        try {
            Document document = getDocument(url);
            String title = document.select("title").val();
            title=title.substring(0,title.indexOf("-"));
            Tvtype tvtype = new Tvtype();
            tvtype.setClick(1);
            tvtype.setState("0");
            tvtype.setIdentification(url);
            tvtype.setTitle(title);
            tvtype.setCreatetime(LocalDateTime.now());
            boolean save = iTvtypeService.save(tvtype);

            new Thread(()->{
                aiqiyitv();
            }).start();
            return save;
        } catch (IOException e) {

            e.printStackTrace();
            return false;
        }
    }
*/
/*    //爱奇艺电影采集
    public boolean aiqiyitv(String url){

        try {
                      Document document = getDocument(url);
          *//*
*/
/*  String content = document.select("meta[name=description]").get(0).attr("content");
            System.out.println(content);*//*
*/
/*

            Element elementById = document.getElementById("block-D");//元素id
            String attr = elementById.attr(":first-search-list");
            JSONObject jsonObject = JSON.parseObject(attr);
            Integer search_time = (Integer)jsonObject.get("search_time");//采集条数
            JSONArray list = jsonObject.getJSONArray("list");
            Iterator<Object> iterator = list.iterator();
            LinkedList<Tv> tvs = new LinkedList<>();
            while (iterator.hasNext()){
                JSONObject jsonObject1 = (JSONObject)iterator.next();
                String name = (String)jsonObject1.get("name");//电影名
                String imageurl = (String)jsonObject1.get("imageUrl");//电影封面图片
                String playurl = (String)jsonObject1.get("playUrl");//电影地址
                String duration = (String)jsonObject1.get("duration");//播放时长
                String description = (String)jsonObject1.get("description");//电影描述
                String focus = (String)jsonObject1.get("focus");//焦点
                String formatPeriod = (String)jsonObject1.get("formatPeriod");//上映时间
                JSONObject cast = (JSONObject)jsonObject1.get("cast");
                String guests = cast.toJSONString();

                Tv tv = new Tv();
                tv.setName(name);
                tv.setImageurl(imageurl);
                tv.setPlayurl(playurl);
                tv.setDescription(description);
                tv.setDuration(duration);
                tv.setGuests(guests);
               // tv.setTypeId(typeid);
                tv.setFocus(focus);
                tv.setFormatPeriod(formatPeriod);
                tvs.add(tv);
//实体类
            }

            boolean b = iTvService.saveBatch(tvs);
            //System.out.println(responseFormat(attr));
             return b;
        } catch (IOException e) {
            e.printStackTrace();
            return false;

        }

    }*//*

  //爱奇艺电影采集
   //@Transactional
  public void aiqiyitv(){
      List<Tvtype> state = iTvtypeService.list(new QueryWrapper<Tvtype>().eq("state", 0));
      if(state.size()!=0){
          for (Tvtype t:state) {
              LinkedList<Tv> tvtypes = new LinkedList<>();
              try {
                  Document document = getDocument(t.getIdentification());
              */
/*String content = document.select("meta[name=description]").get(0).attr("content");
              System.out.println(content);*//*

                  Element elementById = document.getElementById("block-D");//元素id
                  String attr = elementById.attr(":first-search-list");
                  JSONObject jsonObject = JSON.parseObject(attr);
                  Integer search_time = (Integer) jsonObject.get("search_time");//采集条数
                  JSONArray list = jsonObject.getJSONArray("list");
                  Iterator<Object> iterator = list.iterator();
                  while (iterator.hasNext()) {
                      JSONObject jsonObject1 = (JSONObject) iterator.next();
                      String name = (String) jsonObject1.get("name");//电影名
                      String imageurl = (String) jsonObject1.get("imageUrl");//电影封面图片
                      String playurl = (String) jsonObject1.get("playUrl");//电影地址
                      String duration = (String) jsonObject1.get("duration");//播放时长
                      String description = (String) jsonObject1.get("description");//电影描述
                      String focus = (String) jsonObject1.get("focus");//焦点
                      String formatPeriod = (String) jsonObject1.get("formatPeriod");//上映时间
                      JSONObject cast = (JSONObject) jsonObject1.get("cast");
                      String guests = cast.toJSONString();

                      Tv tv = new Tv();
                      tv.setName(name);
                      tv.setImageurl(imageurl);
                      tv.setPlayurl(playurl);
                      tv.setDescription(description);
                      tv.setDuration(duration);
                      tv.setGuests(guests);
                      tv.setTypeId(t.getTvtypeId());
                      tv.setClick(1);
                      tv.setFocus(focus);
                      tv.setFormatPeriod(formatPeriod);
                      tvtypes.add(tv);
//实体类
                  }
                  iTvService.saveBatch(tvtypes);//每页批量新增
                  //System.out.println(responseFormat(attr));
                  iTvtypeService.updateById(new Tvtype().setTvtypeId(t.getTvtypeId()).setState("1"));//修改采集状态
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }

      }

  }


    //json格式化
    private static String responseFormat(String resString){

        StringBuffer jsonForMatStr = new StringBuffer();
        int level = 0;
        for(int index=0;index<resString.length();index++)//将字符串中的字符逐个按行输出
        {
            //获取s中的每个字符
            char c = resString.charAt(index);

            //level大于0并且jsonForMatStr中的最后一个字符为\n,jsonForMatStr加入\t
            if (level > 0  && '\n' == jsonForMatStr.charAt(jsonForMatStr.length() - 1)) {
                jsonForMatStr.append(getLevelStr(level));
            }
            //遇到"{"和"["要增加空格和换行，遇到"}"和"]"要减少空格，以对应，遇到","要换行
            switch (c) {
                case '{':
                case '[':
                    jsonForMatStr.append(c + "\n");
                    level++;
                    break;
                case ',':
                    jsonForMatStr.append(c + "\n");
                    break;
                case '}':
                case ']':
                    jsonForMatStr.append("\n");
                    level--;
                    jsonForMatStr.append(getLevelStr(level));
                    jsonForMatStr.append(c);
                    break;
                default:
                    jsonForMatStr.append(c);
                    break;
            }
        }
        return jsonForMatStr.toString();
    }

    private static String getLevelStr(int level) {
        StringBuffer levelStr = new StringBuffer();
        for (int levelI = 0; levelI < level; levelI++) {
            levelStr.append("\t");
        }
        return levelStr.toString();
    }


    public static void main (String[] args) {
      //typecj("https://list.iqiyi.com/www/6/-------------24-1-1-iqiyi--.html");



    }

}
*/
