package com.bootdang.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bootdang.common.aspect.Log;
import com.bootdang.shiro.SessionListenersUser;
import com.bootdang.system.entity.*;
import com.bootdang.system.service.*;
import com.bootdang.util.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.lang.System;
import java.util.*;

@Controller
@RequestMapping("/admin")
public class indexController {

    @Autowired
    IMenuService menuService;
    @Autowired
    IUserService userService;
    @Autowired
    INoticeUserService noticeUserService;

    @Autowired
    SpringEmailUtils springEmailUtils;
    @Autowired
    IArticleService articleService;
    @Resource
    SessionListenersUser sessionListenersUser;

    @Autowired
    FileService fileService;
    @Autowired
    ILogService logService;




    @GetMapping("")
    public String index(Model model){
        int count = noticeUserService.count(new QueryWrapper<NoticeUser>().eq("user_id", ShiroUtils.getUserId()).eq("is_read", 0));
        model.addAttribute("search",menuService.list(new QueryWrapper<Menu>().ne("parentid",0)));
        model.addAttribute("notcount",count);
        return "admin/index";
    }

    @ResponseBody
    @PostMapping("/")
    public Object navbar(){
      List<TreeMenu> treeMenus = menuService.selectMenuAllMy();
      return treeMenus;
    }

    /**
     * 首页
     * @param model
     * @return
     */
    @GetMapping("/main")
    public String main(Model model){
        User user = userService.selectByUserRole(ShiroUtils.getUserId());
        List<Role> role = user.getRole();
        model.addAttribute("rolename",role.get(0).getName());//获取用户角色
        int count = noticeUserService.count(new QueryWrapper<NoticeUser>().eq("user_id", ShiroUtils.getUserId()).eq("is_read", 0));
        model.addAttribute("notcount",count);//当前消息数量
        model.addAttribute("onlinecount",sessionListenersUser.getSessionCount());//在线人数
        model.addAttribute("filecount",fileService.count());//文件数量
        model.addAttribute("usercount",userService.count());//用户数量
        model.addAttribute("warticlestatecount",articleService.count(new QueryWrapper<Article>().eq("state",0)));//未审核文章
        model.addAttribute("yarticlestatecount",articleService.count(new QueryWrapper<Article>().eq("state",1)));//已审核文章
        model.addAttribute("list",logService.list(new QueryWrapper<com.bootdang.system.entity.Log>().orderByDesc("createtime").last("limit 0,8")));


        return "admin/page/main";
    }

    /**
     * 发送邮件
     * @param email
     * @return
     */
    @ResponseBody
    @PostMapping("/sendEmail/{title}")
    @Validated
    public Map<String,Object> sendEmail(@Email(message = "邮箱格式错误") String email,@PathVariable("title") String title){
        Map<String, Object> map = new HashMap<>();
        User user = userService.selectUserByEmail(email);
        if(title.equals("修改密码")){
            if(user==null){
                map.put("code",500);
                map.put("msg","邮箱不存在");
                return map;
            }
        }else if(title.equals("账号注册")){
            if(user!=null){
                map.put("code",500);
                map.put("msg","邮箱已存在");
                return map;
            }
        }

        Random random = new Random();//生成4位验证码
        StringBuffer buffer = new StringBuffer();
        for(int i=0;i<4;i++){
            buffer.append(random.nextInt(9));
        }
        String emailHtml = springEmailUtils.getEmailHtml(email, buffer.toString(),title);
        boolean bol = springEmailUtils.sendMessageHtml(emailHtml, email, title+"验证码");
        if(bol){
            ShiroUtils.getSubjct().getSession().setAttribute("passcode",buffer.toString());//保存到session
            TimerYzm.sendTimer();//开启定时清理
            map.put("code",200);
            map.put("msg","发送成功，请注意查收");
            return map;
        }
        map.put("code",500);
        map.put("msg","发送失败");
        return map;
    }

    @GetMapping("/passwordedit")
    public String passwordedit(){

        return "admin/editpassword";
    }
    @Log(value = "修改密码操作")
    @ResponseBody
    @PostMapping("/updatepass")
    public Map<String,Object> articleadd(String email,String passyzm,String password){
        Map<String, Object> map = new HashMap<>();
        String passcode = (String)ShiroUtils.getSubjct().getSession().getAttribute("passcode");
        if(passcode==null){
            map.put("code",500);
            map.put("msg","验证码不存在或已经过期");
          return map;
        }
        if(!passcode.equals(passyzm)){
            map.put("code",500);
            map.put("msg","验证码错误");
            return map;
        }
        User user = userService.selectUserByEmail(email);
        if(user==null||user.getUserId()!=ShiroUtils.getUserId()){
            map.put("code",500);
            map.put("msg","不是当前用户");
        }
        user.setPassword(new SimpleHash("MD5",password,user.getUsername(),20).toString());
        boolean b = userService.saveOrUpdate(user);
        TimerYzm.timeClose();//关闭定时清理改用手动清理session
        ShiroUtils.getSubjct().getSession().removeAttribute("passcode");
        SecurityUtils.getSubject().logout();
        map.put("code",200);
        map.put("msg","修改成功");
        return map;
    }

    public static void main (String[] args) throws InterruptedException {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run () {
                System.out.println("11");
            }
        },5000);

        Thread.sleep(10000);

        timer.cancel();
    }
}
