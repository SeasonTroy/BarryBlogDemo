package com.bootdang.quartz;

import org.quartz.Scheduler;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.io.IOException;
import java.io.InputStream;
import java.security.cert.X509Certificate;
import java.util.Properties;

@Configuration
public class quartzConfig {
     @Autowired
     JobFactory MyAutoWiredSptingBeanJobFactory;

     @Bean
     public SchedulerFactoryBean schedulerFactoryBean(){
         SchedulerFactoryBean schedulerFactoryBean=null;
         try {
             schedulerFactoryBean = new SchedulerFactoryBean();
         schedulerFactoryBean.setJobFactory(MyAutoWiredSptingBeanJobFactory);
         schedulerFactoryBean.setOverwriteExistingJobs(true);
         //schedulerFactoryBean.setConfigLocation(new ClassPathResource("config/quartz.properties"));
            schedulerFactoryBean.setQuartzProperties(properties());
         } catch (Exception e) {
             e.printStackTrace();
         }
         return schedulerFactoryBean;
     }

     @Bean
     public Properties properties() throws IOException {
         PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();

         propertiesFactoryBean.setLocation(new ClassPathResource("/config/quartz.properties"));

         propertiesFactoryBean.afterPropertiesSet();

         return propertiesFactoryBean.getObject();
     }

     @Bean
     public Scheduler scheduler(){
         Scheduler object = schedulerFactoryBean().getScheduler();
         return object;
     }

}
