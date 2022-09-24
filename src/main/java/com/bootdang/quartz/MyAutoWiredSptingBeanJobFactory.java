package com.bootdang.quartz;


import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import org.springframework.stereotype.Component;

/*
* 让自己写的job注入spring相关的属性 但是此bean不在spring中
* */
@Component
public class MyAutoWiredSptingBeanJobFactory extends SpringBeanJobFactory {

     @Autowired
     AutowireCapableBeanFactory autowireCapableBeanFactory;//

    @Override
    protected Object createJobInstance (TriggerFiredBundle bundle) throws Exception {
        Object jobInstance = super.createJobInstance(bundle);
        autowireCapableBeanFactory.autowireBean(jobInstance);
        return jobInstance;
    }
}
