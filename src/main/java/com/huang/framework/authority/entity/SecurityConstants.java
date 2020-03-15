package com.huang.framework.authority.entity;

/**
 * @author -Huang
 * @create 2020-03-04 10:09
 */
public interface SecurityConstants {
    /**
     * 默认的用户名密码登录请求处理url
     */
    String DEFAULT_USERNAME_PASSWORD_LOGIN_URL = "/login";

    /**
     * 默认的手机验证码登录请求处理url
     */
    String DEFAULT_MOBILE_LOGIN_URL = "/login/mobile";

    /**
     * 默认的处理验证码的url前缀
     */
    String DEFAULT_VALIDATE_CODE_URL_PREFIX = "/code";

    /**
     * 验证短信验证码时，http请求中默认的携带短信验证码信息的参数的名称
     */
    String DEFAULT_PARAMETER_NAME_CODE_SMS = "smsCode";

    /**
     * 发送短信验证码 或 验证短信验证码时，传递手机号的参数的名称
     */
    String DEFAULT_PARAMETER_NAME_MOBILE = "mobile";
}
