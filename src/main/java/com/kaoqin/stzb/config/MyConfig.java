/*
 * @Date: 2021-07-15 16:24:23
 * @LastEditors: CHEN SHENGWEI
 * @LastEditTime: 2021-10-27 16:57:39
 * @FilePath: \stzb\src\main\java\com\kaoqin\stzb\config\MyConfig.java
 */
package com.kaoqin.stzb.config;

import com.kaoqin.stzb.interceptor.AuthenticationInterceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyConfig implements WebMvcConfigurer {
    // 拦截器运行在bean前面，需要提前注入bean
    @Bean(value = "com.kaoqin.stzb.interceptor.AuthenticationInterceptor")
    public AuthenticationInterceptor authenticationInterceptor() {
        return new AuthenticationInterceptor();
    }
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor()).addPathPatterns("/**")
        .excludePathPatterns("/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**", "/doc.html/**");
    }
}