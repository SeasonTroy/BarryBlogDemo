package com.bootdang.shiro;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class SessionListenersUser implements SessionListener {

    private final static AtomicInteger sessioncount= new AtomicInteger(0);

    //开始触发
    @Override
    public void onStart (Session session) {
        sessioncount.incrementAndGet();//加一
    }
   /*
   * 关闭触发
   * */
    @Override
    public void onStop (Session session) {
       sessioncount.decrementAndGet();//减一
    }
   /*
   * 过期触发
   * */
    @Override
    public void onExpiration (Session session) {
       sessioncount.decrementAndGet();//减一
    }

    public  int getSessionCount(){
        return sessioncount.get();
    }
}
