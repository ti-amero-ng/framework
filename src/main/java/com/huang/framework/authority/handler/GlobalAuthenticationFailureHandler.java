package com.huang.framework.authority.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.huang.framework.response.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author -Huang
 * @create 2020-03-03 20:35
 */
@Slf4j
@Component
public class GlobalAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        log.error("SpringSecurity异常："+e.getMessage());
        response.getWriter().write(new ObjectMapper().writeValueAsString(ResponseResult.info(e.getMessage())));
    }
}
