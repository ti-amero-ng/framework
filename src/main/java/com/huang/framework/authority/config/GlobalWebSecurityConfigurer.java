package com.huang.framework.authority.config;

import com.huang.framework.authority.service.AbstractCheckSmsCode;
import com.huang.framework.authority.service.DefaultSmsCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * spring security相关配置
 * @Author -Huang
 * @create 2019/9/4 10:05
 */
@Configuration
@Order(SecurityProperties.BASIC_AUTH_ORDER)
public class GlobalWebSecurityConfigurer extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * 如果要让某种运行环境下关闭权限校验，请重写该方法
     * @return
     */
    protected CloseAuthorityEvironment customCloseAuthorityEvironment(){
        return null;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }


    /**
     * 让Security 忽略这些url，不做拦截处理
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers
                ("/swagger-ui.html/**", "/webjars/**",
                        "/swagger-resources/**", "/v2/api-docs/**",
                        "/swagger-resources/configuration/ui/**",
                        "/swagger-resources/configuration/security/**",
                        "/images/**");
    }

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        boolean isCloseAuth;
//
//        CloseAuthorityEvironment closeAuthority = customCloseAuthorityEvironment();
//        if(closeAuthority ==null || closeAuthority.getCloseAuthEnvironment() == null || closeAuthority.getCurrentRunEnvironment()==null){
//            isCloseAuth = false;
//        }else{
//            isCloseAuth = closeAuthority.getCloseAuthEnvironment().equals(closeAuthority.getCurrentRunEnvironment());
//        }
//
//        if(isCloseAuth){
//            closeAuthConfigure(http);
//        }else{
//            customConfigure(http);
//            commonConfigure(http);
//        }
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                .and()
                .requestMatchers()
                .antMatchers("/oauth/authorize")
                .and()
                .authorizeRequests()
                .anyRequest()
                .authenticated().and();
    }

    /**
     * 用户自定义配置，子类可覆盖自定义实现
     * @param http
     * @throws Exception
     */
    protected HttpSecurity customConfigure(HttpSecurity http) throws Exception{
        http.cors().and().csrf().disable().authorizeRequests()
                .anyRequest().authenticated()
                .and();
        return http;
    }

    /**
     * 关闭接口权限校验配置
     * @param http
     * @throws Exception
     */
    private void closeAuthConfigure(HttpSecurity http) throws Exception{
        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers("/**").permitAll();
    }

    private void commonConfigure(HttpSecurity http) throws Exception{
        http.formLogin()
                .and()
                .requestMatchers()
                .antMatchers("/oauth/authorize")
                .and()
                .authorizeRequests()
                .anyRequest()
                .authenticated().and();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    /**
     * 短信验证码登录验证实现类配置
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(AbstractCheckSmsCode.class)
    public AbstractCheckSmsCode smsValidateCheck(){
        return new DefaultSmsCheck();
    }

}
