package com.huang.framework.demo.config;

import com.huang.framework.config.GlobalReturnConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * 统一处理返回配置
 * @author -Tanyq
 * @date  2019-12-23
 */
@EnableWebMvc
@Configuration
@RestControllerAdvice(basePackages = {"com.huang.framework.controller"})
public class ControllerReturnConfig extends GlobalReturnConfig {
}
