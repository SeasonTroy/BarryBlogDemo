package com.bootdang.common.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class MySystemListener implements ServletContextListener {

    @Override
    public void contextInitialized (ServletContextEvent sce) {
        sce.getServletContext().setAttribute("version","1.0v");
        sce.getServletContext().setAttribute("Opener","java攀登");
        sce.getServletContext().setAttribute("systemversion",System.getProperty("java.version"));
        sce.getServletContext().setAttribute("insertpath",System.getProperty("java.home"));
        sce.getServletContext().setAttribute("jvmversion",System.getProperty("java.vm.specification.version"));
        sce.getServletContext().setAttribute("caozuosys",System.getProperty("os.name"));
        sce.getServletContext().setAttribute("maxfilesize","30MB");
        sce.getServletContext().setAttribute("userpath",System.getProperty("user.dir"));
    }

    @Override
    public void contextDestroyed (ServletContextEvent sce) {

    }
}
