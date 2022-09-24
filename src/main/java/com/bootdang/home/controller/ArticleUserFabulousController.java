package com.bootdang.home.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bootdang.system.entity.ArticleUserFabulous;
import com.bootdang.system.service.IArticleUserFabulousService;
import com.bootdang.util.ShiroUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
public class ArticleUserFabulousController {
    @Autowired
    IArticleUserFabulousService iArticleUserFabulousService;

    @RequiresUser()
    @ResponseBody
    @PostMapping("/fabulous")
    public Map<String,Object> fabulous(Integer articleid){
        ArticleUserFabulous articleUserFabulous = new ArticleUserFabulous().setArticleId(articleid).setUserId(ShiroUtils.getUserId());
        Map<String, Object> map = new HashMap<>();
        map.put("user_id",ShiroUtils.getUserId());
        map.put("article_id",articleid);
        ArticleUserFabulous one = iArticleUserFabulousService.getOne(new QueryWrapper<ArticleUserFabulous>().allEq(map));//getone返回的是一个java对象的所有结果集
        if(one==null) {//如果没有点赞则添加
            boolean save = iArticleUserFabulousService.save(articleUserFabulous);
        }else {//如果已经点赞则删除
            iArticleUserFabulousService.removeByMap(map);
        }
        Map<String, Object> p = new HashMap<>();
        p.put("code",200);
        return p;

    }
}

