package com.bootdang.common.interceptor;

import com.bootdang.system.entity.User;
import com.bootdang.util.ShiroUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class MyInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle (HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User user = ShiroUtils.getUser();
        if(user==null){
            return true;
        }else {
            if (user.getIsAdmin()!=null&&user.getIsAdmin()== 1) {
                return true;
            }
            response.sendRedirect("/");
            return false;
        }
    }

    @Override
    public void postHandle (HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion (HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
