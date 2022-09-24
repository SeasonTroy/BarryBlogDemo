package com.bootdang.home.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.additional.query.impl.QueryChainWrapper;
import com.bootdang.common.aspect.Log;
import com.bootdang.lucene.luceneUtils;
import com.bootdang.system.entity.*;
import com.bootdang.system.service.*;
import com.bootdang.util.ShiroUtils;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller(value = "homeindexController")
public class indexController {

    @Autowired
    IArticleService iArticleService;
    @Autowired
    ITagService tagService;
    @Autowired
    IArticleTagService iArticleTagService;
    @Autowired
    IArticleUserFabulousService articleUserFabulousService;
    @Autowired
    ICommentService iCommentService;

    @Autowired
    com.bootdang.lucene.luceneUtils luceneUtils;

    @RequestMapping("/")
    public String index(Model model){

        iArticleService.indexModel(model);

        return "home/index";
    }

    /**
     * 文章查看
     * @return
     */
    @GetMapping("/info/{id}")
    public String articleInfo(@PathVariable("id") Integer id,Model model){
        iArticleService.updateCount(id);//点击量+1
        List<Article> art = iArticleService.selectArticleAll(new Article().setArId(id));
        model.addAttribute("article",art.get(0));


       Article leftid = iArticleService.getOne(new QueryWrapper<Article>().eq("state",1).eq("type",art.get(0).getType()).lt("ar_id", id).last("order by ar_id desc limit 0,1"));
       Article nextid = iArticleService.getOne(new QueryWrapper<Article>().eq("state",1).eq("type",art.get(0).getType()).gt("ar_id", id).last("limit 0,1"));
           model.addAttribute("leftid",leftid);//上一条的数据
           model.addAttribute("nextid",nextid);//下一条的数据


        int fabulouscount = articleUserFabulousService.count(new QueryWrapper<ArticleUserFabulous>().eq("article_id", id));
        model.addAttribute("fabulouscount",fabulouscount);//当前文章点赞数量
        ArticleUserFabulous one =null;
      if(ShiroUtils.getUser()!=null) {
          QueryWrapper<ArticleUserFabulous> eq = new QueryWrapper<ArticleUserFabulous>().eq("user_id", ShiroUtils.getUserId()).eq("article_id", id);
          one = articleUserFabulousService.getOne(eq);
      }
      model.addAttribute("one",one);//当前用户是否点赞
        return "home/info";

    }

    @GetMapping("/tag/{id}")
    public String tag(@PathVariable("id") Integer tid,Model model){
        List<ArticleTag> tag_id = iArticleTagService.list(new QueryWrapper<ArticleTag>().eq("tag_id", tid));
        List<Integer> collect = tag_id.stream().map((a) -> {
            return a.getArticleId();
        }).collect(Collectors.toList());
        if(collect.size()==0){
            collect.add(0);
        }
        Collection<Article> articles = iArticleService.listByIds(collect);
        model.addAttribute("articles",articles);
        return "home/tag/tagArticle";
    }
    @GetMapping("/usercore/articlebk")
    public String articlebk(Model model){
        List<Article> articleuserid = iArticleService.selectArticleAll(new Article().setCreateuserid(ShiroUtils.getUserId()).setType(1));

        model.addAttribute("article",articleuserid);
        return "home/usercore/articlebk";
    }
    @GetMapping("/usercore/articlezy")
    public String articlezy(Model model){
        List<Article> articleuserid = iArticleService.selectArticleAll(new Article().setCreateuserid(ShiroUtils.getUserId()).setType(2));
        model.addAttribute("article",articleuserid);
        return "home/usercore/articlebk";
    }
    @Log(value = "修改文章参数")
    @PostMapping(value = "/pl")
    @ResponseBody
    public HashMap<String, Object> updatepl(Integer id, boolean state){
        HashMap<String,Object> result = new HashMap<>();
        if(id==null||id.equals(0)){
            result.put("code",500);
            result.put("msg","修改失败");
            return result;
        }
        Article byId = iArticleService.getById(id);
        if(!byId.getCreateuserid().equals(ShiroUtils.getUserId())){
            result.put("code",500);
            result.put("msg","修改失败，此文章帖子不是你的窝");
            return result;
        }
        Article article= new Article();
        article.setArId(id);
        article.setCommentState(state==true?1:0);
        boolean save = iArticleService.saveOrUpdate(article);
        if(save){
            result.put("code",200);
            result.put("msg","修改成功");
            return result;
        }
        result.put("code",500);
        result.put("msg","修改失败");
        return result;
    }

    @Log(value = "删除了文章")
    @ResponseBody
    @PostMapping("/delete")
    public Map<String,Object> delete(Integer id) {
        Map<String, Object> map = new HashMap<>();
        boolean delete = false;
        try {
            delete = iArticleService.delete(id);
            iCommentService.remove(new QueryWrapper<Comment>().eq("articleid",id));
        } catch (IOException e) {
            e.printStackTrace();
            map.put("code",500);
            map.put("msg","索引删除失败");
            return map;
        }
        if(delete){
            map.put("code",200);
            map.put("msg","删除成功");
            return map;
        }
        map.put("code",500);
        map.put("msg","删除失败");
        return map;
    }

    @PostMapping("/serach")
    public String serach(String serach,Model model){
        Map<String, Object> map=null;
        try {
            map = luceneUtils.lighlighterSelect(serach);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidTokenOffsetsException e) {
            e.printStackTrace();
        }
        model.addAttribute("serach",serach);
        if(map!=null&&((int)map.get("count")!=0)){
            model.addAttribute("count",(int)map.get("count"));
            model.addAttribute("articles",map.get("articles"));
        }else {
            model.addAttribute("count",(int)map.get("count"));
            model.addAttribute("msg","没有搜索到内容");
        }
        return "home/search/search";
    }

}
