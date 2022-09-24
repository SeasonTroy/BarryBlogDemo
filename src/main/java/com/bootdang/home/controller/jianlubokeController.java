package com.bootdang.home.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bootdang.system.entity.Article;
import com.bootdang.system.entity.Arttype;
import com.bootdang.system.service.IArticleService;
import com.bootdang.system.service.IArttypeService;
import com.bootdang.system.service.ITagService;
import com.bootdang.util.FinaUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicIntegerArray;

@Controller
public class jianlubokeController {
    @Autowired
    IArttypeService iArttypeService;
    @Autowired
    IArticleService iArticleService;
    @Autowired
    ITagService iTagService;

    @GetMapping("/jianluboke/{id}")
    public String index(@PathVariable(value = "id") Integer id, Model model){
       Arttype arttype = iArttypeService.getById(id);
       iArttypeService.saveOrUpdate(new Arttype().setActtypeId(id).setClick(arttype.getClick()+1));
       model.addAttribute("artt",arttype);
        model.addAttribute("ids",id);
        List<Arttype> parentid = iArttypeService.list(new QueryWrapper<Arttype>().eq("parentid", id));
        model.addAttribute("parentarttype", parentid);


        return "home/articlewz/article";
    }

    @GetMapping("/jianluboke/{id}/{pid}")
    public String indexpid(@PathVariable(value = "id") Integer id,@PathVariable(value = "pid") Integer pid, Model model){
        Arttype arttype = iArttypeService.getById(pid);
        iArttypeService.saveOrUpdate(new Arttype().setActtypeId(pid).setClick(arttype.getClick()+1));
        model.addAttribute("artt",arttype);
        model.addAttribute("ids",id);

        List<Arttype> parentid = iArttypeService.list(new QueryWrapper<Arttype>().eq("parentid", id));
        model.addAttribute("parentarttype", parentid);
        model.addAttribute("pid",pid);

        return "home/articlewz/article";
    }

    @ResponseBody
    @GetMapping("/blog")
    public Map<String,Object> blog(Integer typeid,String type,Integer limit,Integer page){
        HashMap<String, Object> map = new HashMap<>();

        QueryWrapper<Article> articleQueryWrapper = new QueryWrapper<Article>();
        if(typeid!=null) {
            articleQueryWrapper.eq("arttype_id", typeid);
        }

        articleQueryWrapper.eq("state", 1).eq("type", 1);
        if(FinaUtil.ARTICLE_ZUIXIN.equals(type)) {
            articleQueryWrapper.orderByDesc("ar_id");

       }else if(FinaUtil.ARTICLE_ZUIRE.equals(type)){
            articleQueryWrapper.orderByDesc("clickcount");
       }

        PageHelper.startPage(page+1, limit);
        List<Article> list = iArticleService.list(articleQueryWrapper);
    PageInfo<Article> pageInfo = new PageInfo<Article>(list);
    map.put("total", pageInfo.getTotal());
    map.put("rows", list);
    return map;


    }
}
