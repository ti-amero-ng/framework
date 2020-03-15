package com.huang.framework.controller;

import com.huang.framework.service.SmsService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 生成短信验证码接口
 * @author -Huang
 * @create 2020-03-13 10:53
 */
@Log
@RestController
public class ValidateController {
    @Autowired
    private SmsService smsService;

    @GetMapping("/code/sms")
    public Object createSmsCode(HttpServletRequest request, String mobile) {
        String send = smsService.send(mobile);
        System.out.println(send);
        return send;
    }

}