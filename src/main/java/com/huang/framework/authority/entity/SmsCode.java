package com.huang.framework.authority.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author -Huang
 * @create 2020-03-13 10:52
 */
@Data
public class SmsCode {
    /**
     * 验证码
     */
    private String code;
    /**
     * 过期时间
     */
    private LocalDateTime expireTime;

    public SmsCode(String code, int expireIn) {
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
    }

    public boolean isExpire() {
        return LocalDateTime.now().isAfter(expireTime);
    }
}