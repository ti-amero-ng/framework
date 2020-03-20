package com.huang.framework.authority.config;

import com.huang.framework.authority.filter.SmsAuthenticationFilter;
import com.huang.framework.authority.handler.GlobalAuthenticationFailureHandler;
import com.huang.framework.authority.handler.GlobalAuthenticationSuccessHandler;
import com.huang.framework.authority.provider.SmsAuthenticationProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 短信登录配置类
 * 将自定义Filter与Provider串起来，加入到spring security的整个过滤器链中
 * @author -Huang
 * @create 2020-03-13 11:08
 */
public class SmsAuthenticationConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private UserDetailsService userDetailService;

    private AuthorizationServerTokenServices authorizationServerTokenServices;

    private ClientDetailsService clientDetailsService;

    private BCryptPasswordEncoder passwordEncoder;

    public SmsAuthenticationConfig(UserDetailsService userDetailService, AuthorizationServerTokenServices authorizationServerTokenServices, ClientDetailsService clientDetailsService, BCryptPasswordEncoder passwordEncoder){
        this.userDetailService = userDetailService;
        this.authorizationServerTokenServices = authorizationServerTokenServices;
        this.clientDetailsService = clientDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

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
        authenticationFilter.setAuthenticationSuccessHandler(new GlobalAuthenticationSuccessHandler(authorizationServerTokenServices,clientDetailsService,passwordEncoder));
        //登录失败处理
        authenticationFilter.setAuthenticationFailureHandler(new GlobalAuthenticationFailureHandler());

        //配置SmsAuthenticationProvider ，注入UserDetailsService
        SmsAuthenticationProvider smsAuthenticationProvider = new SmsAuthenticationProvider();
        smsAuthenticationProvider.setUserDetailsService(userDetailService);

        //调用HttpSecurity的authenticationProvider方法指定了AuthenticationProvider为SmsAuthenticationProvider，
        // 并将SmsAuthenticationFilter过滤器添加到了UsernamePasswordAuthenticationFilter后面。
        httpSecurity.authenticationProvider(smsAuthenticationProvider)
                .addFilterAfter(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}