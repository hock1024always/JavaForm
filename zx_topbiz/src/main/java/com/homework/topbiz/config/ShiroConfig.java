package com.homework.topbiz.config;

import com.homework.topbiz.shiro.CustomRealm;
import com.homework.topbiz.shiro.CustomFilter; // 确保这个类是存在的
import org.apache.shiro.authc.Authenticator;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.authz.Authorizer;
import org.apache.shiro.authz.ModularRealmAuthorizer;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor; // 引入 LifecycleBeanPostProcessor
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor; // 引入 AuthorizationAttributeSourceAdvisor
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn; // 如果需要可以保留，但通常 DefaultAdvisorAutoProxyCreator 顺序不是问题
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Arrays;
import java.util.LinkedHashMap; // 引入 LinkedHashMap 保持过滤器顺序
import java.util.Map; // 引入 Map

@Configuration
public class ShiroConfig {

    private final RedisTemplate<String, Object> redisTemplate;

    // CustomFilter 也要作为 Spring Bean 注入
    private final CustomFilter customFilter;

    // 构造器注入所有依赖的 Bean
    public ShiroConfig(RedisTemplate<String, Object> redisTemplate, CustomFilter customFilter) {
        this.redisTemplate = redisTemplate;
        this.customFilter = customFilter; // 注入 CustomFilter
    }

    // 1. Shiro 的生命周期处理器：用于管理 Shiro Bean 的生命周期
    // 必须是 static，并且 Bean 的名称通常是 lifecycleBeanPostProcessor
    @Bean(name = "lifecycleBeanPostProcessor")
    public static LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    // 2. AOP 代理创建器：用于扫描 Advisor 并为符合条件的 Bean 创建代理
    // 这个 BeanPostProcessor 必须是非静态的，或者至少确保它能够正确地被 Spring 处理
    // 移除 static 关键字，并设置 setProxyTargetClass(true)
    @Bean
    // @DependsOn("lifecycleBeanPostProcessor") // 通常不需要，但如果遇到加载顺序问题可以尝试
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true); // 强制使用 CGLIB 代理，对 Shiro 注解生效很关键
        // creator.setUsePrefix(true); // 这个通常不需要，可以移除
        return creator;
    }

    // 3. Shiro 的 Realm
    @Bean
    public CustomRealm customRealm() {
        return new CustomRealm(redisTemplate);
    }

    // 4. Shiro 的认证器
    @Bean
    public Authenticator authenticator(CustomRealm customRealm) {
        ModularRealmAuthenticator authenticator = new ModularRealmAuthenticator();
        authenticator.setRealms(Arrays.asList(customRealm));
        return authenticator;
    }

    // 5. Shiro 的授权器
    @Bean
    public Authorizer authorizer(CustomRealm customRealm) {
        ModularRealmAuthorizer authorizer = new ModularRealmAuthorizer();
        authorizer.setRealms(Arrays.asList(customRealm));
        return authorizer;
    }

    // 6. Shiro 的 Session 管理器
    @Bean
    public SessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setGlobalSessionTimeout(1800000); // 30分钟
        sessionManager.setDeleteInvalidSessions(true);
        sessionManager.setSessionValidationSchedulerEnabled(true);
        return sessionManager;
    }

    // 7. Shiro 的 SecurityManager
    @Bean
    public SecurityManager securityManager(CustomRealm customRealm, Authenticator authenticator, Authorizer authorizer, SessionManager sessionManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(customRealm);
        securityManager.setAuthenticator(authenticator);
        securityManager.setAuthorizer(authorizer);
        securityManager.setSessionManager(sessionManager);

        ThreadContext.bind(securityManager);//加上这句代码手动绑定
        // ... 其他配置，如 CacheManager
        return securityManager;
    }

    // 8. 启用 Shiro 注解：这是将 Shiro 注解与 Spring AOP 结合的关键
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    // 9. ShiroFilterFactoryBean：配置 Shiro 过滤器链
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        // **重要：将 CustomFilter 作为 Bean 注入到 shiroFilterFactoryBean 的 filters 集合中**
        // 这样 Shiro 过滤器才能正确地被 Spring 管理和处理
        Map<String, jakarta.servlet.Filter> filters = new LinkedHashMap<>(); // 使用 LinkedHashMap 保持顺序
        filters.put("custom", customFilter); // customFilter 已经在构造器中注入
        shiroFilterFactoryBean.setFilters(filters);


        // 配置过滤器链定义
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/api/login", "anon");
        filterChainDefinitionMap.put("/api/user/login-with-name-password", "anon");
        filterChainDefinitionMap.put("/api/user/send-verify-code", "anon");
        filterChainDefinitionMap.put("/api/user/login-with-name-verify-code", "anon");
        filterChainDefinitionMap.put("/api/user/userregister", "anon");
        // 配置自定义过滤器，并将其应用到所有其他路径
        filterChainDefinitionMap.put("/**", "custom"); // 使用你定义的过滤器别名

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        return shiroFilterFactoryBean;
    }
}


//package com.homework.topbiz.config;
//
//import com.homework.topbiz.shiro.CustomRealm;
//import com.homework.topbiz.shiro.CustomFilter;
//import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
//import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
//import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.apache.shiro.mgt.SecurityManager;
//import org.springframework.context.annotation.DependsOn;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
//import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
//import org.apache.shiro.authc.Authenticator;
//import org.apache.shiro.authz.ModularRealmAuthorizer;
//import org.apache.shiro.authz.Authorizer;
//import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
//import org.apache.shiro.session.mgt.SessionManager;
//import java.util.Arrays;
//
//@Configuration
//public class ShiroConfig {
//
//    private final RedisTemplate<String, Object> redisTemplate;
//
//    public ShiroConfig(RedisTemplate<String, Object> redisTemplate) {
//        this.redisTemplate = redisTemplate;
//    }
//
//    @Bean
////    @DependsOn("lifecycleBeanPostProcessor")
//    public static DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator(){
//
//        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator=new DefaultAdvisorAutoProxyCreator();
//        defaultAdvisorAutoProxyCreator.setUsePrefix(true);
//
//        return defaultAdvisorAutoProxyCreator;
//    }
//
//    @Bean
//    public CustomRealm customRealm() {
//        return new CustomRealm(redisTemplate);
//    }
//
//    @Bean
//    public Authenticator authenticator(CustomRealm customRealm) {
//        ModularRealmAuthenticator authenticator = new ModularRealmAuthenticator();
//        authenticator.setRealms(Arrays.asList(customRealm));
//        return authenticator;
//    }
//
//    @Bean
//    public Authorizer authorizer(CustomRealm customRealm) {
//        ModularRealmAuthorizer authorizer = new ModularRealmAuthorizer();
//        authorizer.setRealms(Arrays.asList(customRealm));
//        return authorizer;
//    }
//
//    @Bean
//    public SessionManager sessionManager() {
//        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
//        sessionManager.setGlobalSessionTimeout(1800000); // 30分钟
//        sessionManager.setDeleteInvalidSessions(true);
//        sessionManager.setSessionValidationSchedulerEnabled(true);
//        return sessionManager;
//    }
//
//    @Bean
//    public SecurityManager securityManager(CustomRealm customRealm, Authenticator authenticator, Authorizer authorizer, SessionManager sessionManager) {
//        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
//        securityManager.setRealm(customRealm);
//        securityManager.setAuthenticator(authenticator);
//        securityManager.setAuthorizer(authorizer);
//        securityManager.setSessionManager(sessionManager);
//        return securityManager;
//    }
//
//    @Bean
//    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
//        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
//        shiroFilterFactoryBean.setSecurityManager(securityManager);
//
//        // 配置过滤器链
//        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
//        chainDefinition.addPathDefinition("/api/login", "anon");
//        chainDefinition.addPathDefinition("/api/user/login-with-name-password", "anon");
//        chainDefinition.addPathDefinition("/api/user/send-verify-code", "anon");
//        chainDefinition.addPathDefinition("/api/user/login-with-name-verify-code", "anon");
//        chainDefinition.addPathDefinition("/api/user/userregister", "anon");
//
//        chainDefinition.addPathDefinition("/**", "custom");
//
//        shiroFilterFactoryBean.setFilterChainDefinitionMap(chainDefinition.getFilterChainMap());
//
//        // 配置自定义过滤器
//        shiroFilterFactoryBean.getFilters().put("custom", new CustomFilter());
//
//        return shiroFilterFactoryBean;
//    }
//}

