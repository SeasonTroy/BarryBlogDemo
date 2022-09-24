package com.bootdang.system.service.impl;

import com.bootdang.system.entity.OnlineUser;
import com.bootdang.system.entity.User;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@Service
public class OnlineUserService {
    @Autowired
    SessionDAO sessionDAO;

    /*
    * 查询当前系统在线用户
    * */
    public List<OnlineUser> selectOnLineUser(){
        List<OnlineUser> objects = new LinkedList<>();
        Collection<Session> activeSessions = sessionDAO.getActiveSessions();
        activeSessions.forEach((session)->{
            OnlineUser onlineUser = new OnlineUser();
            if(session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY)!=null){
                SimplePrincipalCollection princip=(SimplePrincipalCollection)session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
                Object primaryPrincipal = princip.getPrimaryPrincipal();
                User of = User.of();
                BeanUtils.copyProperties(primaryPrincipal,of);
                onlineUser.setUser_id(of.getUserId());
                onlineUser.setUsername(of.getUsername());
                onlineUser.setIsadmin(of.getIsAdmin());
            }
            onlineUser.setHost(session.getHost());
            onlineUser.setLogindatetime(session.getStartTimestamp());
            onlineUser.setLastdatetime(session.getLastAccessTime());
            onlineUser.setXhid((String) session.getId());
            onlineUser.setLogoutdatetime(session.getTimeout());

         objects.add(onlineUser);
        });
        return objects;
    }

    public Boolean delete(String id){
        try {
            sessionDAO.delete(sessionDAO.readSession(id));
        }catch (Exception s){
            return false;
        }
      return true;
    }

    public Boolean deleteall(List<String> ids){
       ids.forEach((id)->{
           sessionDAO.delete(sessionDAO.readSession(id));
       });
       return true;
    }

}
