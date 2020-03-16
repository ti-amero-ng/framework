package com.huang.framework.authority.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.huang.framework.service.SecurityConstants;
import com.huang.framework.authority.provider.SmsAuthenticationToken;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
* 处理短信登录的过滤器。
* 过滤器的作用就是获取到手机号，然后封装成 SmsAuthenticationToken
 * @author -Huang
 * @create 2020-03-13 10:56
 */
public class SmsAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    public static final String SPRING_SECURITY_FORM_MOBILE_KEY = SecurityConstants.DEFAULT_PARAMETER_NAME_MOBILE;
    private String mobileParameter = SPRING_SECURITY_FORM_MOBILE_KEY;
    private boolean postOnly = true;

    public SmsAuthenticationFilter() {
        super(new AntPathRequestMatcher(SecurityConstants.DEFAULT_LOGIN_URL_MOBILE, HttpMethod.POST.toString()));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response){
        if (postOnly && !request.getMethod().equals(HttpMethod.POST.toString())) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }
        if(!request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE)){
            throw new AuthenticationServiceException(
                    "Authentication method not supported:" + request.getMethod());
        }
        ObjectMapper mapper = new ObjectMapper();
        SmsAuthenticationToken authRequest = null;
        try(InputStream is = request.getInputStream()) {
            Map<String,String> authenticationBean = mapper.readValue(is,Map.class);
            authRequest = new SmsAuthenticationToken(
                    authenticationBean.get(mobileParameter));
        } catch (IOException e) {
            e.printStackTrace();
            authRequest = new SmsAuthenticationToken("");
        }finally {
            setDetails(request,authRequest);
            return this.getAuthenticationManager().authenticate(authRequest);
        }
    }

    protected String obtainMobile(HttpServletRequest request) {
        InputStream is = null;
        String mobile = null;
        try {
            is = request.getInputStream();
            ObjectMapper mapper = new ObjectMapper();
            Map<String,String> authenticationBean = mapper.readValue(is,Map.class);
            mobile = authenticationBean.get(mobileParameter);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mobile;
    }

    protected void setDetails(HttpServletRequest request,
                              SmsAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }

    public void setUsernameParameter(String mobileParameter) {
        Assert.hasText(mobileParameter, "Username parameter must not be empty or null");
        this.mobileParameter = mobileParameter;
    }

    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }

    public final String getMobileParameter() {
        return mobileParameter;
    }
}