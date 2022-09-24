package com.bootdang.home.controller;

import com.alibaba.fastjson.JSON;
import com.bootdang.common.aspect.Log;
import com.bootdang.shiro.MyUserNamePassWord;
import com.bootdang.system.entity.DefaultAdminuser;
import com.bootdang.system.entity.User;
import com.bootdang.system.service.IDefaultAdminuserService;
import com.bootdang.system.service.IUserService;
import com.bootdang.util.ShiroUtils;
import com.bootdang.util.TimerYzm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.h2.H2ConsoleAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/*
这是前台的loginController
 */

@Controller("HomeLoginController")
public class LoginController {
    @Autowired
    IUserService userService;
    @Autowired
    IDefaultAdminuserService iDefaultAdminuserService;

    @ResponseBody
    @PostMapping("/home/login")
    public HashMap<String, Object> login(User user,Integer checked){
        HashMap<String, Object> map = new HashMap<>();
        if(user.getPassword()==null||
                "".equals(user.getPassword())||
                "".equals(user.getUsername())
                ||user.getUsername()==null
        ){
            map.put("code",500);
            map.put("msg","非法登录");
           return map;
       }
        UsernamePasswordToken usernamePasswordToken = new MyUserNamePassWord(user.getUsername(),user.getPassword(),0);

        if(checked==1){
            usernamePasswordToken.setRememberMe(true);
        }
        Subject subject = SecurityUtils.getSubject();
        try {
          subject.login(usernamePasswordToken);
       } catch (IncorrectCredentialsException ice){
           map.put("code",500);
           map.put("msg","密码错误");
           return map;
       }catch (UnknownAccountException uae){
           map.put("code",500);
           map.put("msg","账号不存在");
           return map;
       }catch (AuthenticationException auth){
           map.put("code",500);
           map.put("msg",auth.getMessage());
           return map;
       }catch(Exception e){
           map.put("code",500);
           map.put("msg","登录错误请联系开发者");
           return map;

       }finally {
           usernamePasswordToken.clear();
       }
        if(subject.isAuthenticated()){
            userService.updatelogupdate(ShiroUtils.getUserId());//登录次数加1
            userService.saveOrUpdate(User.of().setUserId(ShiroUtils.getUserId()).setLastlogin(LocalDateTime.now()));//修改登录时间
            subject.getSession().setAttribute("User", ShiroUtils.getUser());//添加session中
            map.put("code",200);
            map.put("msg","登录成功");
            return map;

        }else {
            map.put("code",500);
            map.put("msg","账号登录有误");
            return map;
        }
    }

    @GetMapping("/zhuc")
    public String zhuc(){
        return "home/zhuc";
    }

    /**
     * 用户注册
     * @return
     */
    @ResponseBody
    @PostMapping("/home/zhuc")
    public Map<String,Object> insetuser(@Valid User user, String yzm, BindingResult result){
        HashMap<String, Object> map = new HashMap<>();
        if(result.hasErrors()){
            String defaultMessage = result.getFieldError().getDefaultMessage();
            map.put("code",500);
            map.put("msg",JSON.toJSONString(defaultMessage));
            return map;
        }
        if(yzm==null||!yzm.equals(ShiroUtils.getSubjct().getSession().getAttribute("passcode"))){
         map.put("code",500);
         map.put("msg","验证码错误");
         return map;
        }
        if(userService.SelectByAdminName(user.getUsername())!=null){
            map.put("code",500);
            map.put("msg","账号已存在");
            return map;
        }

        if(userService.selectUserByEmail(user.getEmail())!=null){
            map.put("code",500);
            map.put("msg","邮箱已存在");
            return map;
        }

        user.setCreatetime(LocalDateTime.now());
        DefaultAdminuser defaultAdminuser = (iDefaultAdminuserService.list()).get(0);
        user.setLogo(defaultAdminuser.getDefaultuserLogo());
        user.setContext(defaultAdminuser.getDefaultContext());
        user.setJf(0);
        user.setState("1");
        user.setSex("不明");
        user.setIsAdmin(0);
        user.setName(UUID.randomUUID().toString());
        user.setPassword(new SimpleHash("MD5",user.getPassword(),user.getUsername(),20).toString());
        boolean  save = userService.save(user);

        if(save){
            map.put("code",200);
            map.put("msg","注册成功");
            return map;
        }
        map.put("code",500);
        map.put("msg","注册失败");
        return map;

    }

    @GetMapping(value = "/logout")
    public String loginout(){
        SecurityUtils.getSubject().logout();
        return "redirect:/";
    }
}
