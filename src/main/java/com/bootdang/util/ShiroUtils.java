package com.bootdang.util;

import com.bootdang.system.entity.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


public class ShiroUtils {

    public static Subject getSubjct() {
        return SecurityUtils.getSubject();
    }
    public static User getUser() {
        Object object = getSubjct().getPrincipal();
        User user =null;
        if(object!=null){
           user = User.of();
            BeanUtils.copyProperties(object,user);
        }

        return user;
    }
    public static int getUserId() {
        if(getUser()!=null)
        {
            return  getUser().getUserId();
        }else{
            return 0;
        }

    }
    public static void logout() {
        getSubjct().logout();
    }


}
