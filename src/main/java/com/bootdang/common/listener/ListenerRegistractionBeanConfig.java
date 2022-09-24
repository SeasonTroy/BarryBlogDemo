package com.bootdang.common.listener;

import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.EventListener;

@Configuration
public class ListenerRegistractionBeanConfig {

    @Bean
    public ServletListenerRegistrationBean servletListenerRegistrationBean1(){
        ServletListenerRegistrationBean<EventListener> eventList = new ServletListenerRegistrationBean<>();
            eventList.setListener(new MySystemListener());

            eventList.setEnabled(true);
            eventList.setOrder(1);
            return eventList;
    }
    @Bean
    public ServletListenerRegistrationBean servletListenerRegistrationBean2(HomeSystemListener homeSystemListener){
        ServletListenerRegistrationBean<EventListener> eventList = new ServletListenerRegistrationBean<>();
        eventList.setListener(homeSystemListener);

        eventList.setEnabled(true);
        eventList.setOrder(2);
        return eventList;
    }
}
