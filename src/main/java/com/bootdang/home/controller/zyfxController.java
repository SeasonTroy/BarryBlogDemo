package com.bootdang.home.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bootdang.system.entity.Article;
import com.bootdang.system.entity.Arttype;
import com.bootdang.system.entity.Download;
import com.bootdang.system.entity.User;
import com.bootdang.system.service.IArticleService;
import com.bootdang.system.service.IArttypeService;
import com.bootdang.system.service.IDownloadService;
import com.bootdang.system.service.IUserService;
import com.bootdang.util.JfFinalUtils;
import com.bootdang.util.ShiroUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class zyfxController {
    @Autowired
    IArttypeService iArttypeService;
    @Autowired
    IArticleService iArticleService;
    @Autowired
    IDownloadService iDownloadService;
    @Autowired
    IUserService iUserService;

    @ModelAttribute
    public void publicindex(HttpServletRequest request,Model model){

    }

    @GetMapping("/zyfx/{id}")
    public String index(@RequestParam(name = "page",required = false,defaultValue = "1") Integer page, @RequestParam(name = "limit",required = false,defaultValue = "10") Integer limit,@PathVariable(value = "id") Integer id, Model model){
        Arttype arttype = iArttypeService.getById(id);
        iArttypeService.saveOrUpdate(new Arttype().setActtypeId(id).setClick(arttype.getClick()+1));
        model.addAttribute("artt",arttype);
        model.addAttribute("ids",id);
        List<Arttype> parentid = iArttypeService.list(new QueryWrapper<Arttype>().eq("parentid", id));
        model.addAttribute("parentarttype", parentid);
        QueryWrapper<Article> wrapper = new QueryWrapper<>();
        wrapper.eq("state",1);
        wrapper.ne("type",1).orderByDesc("ar_id");
        PageHelper.startPage(page,limit);//分页
        List<Article> articles = iArticleService.list(wrapper);
        PageInfo<Article> pageInfo = new PageInfo(articles);
        model.addAttribute("pageinfo",pageInfo);
        model.addAttribute("articlestop",articles);
        return "home/article/articelzx";
    }

        @GetMapping("/zyfx/{id}/{pid}")
        public String indexpid(@RequestParam(name = "page",required = false,defaultValue = "1") Integer page, @RequestParam(name = "limit",required = false,defaultValue = "10") Integer limit, @PathVariable(value = "id") Integer id, @PathVariable(value = "pid") Integer pid, Model model){
            Arttype arttype = iArttypeService.getById(pid);
            iArttypeService.saveOrUpdate(new Arttype().setActtypeId(pid).setClick(arttype.getClick()+1));
            model.addAttribute("artt",arttype);
            model.addAttribute("ids",id);

                List<Arttype> parentid = iArttypeService.list(new QueryWrapper<Arttype>().eq("parentid", id));
                model.addAttribute("parentarttype", parentid);
                model.addAttribute("pid",pid);
                QueryWrapper<Article> wrapper = new QueryWrapper<>();
                wrapper.eq("state",1).eq("arttype_id",pid);
                wrapper.ne("type",1).orderByDesc("ar_id");
                PageHelper.startPage(page,limit);//分页
                  List<Article> articles = iArticleService.list(wrapper);
                  PageInfo<Article> pageInfo = new PageInfo(articles);
                  model.addAttribute("pageinfo",pageInfo);
            model.addAttribute("articlestop",articles);
                return "home/article/articelzx";
            }

    @RequiresUser
    @ResponseBody
    @GetMapping("/download")
    public Map<String,Object> download(Integer id){
        HashMap<String, Object> map = new HashMap<>();
        Article byId = iArticleService.getById(id);

        Download one = iDownloadService.getOne(new QueryWrapper<Download>().eq("article_id", id).eq("user_id", ShiroUtils.getUserId()));
        if(one!=null){//是否下载过
            map.put("code",200);
            map.put("zyurl",byId.getDownloadurl());
            map.put("pass",byId.getDownloadpassword());
            return map;
        }
        if(byId.getIsFree()==1){//免费资源
            map.put("code",200);
            map.put("zyurl",byId.getDownloadurl());
            map.put("pass",byId.getDownloadpassword());
            return map;
        }else {//收费资源
            User us = iUserService.getById(ShiroUtils.getUserId());
            if (us.getJf() - byId.getJf() < 0) {
                map.put("code", 500);
                map.put("msg", "你的积分不足");
                return map;
            }
            boolean b = iUserService.updateById(User.of().setUserId(us.getUserId()).setJf(us.getJf() - byId.getJf()));//扣掉积分
            iUserService.updateJfADD(byId.getCreateuserid(),  byId.getJf());//添加积分
                if(b){
                    iDownloadService.save(new Download().setArticleId(byId.getArId()).setUserId(us.getUserId()).setDownloadtime(LocalDateTime.now()));//新增下载记录
                }
                map.put("code",200);
                map.put("zyurl",byId.getDownloadurl());
                map.put("pass",byId.getDownloadpassword());
               return map;
        }


    }

}
