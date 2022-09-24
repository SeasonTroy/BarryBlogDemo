package com.bootdang.common.listener;

import com.bootdang.system.service.ISystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

@Component
public class HomeSystemListener implements ServletContextListener {
     @Autowired
    ISystemService iSystemService;

    @Override
    public void contextInitialized (ServletContextEvent sce) {
       sce.getServletContext().setAttribute("systemparam",iSystemService.list().get(0));
    }

    @Override
    public void contextDestroyed (ServletContextEvent sce) {

    }
}

