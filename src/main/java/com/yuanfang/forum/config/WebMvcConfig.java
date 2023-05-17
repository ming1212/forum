package com.yuanfang.forum.config;

import com.yuanfang.forum.interceptor.LoginRequiredInterceptor;
import com.yuanfang.forum.interceptor.LoginTicketInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private LoginTicketInterceptor loginTicketInterceptor;

    @Autowired
    private LoginRequiredInterceptor loginRequiredInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginTicketInterceptor)
                .excludePathPatterns("/css/**","/js/**","img/**");

        registry.addInterceptor(loginRequiredInterceptor)
                .excludePathPatterns("/css/**","/js/**","img/**");
    }
}
