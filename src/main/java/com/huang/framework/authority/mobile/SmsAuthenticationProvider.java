package com.huang.framework.authority.mobile;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * 指定支持处理的Token类型为SmsAuthenticationToken  短信登录token
 * @author -Huang
 * @create 2020-03-13 10:59
 */
@Slf4j
public class SmsAuthenticationProvider implements AuthenticationProvider {
    private UserDetailsService userDetailsService;

    public void setUserDetailsService(UserDetailsService userDetailsService){
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        //转换过滤器传过来的SmsAuthenticationToken
        SmsAuthenticationToken smsAuthenticationToken = (SmsAuthenticationToken) authentication;
        //通过 loadUserByUsername 方法查询对于用户
        UserDetails userDetails = userDetailsService.loadUserByUsername((String) authentication.getPrincipal());

        if (userDetails == null) {
            throw new InternalAuthenticationServiceException("无法获取用户信息");
        }

        //生成新的SmsAuthenticationToken
        SmsAuthenticationToken authenticationResult = new SmsAuthenticationToken(userDetails.getAuthorities(), userDetails);
        authenticationResult.setDetails(smsAuthenticationToken.getDetails());
        return authenticationResult;
    }

    /**
     * AuthencationManager负责协调由那个Provider来处理对应的Filter, 具体协调的过程就是通过该方法来协调的.
     * 判断传入的参数 是否和 PhoneNumAuthenticationToken.class 为同一个类型。如果是同一个类型，就执行authenticate方法。
     *
     * @param authentication
     * @return
     */
    @Override
    public boolean supports(Class<?> authentication) {
        //isAssignableFrom() 该方法是判断两个Class类型的对象是否为同一个。
        return authentication.isAssignableFrom(SmsAuthenticationToken.class);
    }
}