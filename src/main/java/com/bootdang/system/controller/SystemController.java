package com.bootdang.system.controller;


import com.bootdang.system.entity.System;
import com.bootdang.system.service.ISystemService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 */
@Controller
@RequestMapping("/admin/systemparameters")
public class SystemController {

    @Autowired
    ISystemService systemService;

    @RequestMapping()
    public String index(Model model){
        model.addAttribute("system",systemService.list().get(0));
        return "admin/page/system/systemParameter";
    }

    @RequiresPermissions("system-update")
    @ResponseBody
    @PostMapping("/insert")
    public Map<String,Object> insert(HttpServletRequest request,System system){
        Map<String,Object> map=new HashMap<>();
        if(system.getState()==null){
            system.setState(0);
        }else{
            system.setState(1);
        }
        boolean b = systemService.saveOrUpdate(system);
        if(b){
            request.getSession().getServletContext().setAttribute("systemparam",system);
         map.put("code",200);
         return map;
        }
        map.put("code",500);
        return map;
    }
}

