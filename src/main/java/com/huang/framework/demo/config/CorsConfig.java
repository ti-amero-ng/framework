package com.huang.framework.demo.config;

import com.huang.framework.config.GlobalCorsConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * 跨域配置
 * @author -Tanqy
 * @date  2019-12-23
 */
@Configuration
@EnableWebMvc
public class CorsConfig extends GlobalCorsConfig {

}