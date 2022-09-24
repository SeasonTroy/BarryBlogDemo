package com.bootdang.common;

import com.bootdang.common.interceptor.MyInterceptor;
import com.bootdang.util.FileAdd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;
import org.springframework.web.servlet.config.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
@EnableConfigurationProperties(FileAdd.class)
public class MvcConfig implements WebMvcConfigurer {

    @Autowired
    FileAdd fileAdd;


    @Override
    public void addInterceptors (InterceptorRegistry registry) {
        registry.addInterceptor(new MyInterceptor()).addPathPatterns("/admin/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /** 图片传路径 */
        registry.addResourceHandler("/upload/**").addResourceLocations("file:"+fileAdd.getPath());
    }

    @Override
    public void addViewControllers (ViewControllerRegistry registry) {
       //registry.addViewController("/add").setViewName("admin/page/systemcolumn/system-add");
    }

    @Override
    public void configureViewResolvers (ViewResolverRegistry registry) {

    }


}
