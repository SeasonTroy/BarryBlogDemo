package com.bootdang.shiro;

import com.bootdang.system.entity.User;
import com.bootdang.system.service.IUserService;
import com.bootdang.util.ShiroUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class UserSetting extends FormAuthenticationFilter {


    @Override
    protected boolean isAccessAllowed (ServletRequest request, ServletResponse response, Object mappedValue) {
        Subject subject = getSubject(request, response);
        // 如果是记住我登录的，则需要处理一下
        // isRemembered为true、isAuthenticated为false
        if (!subject.isAuthenticated() && subject.isRemembered()) {
            // 通过记住我第一次进程序，并且保存的principal中有内容，添加用户到session
            if (subject.getSession().getAttribute("User") == null && subject.getPrincipal() != null) {
                subject.getSession().setAttribute("User", subject.getPrincipal());
            }
        }
        return true;
    }
}
