package com.bootdang.system.controller;

import com.bootdang.util.WebUtilsPro;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice(basePackages = {"com.bootdang.system.controller","com.bootdang.home.controller"})
@Controller
public class shiroControllerAdvice {

    /*
    * 授权处理
    * */
    @ExceptionHandler({UnauthorizedException.class,AuthorizationException.class})
    public ModelAndView AuthorizationInfo(HttpServletRequest request, HttpServletResponse response){
        ModelAndView modelAndView = new ModelAndView();
        if (WebUtilsPro.isAjaxRequest(request)) {
            // 输出JSON
            Map<String,Object> map = new HashMap<>();
            map.put("code", "999");
            map.put("msg", "你没有权限喔");
            modelAndView.setView(new MappingJackson2JsonView());//返回带类名
            modelAndView.addObject(map);
            return modelAndView;
        } else {
            modelAndView.setViewName("redirect:/unauthorized");
            return  modelAndView;
        }
    }
    /*
    * 认证处理
    * */
    @ExceptionHandler({UnauthenticatedException.class, AuthenticationException.class})
    public ModelAndView AuthenticationInfo(HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
        if (WebUtilsPro.isAjaxRequest(request)) {
            // 输出JSON
            Map<String,Object> map = new HashMap<>();
            map.put("code", "990");
            map.put("msg", "请先登录");
            modelAndView.setView(new MappingJackson2JsonView());//返回带类名
            modelAndView.addObject(map);
            return modelAndView;
        } else {
            modelAndView.setViewName("redirect:/login?user=user");
            return  modelAndView;
        }
    }

    @RequestMapping("/unauthorized")
    public String unauthorized(){
        return "unauthorized/unauthorized";
    }

    @ExceptionHandler(BindException.class)
    @ResponseBody
    public  HashMap<String, Object> handleBindException(BindException e) {
        // 得到第一个字段的错误信息
        String msg = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        HashMap<String, Object> map = new HashMap<>();
        map.put("500",msg);
        return map;
    }
}
