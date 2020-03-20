package com.huang.framework.authority.service;

/**
 * @author -Huang
 * @create 2020-03-15 21:00
 */
public class DefaultSmsCheck implements AbstractCheckSmsCode {
    @Override
    public Boolean checkCode(String mobile, String code) {
        System.out.println("手机号： "+mobile +",验证码： "+code+"验证码通过");
        return true;
    }
}
