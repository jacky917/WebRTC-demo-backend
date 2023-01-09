package com.demo.webrtc.config;

import com.demo.webrtc.config.shiro.FirstShiroRealm;
import com.demo.webrtc.config.shiro.SecondShiroRealm;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

//    @Bean
//    RememberMeManager rememberMeManager(){
//        CookieRememberMeManager rememberMeManager = new CookieRememberMeManager();
//        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
//        simpleCookie.setHttpOnly(true);
//        // 有效期七天
//        simpleCookie.setMaxAge(604800);
//        rememberMeManager.setCookie(simpleCookie);
//        rememberMeManager.setCipherKey(Base64.getDecoder().decode("1111111"));
//        return rememberMeManager;
//    }

    @Bean
    public ModularRealmAuthenticator authenticator(){
        ModularRealmAuthenticator modularRealmAuthenticator = new ModularRealmAuthenticator();
//        ArrayList<Realm> realms = new ArrayList<>();
//        realms.add(this.firRealm());
//        realms.add(this.secRealm());
//        modularRealmAuthenticator.setRealms(realms);
//        modularRealmAuthenticator.setAuthenticationStrategy(new AllSuccessfulStrategy());
//        modularRealmAuthenticator.setAuthenticationStrategy(new FirstSuccessfulStrategy());
        modularRealmAuthenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
        return modularRealmAuthenticator;
    }

    @Bean
    public ShiroFilterFactoryBean filterFactoryBean(@Qualifier("manager") DefaultWebSecurityManager manager){
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();

        Map<String,String> map = new LinkedHashMap<>();
        map.put("/login","anon");
        map.put("/test","anon");
        map.put("/logout","logout");

//        map.put("/manage","perms[manage]");
        map.put("/admin","roles[admin]");
        map.put("/manage","roles[manage]");

//        map.put("/**","authc");
        map.put("/**","anon");

        //设置登录页面
        shiroFilter.setLoginUrl("/login");
        //未授权页面
        shiroFilter.setUnauthorizedUrl("/authc");
        shiroFilter.setFilterChainDefinitionMap(map);

        shiroFilter.setSecurityManager(manager);
        return shiroFilter;
    }


//    @Bean
//    public DefaultWebSecurityManager manager(@Qualifier("firRealm") FirstShiroRealm firstShiroRealm){
//        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
//        manager.setRealm(firstShiroRealm);
//        return manager;
//    }

    @Bean
    public DefaultWebSecurityManager manager(@Qualifier("authenticator") ModularRealmAuthenticator modularRealmAuthenticator){
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setAuthenticator(modularRealmAuthenticator);
        ArrayList<Realm> realms = new ArrayList<>();
        realms.add(this.firRealm());
        realms.add(this.secRealm());
        manager.setRealms(realms);
        return manager;
    }


    @Bean
    public FirstShiroRealm firRealm(){
        return new FirstShiroRealm();
    }

    @Bean
    public SecondShiroRealm secRealm(){
        return new SecondShiroRealm();
    }

//    @Bean
//    public Realm getRealm(){
//        //设置凭证匹配器，修改为hash凭证匹配器
//        HashedCredentialsMatcher myCredentialsMatcher = new HashedCredentialsMatcher();
//        //设置算法
//        myCredentialsMatcher.setHashAlgorithmName("md5");
//        //散列次数
//        myCredentialsMatcher.setHashIterations(512);
//        ShiroRealm realm = new ShiroRealm();
//        realm.setCredentialsMatcher(myCredentialsMatcher);
//        return realm;
//    }

}