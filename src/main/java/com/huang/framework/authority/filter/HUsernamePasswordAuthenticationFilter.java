package com.huang.framework.authority.filter;

import com.huang.framework.authority.jwt.JwtTokenUtils;
import com.huang.framework.authority.entity.JwtUser;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 权限校验 用户名密码登录验证过滤器
 * @Author -Huang
 * @create 2019/9/4 10:04
 */
public class HUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    public HUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    /**
     * 接收或解析用户凭证
     * @param request
     * @param response
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        // 从输入流中获取到登录的信息
            String username = this.obtainUsername(request);
            String password = this.obtainPassword(request);
            System.out.println(username + ","+password);
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>())
            );
    }

//    /**
//     * 成功验证后调用的方法
//     * 如果验证成功，就生成token并返回
//     */
//    @Override
//    protected void successfulAuthentication(HttpServletRequest request,
//                                            HttpServletResponse response,
//                                            FilterChain chain,
//                                            Authentication authResult) throws IOException, ServletException {
//
//        JwtUser jwtUser = (JwtUser) authResult.getPrincipal();
//        String token = JwtTokenUtils.createToken(jwtUser.getUsername());
//        // 返回创建成功的token
//        // 按照jwt的规定，最后请求的格式应该是 `Bearer token`
//        System.out.println(token);
//        response.setHeader("token", JwtTokenUtils.TOKEN_PREFIX + token);
//    }

//    /**
//     * 这是验证失败时候调用的方法
//     * @param request
//     * @param response
//     * @param failed
//     * @throws IOException
//     * @throws ServletException
//     */
//    @Override
//    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
//        response.getWriter().write("authentication failed, reason: " + failed.getMessage());
//    }
}

