package com.bootdang.system.controller;

import com.bootdang.shiro.SessionListenersUser;
import com.bootdang.system.entity.OnlineUser;
import com.bootdang.system.service.impl.OnlineUserService;
import com.bootdang.util.SystemColumnUrl;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/useronline")
public class SystemUserOnLineController  {

    @Autowired
    OnlineUserService onlineUserService;

    @Resource
    SessionListenersUser sessionListenersUser;

    @Resource
    SystemColumnUrl systemColumnUrl;

    @RequestMapping()
    public String index(Model model){


        model.addAttribute("zxuser",sessionListenersUser.getSessionCount());
        model.addAttribute("daohang",systemColumnUrl.select("/admin/useronline"));
        return "admin/page/onlineuser/user_list";
    }

    @ResponseBody
    @GetMapping("/selectuseronline")
    public List<OnlineUser> select(){
         return onlineUserService.selectOnLineUser();
    }

    @RequiresPermissions("onlineuser-delete")
    @ResponseBody
    @PostMapping("/delete")
    public Map<String,Object> delete(String sessionid){
        HashMap<String, Object> objectObjectHashMap = new HashMap<>();
        Boolean delete = onlineUserService.delete(sessionid);
        if(delete){
           objectObjectHashMap.put("code",200);
           objectObjectHashMap.put("msg","移除成功");
           return objectObjectHashMap;
        }
        objectObjectHashMap.put("code",500);
        objectObjectHashMap.put("msg","移除失败");
        return objectObjectHashMap;
    }

    @RequiresPermissions("onlineuser-delete")
    @ResponseBody
    @PostMapping("/deleteall")
    public Map<String,Object> deleteall (String datas){
        String[] split = datas.split(",");
        HashMap<String, Object> objectObjectHashMap = new HashMap<>();
        Boolean delete = onlineUserService.deleteall(Arrays.asList(split));
        if(delete){
            objectObjectHashMap.put("code",200);
            objectObjectHashMap.put("msg","移除成功");
            return objectObjectHashMap;
        }
        objectObjectHashMap.put("code",500);
        objectObjectHashMap.put("msg","移除失败");
        return objectObjectHashMap;
    }

}
