package com.bootdang.util;

import com.bootdang.system.service.impl.UserServiceImpl;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;

@Repository
public class MyWebApplictionUtil implements ApplicationContextAware {

    private static  ApplicationContext applicationContext;

    @Override
    public void setApplicationContext (ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }
    public static ApplicationContext getApplicationContext (Class<UserServiceImpl> userServiceClass){
        return applicationContext;
    }
    public static <T> T getBean(Class<T> bean){
        return applicationContext.getBean(bean);
    }
}
