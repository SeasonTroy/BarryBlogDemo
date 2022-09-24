package com.bootdang.system.controller;


import com.alibaba.druid.sql.ast.statement.SQLIfStatement;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bootdang.common.aspect.Log;
import com.bootdang.lucene.luceneUtils;
import com.bootdang.system.entity.*;
import com.bootdang.system.service.*;
import com.bootdang.util.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Hash;
import org.apache.shiro.util.StringUtils;
import org.hibernate.validator.constraints.CodePointLength;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.lang.System;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 资源表 前端控制器
 * </p>
 */
@CrossOrigin(origins = "*",maxAge = 3600)
@Controller
@RequestMapping("/admin/article")
public class ArticleController extends SystemColumnUrl {

    @Autowired
    IArticleService articleService;
    @Autowired
    IArttypeService arttypeService;
    @Autowired
    FileService fileService;
    @Autowired
    ITagService tagService;
    @Autowired
    IUserService userService;
    @Autowired
    IArticleTagService articleTagService;
    @Autowired
    ICommentService iCommentService;
    @Resource
    com.bootdang.lucene.luceneUtils luceneUtils;
    @Autowired
    FileAdd fileAdd;


    @RequestMapping()
    public String index(Model model,@RequestParam(name = "page",required = false,defaultValue = "1") Integer page,@RequestParam(name = "limit",required = false,defaultValue = "10") Integer limit){

        model.addAttribute("daohang",select(null));
        PageHelper.startPage(page,limit);
        List<Article> articles = articleService.selectArticleAll(new Article());
        PageInfo<Article> objectPageInfo = new PageInfo<>(articles);
        model.addAttribute("pageinfo",objectPageInfo);
        /*
        查询所有资源
        */

        model.addAttribute("list",articles);
        return "admin/page/article/article_list";
    }

    /**
     *新增页面
     * @param model
     * @return
     */
    @RequiresPermissions(value = "article-insert")
    @GetMapping("/add")
    public String add(Model model) {
        List<Tag> tags = tagService.list();//全部标签
        model.addAttribute("tags",tags);
        model.addAttribute("arttypes", arttypeService.selectArttype());
        return "admin/page/article/article-add";
    }

    /**
     * 文章缩略图上传
     * @param file
     * @return
     */
    @Log(value = "上传了缩略图")
    @ResponseBody
    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    public Map<String,Object> fileimage(@RequestParam("file") MultipartFile file){
        String update = articleService.upload(file);
        HashMap<String,Object> r= new HashMap<>();
        if(update.equals("0")){
            r.put("code",500);
            r.put("msg","上传失败");
        }else{
            r.put("code",200);
            r.put("msg","上传成功");
            r.put("url",update);
        }
        return r;
    }

    @RequestMapping(value="/ueditorurl")
    @ResponseBody
    public String ueditor() {
        return PublicMsg.url;
    }


    /**
     * 编辑器上传图片
     * @param file
     * @return
     */

    @ResponseBody
    @RequestMapping(value = "/uploads/{name}",method = RequestMethod.POST)
    public Map<String,Object> fileupload(@PathVariable("name") String name,@RequestParam("file") MultipartFile file){

        HashMap<String,Object> r= new HashMap<>();
        String upload = articleService.upload(file);
     if(name.equals("layui")){
         if(upload.equals("0")){
             r.put("code",1);
             r.put("msg","上传失败");
             HashMap<String, Object> objectObjectHashMap = new HashMap<>();
             r.put("data",objectObjectHashMap);
         }else{
             r.put("code",0);
             r.put("msg","上传成功");
             HashMap<String, Object> objectObjectHashMap = new HashMap<>();
             objectObjectHashMap.put("src",upload);
             objectObjectHashMap.put("title","无名氏");
             r.put("data",objectObjectHashMap);
         }
     }else if(name.equals("ueditor")){
         if(upload.equals("0")){
             r.put("state","文件上传失败");
             r.put("url","");
             r.put("title","");
             r.put("original","");
         }else{
             r.put("state","SUCCESS");
             r.put("url",upload);
             r.put("title",file.getOriginalFilename());
             r.put("original",file.getOriginalFilename());
         }
     }
        return r;
    }
    @Log(value = "新增了文章")
    @RequiresPermissions(value = "article-insert")
    @ResponseBody
    @PostMapping("/insert")
    public Map<String,Object> insert(Article article) throws IOException {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> insert = articleService.insert(article);
        return insert;
    }

    /**
     * \删除资源的同时先删除评论和索引
     * @param id
     * @return
     */
    @Log(value = "删除了文章")
    @RequiresPermissions(value = "article-delete")
    @ResponseBody
    @PostMapping("/delete")
    public Map<String,Object> delete(Integer id) {
        Map<String, Object> map = new HashMap<>();
        boolean delete = false;
        try {
            delete = articleService.delete(id);
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

    /*
    * 批量删除资源的同时先删除评论和索引
    * */
    @Log(value = "删除了文章")
    @RequiresPermissions("article-delete")
    @ResponseBody
    @PostMapping("/deleteAll")
    public Map<String,Object> deleteAll(String datas){
        HashMap<String, Object> map = new HashMap<>();
        if(datas==null||datas.equals("")){
            map.put("code",500);
            map.put("msg","删除失败");
            return map;
        }
        String[] split = datas.split(",");
        Boolean deleteall = articleService.deleteall(split);
        if(deleteall){
            map.put("code",200);
            map.put("msg","删除成功");
            return map;
        }
        map.put("code",500);
        map.put("msg","删除失败");
        return map;
    }

    /**
     * 生成所有帖子索引
     * @return
     */
    @Log(value = "生成索引操作")
    @RequiresPermissions("article-index")
    @ResponseBody
    @PostMapping("/indexAll")
    public Map<String,Object> indexAll() {
        Map<String, Object> map = articleService.indexAll();
        return map;
    }

    /**
     * 删除全部索引
     * @return
     */
    @Log(value = "删除索引操作")
    @RequiresPermissions("article-index")
    @ResponseBody
    @PostMapping("/deleteIndexAll")
    public Map<String,Object> deleteBatchIndex(){
        HashMap<String, Object> map = new HashMap<>();
        Boolean aBoolean = articleService.deleteIndexAll();
        if(aBoolean){
            map.put("code",200);
            map.put("msg","删除成功");
            return map;
        }
        map.put("code",500);
        map.put("msg","删除失败");
        return map;
    }

    /**
     * 修改是否评论 只能管理员自己的文章才能修改
     * @param id
     * @param state
     * @return
     */
    @Log(value = "修改文章参数")
    @RequiresPermissions("article-update")
    @PostMapping(value = "/updatepl")
    @ResponseBody
    public  HashMap<String, Object> updatepl(Integer id, boolean state){
        HashMap<String,Object> result = new HashMap<>();
        if(id==null||id.equals(0)){
            result.put("code",500);
            result.put("msg","修改失败");
            return result;
        }
      /*  Article byId = articleService.getById(id);
        if(!byId.getCreateuserid().equals(ShiroUtils.getUserId())){
            result.put("code",500);
            result.put("msg","修改失败，此文章帖子不是你的窝");
            return result;
        }*/
        Article article= new Article();
        article.setArId(id);
        article.setCommentState(state==true?1:0);
        boolean save = articleService.saveOrUpdate(article);
        if(save){
            result.put("code",200);
            result.put("msg","修改成功");
            return result;
        }
        result.put("code",500);
        result.put("msg","修改失败");
        return result;
    }

    /**
     * 是否热门修改
     * @param id
     * @param state
     * @return
     */
    @Log(value = "修改文章参数")
    @RequiresPermissions("article-update")
    @PostMapping(value = "/updaterm")
    @ResponseBody
    public  HashMap<String, Object> updaterm(Integer id, boolean state){
        HashMap<String,Object> result = new HashMap<>();
        if(id==null||id.equals(0)){
            result.put("code",500);
            result.put("msg","修改失败");
            return result;
        }
        Article article= new Article();
        article.setArId(id);
        article.setIsHot(state==true?1:0);
        boolean save = articleService.saveOrUpdate(article);
        if(save){
            result.put("code",200);
            result.put("msg","修改成功");
            return result;
        }
        result.put("code",500);
        result.put("msg","修改失败");
        return result;
    }

    /**
     * 是否免费
     * @param id
     * @param state
     * @return
     */
    @Log(value = "修改文章参数")
    @RequiresPermissions("article-update")
    @PostMapping(value = "/updatemf")
    @ResponseBody
    public  HashMap<String, Object> updatemf(Integer id, boolean state){
        HashMap<String,Object> result = new HashMap<>();
        if(id==null||id.equals(0)){
            result.put("code",500);
            result.put("msg","修改失败");
            return result;
        }
        Article article= new Article();
        article.setArId(id);
        article.setIsFree(state==true?1:0);
        boolean save = articleService.saveOrUpdate(article);
        if(save){
            result.put("code",200);
            result.put("msg","修改成功");
            return result;
        }
        result.put("code",500);
        result.put("msg","修改失败");
        return result;
    }

    /**
     * 是否顶置
     * @param id
     * @param state
     * @return
     */
    @Log(value = "修改文章参数")
    @RequiresPermissions("article-update")
    @PostMapping(value = "/updatedz")
    @ResponseBody
    public  HashMap<String, Object> updatedz(Integer id, boolean state){
        HashMap<String,Object> result = new HashMap<>();
        if(id==null||id.equals(0)){
            result.put("code",500);
            result.put("msg","修改失败");
            return result;
        }
        Article article= new Article();
        article.setArId(id);
        article.setTopstate(state==true?1:0);
        boolean save = articleService.saveOrUpdate(article);
        if(save){
            result.put("code",200);
            result.put("msg","修改成功");
            return result;
        }
        result.put("code",500);
        result.put("msg","修改失败");
        return result;
    }

    /**
     * 审核页面 判断是什么类型的帖子然后渲染不同的页面
     * @param id
     * @param model
     * @return
     */

    @GetMapping("/trial")
    public String trial(Integer id,Model model){
        Article byId = articleService.getById(id);
        model.addAttribute("article",byId);
        List<Tag> tags = tagService.selectByArticleId(id);
        model.addAttribute("tags",tags);
        model.addAttribute("arttypes", arttypeService.selectArttype());
        return "admin/page/article/article-trial";

    }

    /**
     * 文章审核
     * @param id 文章id
     * @param state 审核状态
     * @param contxt 驳回内容
     * @return
     */
    @Log(value = "文章审核")
    @RequiresPermissions("article-shenghe")
    @PostMapping(value = "/shstate")
    @ResponseBody
    public Map<String,Object> shstate(Integer id,Integer state,@RequestParam(required = false,defaultValue = "驳回未通过") String contxt){
        HashMap<String,Object> result = new HashMap<>();
        Article article = new Article().setState(state).setArId(id);
        if(state!=null&&state.equals(3)){
           article.setResson(contxt);
        }
        boolean b = articleService.saveOrUpdate(article);
        if(b){
            if(state.equals(1)){
                Article byId = articleService.getById(id);
                luceneUtils.insert(byId);//添加索引
                userService.updateJfADD(byId.getCreateuserid(),JfFinalUtils.CODEFIVE.getCode());//添加积分
            }

            result.put("code",200);
            result.put("msg","审核成功");
            return result;
        }
        result.put("code",500);
        result.put("msg","审核失败");
        return result;
    }

    /**
     * 默认通过
     * @param datas
     * @return
     */
    @Log(value = "文章批量审核")
    @RequiresPermissions("article-shenghe")
    @ResponseBody
    @PostMapping("/shall")
    public Map<String,Object> shall(String datas){
        Map<String, Object> map = new HashMap<>();
        if(datas!=null&&!datas.equals("")) {
            String[] split = datas.split(",");
            Boolean shall = articleService.shall(split);
            if(!shall){
                map.put("code",500);
                map.put("msg","审核失败");
                return map;
            }
            map.put("code",200);
            map.put("msg","审核成功");
            return map;
        }
        map.put("code",500);
        map.put("msg","审核失败,请选择没有审核过的资源");
        return map;
    }

    /**
     * 修改页面跳转
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/edit")
    public String edit(Integer id,Model model){
        Article byId = articleService.getById(id);
        model.addAttribute("article",byId);
        model.addAttribute("arttypes",arttypeService.selectArttype());
        model.addAttribute("arttag",tagService.selectByArticleId(id));
        model.addAttribute("tags", tagService.list());
        return "admin/page/article/article-edit";
    }

    /**
     *文章修改
     * @param article
     * @return
     */
    @Log(value = "文章修改")
    @RequiresPermissions("article-update")
    @ResponseBody
    @PostMapping("/update")
    public Map<String,Object> update(Article article){
        Map<String, Object> update = articleService.update(article);
        return update;
    }

    /**
     * 文章搜索
     * @return
     */
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
        return "admin/page/article/article_serach";
    }
    /*
    * 文章上传没有提交的图片删除
    * */
    @ResponseBody
    @PostMapping("/deleteimage")
    public String deleteimage(String[] litpic){
        String path = fileAdd.getPath();
        String sub= path.substring(0, path.lastIndexOf("/"));
        String substring = sub.substring(0, sub.lastIndexOf("/"));
        Arrays.stream(litpic).filter((a)->{
            return a!=null&&!a.equals("");
        }).forEach((b)->{

            File file = new File(substring+b);
            if(file.exists()){
                file.delete();
            }
        });
        return "删除成功";
    }

    public static void main (String[] args) {
        System.out.println(JfFinalUtils.CODESIX);
    }


}

