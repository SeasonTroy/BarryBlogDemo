package com.bootdang.home.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bootdang.system.entity.Arttype;
import com.bootdang.system.service.IArttypeService;
import com.bootdang.system.service.ITagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(basePackages = "com.bootdang.home.controller")
public class AdviceControllerArttype {
    @Autowired
    IArttypeService arttypeService;
    @Autowired
    ITagService iTagService;

    @ModelAttribute
    public void addmodel(Model model){
        model.addAttribute("listArttype",arttypeService.list(new QueryWrapper<Arttype>().eq("parentid",0).eq("state",1).orderByAsc("sort")));
        model.addAttribute("tags",iTagService.list());
    }
}
