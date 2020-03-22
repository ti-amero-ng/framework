package com.huang.framework.authority.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 定义ViewResolver
 * @author -Huang
 * @create 2020-03-22 15:10
 */
@Configuration
@EnableSwagger2
public class WebApplicationConfig extends WebMvcConfigurationSupport {

    @Override
    protected void configureViewResolvers(ViewResolverRegistry registry) {
        registry.viewResolver(new InternalResourceViewResolver());
    }

}