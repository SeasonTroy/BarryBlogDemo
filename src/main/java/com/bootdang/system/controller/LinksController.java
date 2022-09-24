package com.bootdang.system.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bootdang.system.entity.*;
import com.bootdang.system.service.IArticleService;
import com.bootdang.system.service.IArttypeService;
import com.bootdang.system.service.ILinksService;
import com.bootdang.util.ShiroUtils;
import com.bootdang.util.SystemColumnUrl;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 */
@Controller
@RequestMapping("/admin/links")
public class LinksController {

    @Autowired
    ILinksService linksService;
    @Autowired
    IArttypeService iArttypeService;

    @Resource
    SystemColumnUrl systemColumnUrl;

    @RequestMapping("")
    public String index(Model model){
        List<Links> list = linksService.list();
        model.addAttribute("daohang", systemColumnUrl.select(null));
        model.addAttribute("list",list);
        return "admin/page/links/linksList";
    }

    @GetMapping("/add")
    public String add(Model model){
         model.addAttribute("arttype",iArttypeService.list(new QueryWrapper<Arttype>().eq("parentid",0)));
        return "admin/page/links/linksAdd";
    }

    @RequiresPermissions("link-insert")
    @ResponseBody
    @PostMapping("/insert")
    public Map<String,Object> insert(Links links){
        Map<String, Object> map = new HashMap<>();
        boolean save = linksService.save(links.setCreatetime(LocalDateTime.now()));
        if(!save){
            map.put("code",500);
            map.put("msg","新增失败");
            return map;
        }
        map.put("code",200);
        map.put("msg","新增成功");
        return map;
    }
   @RequiresPermissions("link-delete")
    @ResponseBody
    @PostMapping("/deleteall")
    public Integer deleteall(String datas){
        String[] split = datas.split(",");
        List<String> strings = Arrays.asList(split);
        boolean b = linksService.removeByIds(strings);
        if(b){
            return 200;
        }
        return 500;
    }
    @RequiresPermissions("link-delete")
    @ResponseBody
    @PostMapping("/delete")
    public Map<String,Object> delete(Integer id){
        HashMap<String, Object> map = new HashMap<>();
        boolean b = linksService.removeById(id);
        if(b){
            map.put("code",200);
            map.put("msg","删除成功");
            return map;
        }
        map.put("code",500);
        map.put("msg","删除失败");
        return map;
    }

    @GetMapping("/edit")
    public String edit(Integer id,Model model){
         model.addAttribute("link",linksService.getById(id));
        model.addAttribute("arttype",iArttypeService.list(new QueryWrapper<Arttype>().eq("parentid",0)));
        return "admin/page/links/linksEdit";
    }

    @RequiresPermissions("link-update")
    @ResponseBody
    @PostMapping("/update")
    public HashMap<String,Object> update(Links link){
        HashMap<String, Object> map = new HashMap<>();
        boolean b = linksService.saveOrUpdate(link);
        if(b) {
            map.put("code",200);
            return map;
        }
        map.put("code",500);
        return map;


    }

}

