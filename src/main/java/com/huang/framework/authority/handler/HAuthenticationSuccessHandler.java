package com.huang.framework.authority.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.huang.framework.authority.entity.JwtUser;
import com.huang.framework.authority.jwt.JwtTokenUtils;
import com.huang.framework.response.ResponseResult;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author -Huang
 * @create 2020-03-03 20:34
 */
@Component
public class HAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//        response.setCharacterEncoding("UTF-8");
//        response.setContentType("application/json; charset=utf-8");
//        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//        response.getWriter().write(new ObjectMapper().writeValueAsString(ResponseResult.info("登录成功。")));

        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
        String token = JwtTokenUtils.createToken(jwtUser.getUsername());
        // 返回创建成功的token
        // 按照jwt的规定，最后请求的格式应该是 `Bearer token`
        System.out.println(token);
        response.setHeader("token", JwtTokenUtils.TOKEN_PREFIX + token);
        response.getWriter().write(new ObjectMapper().writeValueAsString(ResponseResult.info(token)));
    }
}
