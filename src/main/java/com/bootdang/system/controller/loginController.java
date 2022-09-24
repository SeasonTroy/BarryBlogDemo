package com.bootdang.system.controller;

import com.alibaba.fastjson.JSON;
import com.bootdang.common.aspect.Log;
import com.bootdang.shiro.MyUserNamePassWord;
import com.bootdang.system.entity.User;
import com.bootdang.system.service.impl.UserServiceImpl;
import com.bootdang.util.HttpResult;
import com.bootdang.util.RndServlet;
import com.bootdang.util.ShiroUtils;
import com.sun.org.apache.regexp.internal.RE;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sun.font.TrueTypeFont;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.sql.ResultSet;
import java.time.LocalDateTime;

/*
这是后台登录的loginController
 */

@Controller
public class loginController {

    static final Logger log=LoggerFactory.getLogger(loginController.class);

    @Autowired
    UserServiceImpl userService;

    @Autowired
    RndServlet rndServlet;

    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public String login(@RequestParam( value = "kickout",required = false) String kickout,Model model){
        if(kickout!=null&&kickout.equals("1")) {
            model.addAttribute("msg","此用户在异地登录 账户可能存在安全隐患！");
        }
//        return "login";
        return "admin/login";
    }

    @RequestMapping(value = "/admin/login",method = RequestMethod.GET)
    public String LoginUrl(){
        return "admin/login";
    }

    @RequestMapping(value="/admin/yzm")
    public void yzmcode( HttpServletResponse response){
        try {
            rndServlet.getCode(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

     @ResponseBody
    @RequestMapping(value = "/admin/code",method = RequestMethod.GET)
    public String codeequal(String code){
         Subject subject = SecurityUtils.getSubject();
         String codec = (String)subject.getSession().getAttribute("code");
         if(codec==null||code.toLowerCase().equals(codec.toLowerCase())){
           return "1";
         }
        return "0";
    }

    //登录
    @Log(value = "登录操作")
    @ResponseBody
    @RequestMapping(value = "/admin/login",method = RequestMethod.POST)
    public String adminLogin(String username, String password, String yzmcode, int checkbox, HttpSession session){
        Subject subject = SecurityUtils.getSubject();
        String code = (String)subject.getSession().getAttribute("code");

        if(code==null||!code.toLowerCase().equals(yzmcode.toLowerCase())){
            return "验证码错误";
        }
       if(username.equals("")||password.equals("")){
          return "账号密码不能为空";
       }

      /*  if(result.hasErrors()){
            String defaultMessage = result.getFieldError().getDefaultMessage();
            return JSON.toJSONString(defaultMessage);
        }*/
        MyUserNamePassWord usernamePasswordToken = new MyUserNamePassWord(username,password,1);
        if(checkbox==1){
            usernamePasswordToken.setRememberMe(true);
        }
        try {
            subject.login(usernamePasswordToken);

        } catch (IncorrectCredentialsException ice){
           return  "密码错误";
        }catch (UnknownAccountException uae){
            return  "账号不存在";
        }catch (AuthenticationException auth){
            log.debug(auth.getMessage());
            return auth.getMessage();
        }catch(Exception e){
            return  "登录错误请联系开发者";
        }finally {
            usernamePasswordToken.clear();
        }
        if(subject.isAuthenticated()){
            userService.updatelogupdate(ShiroUtils.getUserId());//登录次数加1
            userService.saveOrUpdate(User.of().setUserId(ShiroUtils.getUserId()).setLastlogin(LocalDateTime.now()));//修改登录时间
            session.setAttribute("User", ShiroUtils.getUser());//添加session中
            return "1";
        }else {
            return "后台登陆有误";
        }
    }
    @Log(value = "退出操作")
    @GetMapping(value = "/admin/logout")
    public String loginout(){
        SecurityUtils.getSubject().logout();
        return "redirect:/admin/login";
    }
    //注册

    @ResponseBody
    @RequestMapping(value = "/admin/Register",method = RequestMethod.POST)
    public String AdminRegister(@Valid User user , BindingResult result){
        if(result.hasErrors()){
            String defaultMessage = result.getFieldError().getDefaultMessage();
           return JSON.toJSONString(defaultMessage);
        }
        boolean save = userService.save(user);
        HttpResult httpResult = new HttpResult();
        if(save){
            httpResult.setState(200);
            httpResult.setMsg("注册成功");
            return JSON.toJSONString(httpResult);
        }
        httpResult.setState(400);
        httpResult.setMsg("注册失败");
        return JSON.toJSONString(httpResult);
    }
}
