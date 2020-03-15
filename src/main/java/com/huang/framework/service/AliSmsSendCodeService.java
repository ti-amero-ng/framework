package com.huang.framework.service;

import cn.hutool.core.util.RandomUtil;

/**
 * 短信发送模板
 * @author -Huang
 * @create 2020-03-14 14:41
 */
public class AliSmsSendCodeService extends AbstractCheckSmsCode {
    private static final Integer CODE_LEN=6;

    /**
     * 获得随机的code
     * @return
     */
    private String getCode() {
        StringBuilder code=new StringBuilder();
        for (int i = 0; i < CODE_LEN; i++) {
            code.append(RandomUtil.randomInt(0,9));
        }
        return code.toString();
    }

    @Override
    public void checkCode(String mobile, String code) {

    }

}