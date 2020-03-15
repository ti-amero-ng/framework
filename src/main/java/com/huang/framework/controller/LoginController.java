package com.huang.framework.controller;

import com.huang.framework.annotation.TRestController;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author -Huang
 * @create 2020-03-03 17:46
 */
@TRestController
public class LoginController {
    @GetMapping("/login")
    public String login(){
        return "未登录";
    }

    @GetMapping("/hello")
    public String hello(){
        return "hello0000";
    }

    @GetMapping("/user")
    public Object getUser(@AuthenticationPrincipal UserDetails userDetails){
        return userDetails;
    }

    @GetMapping("/users")
    public Object getUsers(Authentication authentication){
        return authentication;
    }
}
