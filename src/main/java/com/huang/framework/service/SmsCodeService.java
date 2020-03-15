//package com.huang.framework.service;
//
//import cn.hutool.core.util.RandomUtil;
//import com.huang.framework.exception.ServiceException;
//import com.huang.framework.utils.RedisUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
///**
// * 短信发送service,继承AbstractCheckSmsCode，具体逻辑用户实现
// * @author -Huang
// * @create 2020-03-14 14:39
// */
//@Slf4j
//@Service
//public class SmsCodeService implements AbstractCheckSmsCode {
//    public final static String MOBILE_SMS_CODE = "MOBILE_SMS_CODE";
//    private static final Integer CODE_LEN=6;
//
//    @Autowired
//    private RedisUtil redisUtil;
//
//    /**
//     * 模拟发送
//     * @param mobile
//     */
//    public String send(String mobile) {
//        String code = getCode();
//        log.info("send code " + code);
//        redisUtil.set(MOBILE_SMS_CODE + mobile, code );
//        return code;
//    }
//
//    /**
//     * 校验验证码
//     * @param mobile
//     * @param code
//     */
//    @Override
//    public Boolean checkCode(String mobile, String code) {
//        String s = redisUtil.get(MOBILE_SMS_CODE + mobile);
//        log.info("checkCode : " + s);
//        if(null == s || !code.equals(s)){
//            redisUtil.del(MOBILE_SMS_CODE + mobile);
//            throw new ServiceException("验证码错误");
//        }
//        redisUtil.del(MOBILE_SMS_CODE + mobile);
//        return true;
//    }
//
//    /**
//     * 获得随机的code
//     * @return
//     */
//    private String getCode() {
//        StringBuilder code=new StringBuilder();
//        for (int i = 0; i < CODE_LEN; i++) {
//            code.append(RandomUtil.randomInt(0,9));
//        }
//        return code.toString();
//    }
//
//}