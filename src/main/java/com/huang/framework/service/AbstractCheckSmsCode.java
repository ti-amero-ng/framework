package com.huang.framework.service;

import org.springframework.stereotype.Service;

/**
 * 验证码校验抽象类
 * @author -Huang
 * @create 2020-03-14 14:42
 */
@Service
public class AbstractCheckSmsCode {
    /**
     * 短信验证码校验逻辑
     * @param mobile
     * @param code
     */
    public Boolean checkCode(String mobile,String code){
        System.out.println("校验验证码");
        return true;
    }
}