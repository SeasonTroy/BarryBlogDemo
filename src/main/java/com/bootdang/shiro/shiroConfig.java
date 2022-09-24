package com.bootdang.shiro;
import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import net.sf.ehcache.CacheManager;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import com.bootdang.shiro.MyModularRealmAuthenticator;

import javax.servlet.Filter;
import java.util.*;


@Configuration
public class shiroConfig {

    @Bean(name="shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
         shiroFilterFactoryBean.setLoginUrl("/login");
         shiroFilterFactoryBean.setUnauthorizedUrl("/autherror");
        Map<String, Filter> objectObjectHashMap = new HashMap<>();
         //objectObjectHashMap.put("kickout",kickoutSessionControlFilter(sessionListenersUser()));
          objectObjectHashMap.put("rembuser",new UserSetting());
         shiroFilterFactoryBean.setFilters(objectObjectHashMap);//登录人数控制过滤器

         shiroFilterFactoryBean.setSecurityManager( securityManager());
          Map<String, String> objectObjectLinkedHashMap = new LinkedHashMap<>();
          objectObjectLinkedHashMap.put("/homestatic/**","anon");
        objectObjectLinkedHashMap.put("/","anon");
        objectObjectLinkedHashMap.put("/admin/login","anon");
        objectObjectLinkedHashMap.put("/static/**","anon");
        objectObjectLinkedHashMap.put("/login","anon");
        objectObjectLinkedHashMap.put("/admin/yzm","anon");
        objectObjectLinkedHashMap.put("/admin/code","anon");
        objectObjectLinkedHashMap.put("/upload/**","anon");
        objectObjectLinkedHashMap.put("/swagger-ui.html","anon");
          objectObjectLinkedHashMap.put("/autherror","anon");
          objectObjectLinkedHashMap.put("/admin/logout","anon");
        objectObjectLinkedHashMap.put("/error/404.html","anon");
        objectObjectLinkedHashMap.put("/unauthorized/**","anon");
        objectObjectLinkedHashMap.put("/admin/sendEmail/*","anon");
          objectObjectLinkedHashMap.put("/admin/**","user");
        objectObjectLinkedHashMap.put("/**","anon,rembuser");


        shiroFilterFactoryBean.setFilterChainDefinitionMap(objectObjectLinkedHashMap);
     return shiroFilterFactoryBean;
    }
    //shiro核心管理器
    @Bean
    public SecurityManager securityManager(){
        DefaultSecurityManager defaultSecurityManager = new DefaultWebSecurityManager();
       // defaultSecurityManager.setSessionManager();
        defaultSecurityManager.setAuthenticator(myModularRealmAuthenticator());
        List<Realm> objects = new ArrayList<>();
        objects.add(myrealm());
        objects.add(myadminrealm());

        defaultSecurityManager.setRealms(objects);
        defaultSecurityManager.setRememberMeManager(rememberMeManager());
        defaultSecurityManager.setSessionManager(sessionManager());
        defaultSecurityManager.setCacheManager(shiroehcacheManager());

        return defaultSecurityManager;
    }
    //自定义认证realm规则
    @Bean
    public MyModularRealmAuthenticator myModularRealmAuthenticator(){
        MyModularRealmAuthenticator myModularRealmAuthenticator = new MyModularRealmAuthenticator();
        myModularRealmAuthenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
        return myModularRealmAuthenticator;
    }
    //自定义realm
    @Bean
    public Realm myrealm(){
        myRealm myRealm = new myRealm();
        myRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        myRealm.setAuthenticationCachingEnabled(true);//认证缓存
        myRealm.setAuthorizationCachingEnabled(true);//授权缓存
        return myRealm;
    }
    @Bean
    public Realm myadminrealm(){
        myAdminRealm myRealm = new myAdminRealm();
        myRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        myRealm.setAuthenticationCachingEnabled(true);
        myRealm.setAuthorizationCachingEnabled(false);
        return myRealm;
    }
  //盐值加密
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher(){
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("MD5");
        hashedCredentialsMatcher.setHashIterations(20);
        return hashedCredentialsMatcher;
    }


    //设置记住我
    @Bean
    public RememberMeManager rememberMeManager(){
        CookieRememberMeManager rememberMeManager = new CookieRememberMeManager();
        rememberMeManager.setCookie(rememberMeCookie());
        rememberMeManager.setCipherKey(Base64.getDecoder().decode("2AvVhdsgUs0FSA3SDFAdag=="));
        return rememberMeManager;
    }
    @Bean
    public SimpleCookie rememberMeCookie(){
        SimpleCookie simpleCookie = new SimpleCookie();
        simpleCookie.setName("jlbk.shiro.rememberme");
        simpleCookie.setHttpOnly(true);
        simpleCookie.setPath("/");
        simpleCookie.setMaxAge(864000);
        return simpleCookie;
    }

    /*public CacheManager ehcacheManager(){
        EhCacheManager ehCacheManager = new EhCacheManager();
        ehCacheManager.setCacheManagerConfigFile("classpath:config/ehcache.xml");
        return ehCacheManager;
    }*/
    @Bean
    public SessionListener mysessionListener(){
        return new SessionListenersUser();
    }

    @Bean//自己管理session的配置
    public SessionManager sessionManager(){
        DefaultWebSessionManager defaultWebSessionManager = new DefaultWebSessionManager();
        List<SessionListener> sessionListeners = new ArrayList<>();
        sessionListeners.add(mysessionListener());

        defaultWebSessionManager.setSessionListeners(sessionListeners);//session监听器
        defaultWebSessionManager.setSessionIdCookie(sessionCookie());//sessionid配置
        defaultWebSessionManager.setSessionIdCookieEnabled(true);
        //defaultWebSessionManager.setSession
        defaultWebSessionManager.setSessionDAO(sessionDAO());
        defaultWebSessionManager.setGlobalSessionTimeout(3600000);//60分钟
        defaultWebSessionManager.setDeleteInvalidSessions(true);//回收垃圾session
        defaultWebSessionManager.setSessionValidationSchedulerEnabled(true);//定时检查session是否过期
        defaultWebSessionManager.setSessionValidationInterval(100000);//session检查时间间隔解决用户关闭浏览器10分钟
        defaultWebSessionManager.setSessionIdUrlRewritingEnabled(false);//取消url 后面的 JSESSIONID

        return defaultWebSessionManager;
    }

    /**
     * 配置Shiro生命周期处理器
     * @return
     */
    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * 必须（thymeleaf页面使用shiro标签控制按钮是否显示）
     * 未引入thymeleaf包，Caused by: java.lang.ClassNotFoundException: org.thymeleaf.dialect.AbstractProcessorDialect
     * @return
     */
    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }


    @Bean
    public SessionDAO sessionDAO(){
        EnterpriseCacheSessionDAO enterpriseCacheSessionDAO = new EnterpriseCacheSessionDAO();
        enterpriseCacheSessionDAO.setCacheManager(shiroehcacheManager());
        enterpriseCacheSessionDAO.setActiveSessionsCacheName("jlbk.shiro.systemcache");
        enterpriseCacheSessionDAO.setSessionIdGenerator(sessionIdGenerator());
        return enterpriseCacheSessionDAO;
    }


    @Bean//session 的key生成策略
    public SessionIdGenerator sessionIdGenerator(){
        return new JavaUuidSessionIdGenerator();
    }

    @Bean//shiro对ehcache的支持
    public EhCacheManager shiroehcacheManager(){
        EhCacheManager ss= new EhCacheManager();
       // ehCacheManager.setCacheManager();
        //ss.setCacheManager(cacheManager());
        ss.setCacheManagerConfigFile("classpath:config/ehcache.xml");


        return ss;
    }
    @Primary
    @Bean
    public CacheManager shirocacheManager(){
        return CacheManager.create();
    }

    @Bean//session的cookie配置
    public SimpleCookie sessionCookie(){
        SimpleCookie simpleCookie = new SimpleCookie("jlbk.shiro.sessionid");
        simpleCookie.setMaxAge(-1);
        simpleCookie.setHttpOnly(true);
        simpleCookie.setPath("/");
        return simpleCookie;
    }

    /*
     * shiro的注解配置
     * */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SessionListenersUser sessionListenersUser){
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager());
        return authorizationAttributeSourceAdvisor;
    }

    @Bean//代理类实现切入
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        defaultAdvisorAutoProxyCreator.setUsePrefix(true);
        return defaultAdvisorAutoProxyCreator;
    }
    /*
     * 注解方式未授权返回地址
     * */
   /* @Bean
    public SimpleMappingExceptionResolver simpleMappingExceptionResolver(){
        SimpleMappingExceptionResolver simpleMappingExceptionResolver = new SimpleMappingExceptionResolver();
        Properties properties = new Properties();
       // properties.setProperty("UnauthorizedException","403");
        properties.setProperty("org.apache.shiro.authz.UnauthorizedException","/unauthorized/unauthorized");
        properties.setProperty("org.apache.shiro.authz.UnauthenticatedException","/unauthorized/unauthorized");
        simpleMappingExceptionResolver.setExceptionMappings(properties);
        return simpleMappingExceptionResolver;
    }*/
    /**
     * 并发登录控制
     * @return
     */
    @Bean
    public KickoutSessionControlFilter kickoutSessionControlFilter(SessionListenersUser sessionListenersUser){
        KickoutSessionControlFilter kickoutSessionControlFilter = new KickoutSessionControlFilter();
        //用于根据会话ID，获取会话进行踢出操作的；
        kickoutSessionControlFilter.setSessionManager(sessionManager());
        //使用cacheManager获取相应的cache来缓存用户登录的会话；用于保存用户—会话之间的关系的；
        kickoutSessionControlFilter.setCacheManager(shiroehcacheManager());
        //是否踢出后来登录的，默认是false；即后者登录的用户踢出前者登录的用户；
        kickoutSessionControlFilter.setKickoutAfter(false);
        //同一个用户最大的会话数，默认1；比如2的意思是同一个用户允许最多同时两个人登录；
        kickoutSessionControlFilter.setMaxSession(1);
        //被踢出后重定向到的地址；
        kickoutSessionControlFilter.setKickoutUrl("/login?kickout=1");
        return kickoutSessionControlFilter;
    }




}
