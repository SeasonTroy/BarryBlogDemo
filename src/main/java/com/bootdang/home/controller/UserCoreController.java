package com.bootdang.home.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bootdang.system.entity.Article;
import com.bootdang.system.entity.Tag;
import com.bootdang.system.entity.User;
import com.bootdang.system.entity.UserTag;
import com.bootdang.system.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class UserCoreController {
     @Autowired
    IUserService userService;
     @Autowired
     IUserTagService userTagService;
     @Autowired
     ITagService tagService;
     @Autowired
     IArticleService iArticleService;
     @Autowired
     IArttypeService iArttypeService;
    @GetMapping("/usercore")
    public String index(){
        return "home/usercore/index";
    }
    @GetMapping("/userinfo")
    public String userinfo(Integer id, Map<String,Object> map){
        User user = userService.selectByUserRole(id);
        QueryWrapper<UserTag> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",id);
        List<UserTag> UserTags = userTagService.list(queryWrapper);
        Set<Integer> collect = UserTags.stream().map((usertag) -> usertag.getTagId()).collect(Collectors.toSet());
        List<Tag> tags = tagService.list();
        map.put("tags",tags);
        map.put("UserTags", collect);
        map.put("userinfo",user);
        return "admin/page/adminuser/userInfo";
    }

    @GetMapping("/articleEdit")
    public ModelAndView articleEdit(Integer id){

        Article byId = iArticleService.getById(id);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("home/usercore/article-edit");
        modelAndView.addObject("article",byId);
        modelAndView.addObject("tag",tagService.list());
        modelAndView.addObject("arttag",tagService.selectByArticleId(id));

        return modelAndView;
    }

    @GetMapping("/usercore/add")
    public String add(Model model) {
        List<Tag> tags = tagService.list();//全部标签
        model.addAttribute("tags",tags);
        model.addAttribute("arttypes", iArttypeService.selectArttype());
        return "home/usercore/article-add";
    }

}

