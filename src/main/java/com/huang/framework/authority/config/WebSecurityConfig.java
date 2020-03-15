package com.huang.framework.authority.config;

import com.huang.framework.authority.filter.CustomAuthenticationFilter;
import com.huang.framework.authority.filter.GlobalBasicAuthenticationFilter;
import com.huang.framework.authority.filter.SmsCodeAuthenticationFilter;
import com.huang.framework.authority.handler.GlobalAuthenticationFailureHandler;
import com.huang.framework.authority.handler.GlobalAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * spring security相关配置
 * @Author -Huang
 * @create 2019/9/4 10:05
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * 短信验证码验证过滤器
     */
    @Autowired
    private SmsCodeAuthenticationFilter validateCodeFilter;
    /**
     * 短信配置provider
     */
    @Autowired
    private SmsAuthenticationConfig smsAuthenticationConfig;
    /**
     * 认证异常处理器
     */
    @Autowired
    private AuthenticationEntryPoint globalAuthenticationEntryPoint;
    /**
     * 访问无权限资源处理器
     */
    @Autowired
    private AccessDeniedHandler globalAccessDeniedHandler;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * 如果要让某种运行环境下关闭权限校验，请重写该方法
     * @return
     */
    protected CloseAuthorityEvironment customCloseAuthorityEvironment(){
        return null;
    }

    /**
     * Web资源权限控制
     *
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
        web.ignoring().antMatchers("/config/**", "/css/**", "/fonts/**", "/img/**", "/js/**");

        //swagger-ui start
        web.ignoring().antMatchers("/v2/api-docs/**");
        web.ignoring().antMatchers("/swagger.json");
        web.ignoring().antMatchers("/swagger-ui.html");
        web.ignoring().antMatchers("/swagger-resources/**");
        web.ignoring().antMatchers("/webjars/**");
        //swagger-ui end
    }

    /**
     * HTTP请求权限控制
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        boolean isCloseAuth;

        CloseAuthorityEvironment closeAuthority = customCloseAuthorityEvironment();
        if(closeAuthority ==null || closeAuthority.getCloseAuthEnvironment() == null || closeAuthority.getCurrentRunEnvironment()==null){
            isCloseAuth = false;
        }else{
            isCloseAuth = closeAuthority.getCloseAuthEnvironment().equals(closeAuthority.getCurrentRunEnvironment());
        }

        if(isCloseAuth){
            closeAuthConfigure(http);
        }else{
            customConfigure(http);
            commonConfigure(http);
        }
    }


//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        // 添加验证码校验过滤器 在UsernamePasswordAuthenticationFilter之前
//        http.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
//                .httpBasic().authenticationEntryPoint(new JwtAuthenticationEntryPoint())
//                .and()
//                .formLogin() // 表单登录
//                // 处理表单登录url
//                .loginProcessingUrl(SecurityConstants.DEFAULT_LOGIN_URL_USERNAME_PASSWORD)
//                .successHandler(new HAuthenticationSuccessHandler())
//                .failureHandler(new HAuthenticationFailureHandler())
//                .and()
////                .addFilter(new HUsernamePasswordAuthenticationFilter(authenticationManager()))
//                .addFilter(new GlobalBasicAuthenticationFilter(authenticationManager()))
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                // 授权配置
//                .authorizeRequests()
//                // 处理表单登录url
//                .antMatchers( "/css/**", "/code/sms")
//                .permitAll()
//                .anyRequest()
//                .authenticated()
//                .and().csrf().disable()
//                // 将短信验证码认证配置加到 Spring Security 中
//                .apply(smsAuthenticationConfig);
//        http.addFilter(customAuthenticationFilter());
//        http.exceptionHandling().accessDeniedHandler(globalAccessDeniedHandler).authenticationEntryPoint(globalAuthenticationEntryPoint);
//    }

    private void commonConfigure(HttpSecurity http) throws Exception{
        //自定义校验逻辑过滤器配置
        GlobalBasicAuthenticationFilter basicAuthenticationFilter = new GlobalBasicAuthenticationFilter(authenticationManager());

        //配置短信验证码过滤器
        http.addFilter(basicAuthenticationFilter);
        http.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
                .httpBasic().authenticationEntryPoint(globalAuthenticationEntryPoint);

        //表单登录登录配置
        http.formLogin()
                .loginProcessingUrl(SecurityConstants.DEFAULT_LOGIN_URL_USERNAME_PASSWORD)
                .successHandler(new GlobalAuthenticationSuccessHandler())
                .failureHandler(new GlobalAuthenticationFailureHandler());

        http.addFilter(customAuthenticationFilter()).apply(smsAuthenticationConfig);


        //访问异常以及权限异常处理器配置
        http.exceptionHandling().accessDeniedHandler(globalAccessDeniedHandler).authenticationEntryPoint(globalAuthenticationEntryPoint);
        // 禁用 SESSION、JSESSIONID
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
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

    /**
     * 自定义用户名密码json登录过滤器
     * @return
     * @throws Exception
     */
    @Bean
    CustomAuthenticationFilter customAuthenticationFilter() throws Exception {
        CustomAuthenticationFilter filter = new CustomAuthenticationFilter();
        filter.setAuthenticationSuccessHandler(new GlobalAuthenticationSuccessHandler());
        filter.setAuthenticationFailureHandler(new GlobalAuthenticationFailureHandler());
        filter.setAuthenticationManager(authenticationManagerBean());
        return filter;
    }

}
