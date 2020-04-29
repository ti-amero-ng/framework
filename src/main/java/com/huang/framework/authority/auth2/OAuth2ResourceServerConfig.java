package com.huang.framework.authority.auth2;

import com.huang.framework.authority.config.CloseAuthorityEvironment;
import com.huang.framework.authority.config.CustomDaoAuthenticationProvider;
import com.huang.framework.authority.filter.CustomAuthenticationFilter;
import com.huang.framework.authority.handler.GlobalAccessDeniedHandler;
import com.huang.framework.authority.handler.GlobalAuthenticationEntryPoint;
import com.huang.framework.authority.handler.GlobalAuthenticationFailureHandler;
import com.huang.framework.authority.handler.GlobalAuthenticationSuccessHandler;
import com.huang.framework.authority.mobile.SmsAuthenticationConfig;
import com.huang.framework.authority.mobile.SmsCodeAuthenticationFilter;
import com.huang.framework.authority.service.AbstractCheckSmsCode;
import com.huang.framework.authority.service.SecurityConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collections;

/**
 * 资源服务配置
 * 添加自定义登录配置 短信、第三方
 * @author -Huang
 * @create 2020-03-18 19:02
 */
@Slf4j
@Configuration
@EnableResourceServer
public class OAuth2ResourceServerConfig extends ResourceServerConfigurerAdapter {
    @Autowired
    private AuthorizationServerTokenServices authorizationServerTokenServices;

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AbstractCheckSmsCode abstractCheckSmsCode;

    @Autowired
    private UserDetailsService userDetailsService;

    private GlobalAuthenticationFailureHandler globalAuthenticationFailureHandler = new GlobalAuthenticationFailureHandler();

    private GlobalAccessDeniedHandler globalAccessDeniedHandler = new GlobalAccessDeniedHandler();

    /**
     * 如果要让某种运行环境下关闭权限校验，请重写该方法
     * @return
     */
    protected CloseAuthorityEvironment customCloseAuthorityEvironment(){
        return null;
    }

    /**
     * 用户自定义配置，子类可覆盖自定义实现
     * @param http
     * @throws Exception
     */
    protected HttpSecurity customConfigure(HttpSecurity http) throws Exception{
        http.cors().and().csrf().disable().authorizeRequests()
                .anyRequest().authenticated();
        return http;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        super.configure(resources);
        resources.authenticationEntryPoint(new AuthExceptionEntryPoint());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        boolean isCloseAuth;

        CloseAuthorityEvironment closeAuthority = customCloseAuthorityEvironment();
        if(closeAuthority ==null || closeAuthority.getCloseAuthEnvironment() == null || closeAuthority.getCurrentRunEnvironment()==null){
            isCloseAuth = false;
        }else{
            isCloseAuth = closeAuthority.getCloseAuthEnvironment().equals(closeAuthority.getCurrentRunEnvironment());
        }

        //关闭权限
        if(isCloseAuth){
            http
                    .authorizeRequests()
                    .anyRequest().permitAll();
        }else {
            customConfigure(http);
        }

        //配置短信验证码过滤器
        http.addFilterBefore(new SmsCodeAuthenticationFilter(abstractCheckSmsCode,globalAuthenticationFailureHandler), UsernamePasswordAuthenticationFilter.class);

        //表单登录登录配置
        http.formLogin()
                .loginProcessingUrl(SecurityConstants.DEFAULT_LOGIN_URL_USERNAME_PASSWORD)
                .successHandler(new GlobalAuthenticationSuccessHandler(authorizationServerTokenServices,clientDetailsService,bCryptPasswordEncoder))
                .failureHandler(globalAuthenticationFailureHandler);

        //添加自定义json登陆处理、短信登陆配置
        http.addFilter(customAuthenticationFilter())
                .apply(new SmsAuthenticationConfig(userDetailsService,authorizationServerTokenServices,clientDetailsService,bCryptPasswordEncoder));

        //访问异常以及权限异常处理器配置
        http.exceptionHandling()
                .accessDeniedHandler(globalAccessDeniedHandler)
                .authenticationEntryPoint(new GlobalAuthenticationEntryPoint());

        // 禁用 SESSION、JSESSIONID
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    /**
     * 自定义用户名密码json登录过滤器
     * @return
     * @throws Exception
     */
    @Bean
    CustomAuthenticationFilter customAuthenticationFilter() throws Exception {
        CustomAuthenticationFilter filter = new CustomAuthenticationFilter();
        filter.setAuthenticationSuccessHandler(new GlobalAuthenticationSuccessHandler(authorizationServerTokenServices,clientDetailsService,bCryptPasswordEncoder));
        filter.setAuthenticationFailureHandler(globalAuthenticationFailureHandler);
        ProviderManager providerManager =
                new ProviderManager(Collections.singletonList(customDaoAuthenticationProvider()));
        filter.setAuthenticationManager(providerManager);
        return filter;
    }

    @Bean
    CustomDaoAuthenticationProvider customDaoAuthenticationProvider(){
        return new CustomDaoAuthenticationProvider(userDetailsService,bCryptPasswordEncoder);
    }
}

