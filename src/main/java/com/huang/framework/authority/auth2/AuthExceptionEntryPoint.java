package com.huang.framework.authority.auth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.huang.framework.response.ResponseResult;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author -Huang
 * @create 2020-03-20 19:38
 */
public class AuthExceptionEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException)
            throws ServletException {

        Map map = new HashMap();
        map.put("code", "401");
        map.put("msg", "无效令牌");
        map.put("data", authException.getMessage());
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        authException.printStackTrace();
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getOutputStream(),map);
        } catch (Exception e) {
            throw new ServletException();
        }
    }
}