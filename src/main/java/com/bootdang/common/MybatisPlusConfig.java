package com.bootdang.common;

import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.github.pagehelper.PageInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class MybatisPlusConfig {

 /*   @Primary
     @Bean
    public SqlSessionFactory sqlSessionFactoryBean(DataSource dataSource) throws Exception {
        MybatisSqlSessionFactoryBean mybatisSqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
        mybatisSqlSessionFactoryBean.setDataSource(dataSource);
        PathMatchingResourcePatternResolver article = new PathMatchingResourcePatternResolver();
        //sqlSessionFactoryBean.setConfigLocation(article.getResource("config/mybatis-config.xml"));
        mybatisSqlSessionFactoryBean.setMapperLocations();
        mybatisSqlSessionFactoryBean.setTypeAliasesPackage("com.bootdang.**.entity");
        //mybatisSqlSessionFactoryBean.setGlobalConfig(globalconfig());
        // 分页参数
        PageInterceptor pageHelper = new PageInterceptor();
        Properties properties = new Properties();
        properties.setProperty("helperDialect","mysql");
        properties.setProperty("reasonable","true");
        pageHelper.setProperties(properties);
        Interceptor[] cc=new Interceptor[]{
                pageHelper
        };
        mybatisSqlSessionFactoryBean.setPlugins(cc);
        return mybatisSqlSessionFactoryBean.getObject();
    }*/
 public static void main (String[] args) {
     String[] s=new String[]{};
     String[] c={};
 }
}
