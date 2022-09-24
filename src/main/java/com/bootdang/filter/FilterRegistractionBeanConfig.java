package com.bootdang.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class FilterRegistractionBeanConfig {

    @Bean
    public FilterRegistrationBean xssRequestFilter(){
        FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        filterFilterRegistrationBean.setFilter(new XssRequestFilter());
        filterFilterRegistrationBean.setOrder(Integer.MAX_VALUE-1);//排序为最后
        filterFilterRegistrationBean.setEnabled(true);//开启过滤
        filterFilterRegistrationBean.addUrlPatterns("/*");
        Map<String, String> initParameters = new HashMap();
        //excludes用于配置不需要参数过滤的请求url
        initParameters.put("excludes", "/favicon.ico,/static/*");
        //isIncludeRichText主要用于设置富文本内容是否需要过滤
        initParameters.put("isIncludeRichText", "true");
        filterFilterRegistrationBean.setInitParameters(initParameters);

        return filterFilterRegistrationBean;
    }
}
