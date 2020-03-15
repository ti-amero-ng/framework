package com.huang.framework.authority.config;

import com.huang.framework.authority.UserDetailServiceImpl;
import com.huang.framework.authority.handler.HAuthenticationFailureHandler;
import com.huang.framework.authority.handler.HAuthenticationSuccessHandler;
import com.huang.framework.authority.filter.SmsAuthenticationFilter;
import com.huang.framework.authority.provider.SmsAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 短信登录配置类
 * 将自定义Filter与Provider串起来，加入到spring security的整个过滤器链中
 * @author -Huang
 * @create 2020-03-13 11:08
 */
@Configuration
public class SmsAuthenticationConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    @Autowired
    private HAuthenticationFailureHandler authenticationFailureHandler;
    @Autowired
    private HAuthenticationSuccessHandler authenticationSuccessHandler;
    @Autowired
    private UserDetailServiceImpl userDetailService;

    /**
     * 将Filter与Provider串起来
     * @param httpSecurity
     * @throws Exception
     */
    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        //实例化SmsAuthenticationFilter过滤器
        SmsAuthenticationFilter authenticationFilter = new SmsAuthenticationFilter();
        //设置AuthenticationManager Filter和Provider中间的桥梁就是AuthenticationManager
        authenticationFilter.setAuthenticationManager(httpSecurity.getSharedObject(AuthenticationManager.class));
        //登录成功处理
        authenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        //登录失败处理
        authenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler);

        //配置SmsAuthenticationProvider ，注入UserDetailsService
        SmsAuthenticationProvider smsAuthenticationProvider = new SmsAuthenticationProvider();
        smsAuthenticationProvider.setUserDetailsService(userDetailService);

        //调用HttpSecurity的authenticationProvider方法指定了AuthenticationProvider为SmsAuthenticationProvider，
        // 并将SmsAuthenticationFilter过滤器添加到了UsernamePasswordAuthenticationFilter后面。
        httpSecurity.authenticationProvider(smsAuthenticationProvider)
                .addFilterAfter(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}