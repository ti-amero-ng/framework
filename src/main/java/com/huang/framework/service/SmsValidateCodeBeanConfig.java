package com.huang.framework.service;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author -Huang
 * @create 2020-03-15 21:02
 */
@Configuration
public class SmsValidateCodeBeanConfig {

    @Bean
    @ConditionalOnMissingBean(AbstractCheckSmsCode.class)
    public AbstractCheckSmsCode smsValidateCheck(){
        return new DefaultSmsCheck();
    }
}
