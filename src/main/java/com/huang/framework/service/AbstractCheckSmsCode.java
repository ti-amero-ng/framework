package com.huang.framework.service;

/**
 * 验证码校验抽象类
 * @author -Huang
 * @create 2020-03-14 14:42
 */
public interface AbstractCheckSmsCode {
    /**
     * 短信验证码校验逻辑
     * @param mobile
     * @param code
     * @return
     */
    Boolean checkCode(String mobile,String code);
}