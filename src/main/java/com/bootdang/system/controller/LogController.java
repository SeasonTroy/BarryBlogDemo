package com.bootdang.system.controller;


import com.bootdang.system.entity.Log;
import com.bootdang.system.service.ILogService;
import com.bootdang.system.service.ISystemService;
import com.bootdang.util.SystemColumnUrl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 日志表 前端控制器
 * </p>
 */
@Controller
@RequestMapping("/admin/log")
public class LogController {

    @Autowired
    ILogService logService;

    @Resource
    SystemColumnUrl systemColumnUrl;

    @RequestMapping()
    public String index(Model model, @RequestParam(value = "page",required = false,defaultValue = "1") Integer page, @RequestParam(value = "limit",required = false,defaultValue = "15")Integer limit){
        PageHelper.startPage(page,limit);
        List<Log> list = logService.list();
        PageInfo<Log> pageinfo = new PageInfo<>(list);
        model.addAttribute("pageinfo",pageinfo);
        model.addAttribute("list",list);
        model.addAttribute("daohang",systemColumnUrl.select(null));
        return  "admin/page/log/log-list";
    }

    @RequiresPermissions("log-delete")
    @ResponseBody
    @PostMapping("/delete")
    public Map<String,Object> delete(Integer id){
        HashMap<String, Object> map = new HashMap<>();
        boolean b = logService.removeById(id);
        if(b){
            map.put("code",200);
            map.put("msg","删除成功");
            return map;
        }
        map.put("code",500);
        map.put("msg","删除失败");
        return map;
    }
    @RequiresPermissions("log-delete")
    @ResponseBody
    @PostMapping("/deleteall")
    public Integer deleteall(String datas){
        String[] split = datas.split(",");
        List<String> strings = Arrays.asList(split);
        boolean b = logService.removeByIds(strings);
        if(b){
            return 200;
        }
        return 500;
    }


}

