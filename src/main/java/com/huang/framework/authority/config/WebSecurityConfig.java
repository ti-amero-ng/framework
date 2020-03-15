package com.huang.framework.authority.config;

import com.huang.framework.authority.UserDetailServiceImpl;
import com.huang.framework.authority.entity.SecurityConstants;
import com.huang.framework.authority.exception.GlobalAccessDeniedHandler;
import com.huang.framework.authority.exception.GlobalAuthenticationEntryPoint;
import com.huang.framework.authority.filter.CustomAuthenticationFilter;
import com.huang.framework.authority.filter.HBasicAuthenticationFilter;
import com.huang.framework.authority.filter.HUsernamePasswordAuthenticationFilter;
import com.huang.framework.authority.filter.SmsCodeAuthenticationFilter;
import com.huang.framework.authority.handler.HAuthenticationFailureHandler;
import com.huang.framework.authority.handler.HAuthenticationSuccessHandler;
import com.huang.framework.authority.handler.JwtAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * spring security相关配置
 * @Author -Huang
 * @create 2019/9/4 10:05
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailServiceImpl UserDetailsServiceImpl;
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
     * 自定义用户名密码json登录过滤器
     * @return
     * @throws Exception
     */
    @Bean
    CustomAuthenticationFilter customAuthenticationFilter() throws Exception {
        CustomAuthenticationFilter filter = new CustomAuthenticationFilter();
        filter.setAuthenticationSuccessHandler(new HAuthenticationSuccessHandler());
        filter.setAuthenticationFailureHandler(new HAuthenticationFailureHandler());
        filter.setAuthenticationManager(authenticationManagerBean());
        return filter;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(UserDetailsServiceImpl).passwordEncoder(bCryptPasswordEncoder());
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


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 添加验证码校验过滤器 在UsernamePasswordAuthenticationFilter之前
        http.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
                .httpBasic().authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                .and()
                .formLogin() // 表单登录
                // 处理表单登录url
                .loginProcessingUrl(SecurityConstants.DEFAULT_USERNAME_PASSWORD_LOGIN_URL)
                .successHandler(new HAuthenticationSuccessHandler())
                .failureHandler(new HAuthenticationFailureHandler())
                .and()
//                .addFilter(new HUsernamePasswordAuthenticationFilter(authenticationManager()))
                .addFilter(new HBasicAuthenticationFilter(authenticationManager()))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // 授权配置
                .authorizeRequests()
                // 处理表单登录url
                .antMatchers( "/css/**", "/code/sms")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and().csrf().disable()
                // 将短信验证码认证配置加到 Spring Security 中
                .apply(smsAuthenticationConfig);
        http.addFilter(customAuthenticationFilter());
        http.exceptionHandling().accessDeniedHandler(globalAccessDeniedHandler).authenticationEntryPoint(globalAuthenticationEntryPoint);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }
}
