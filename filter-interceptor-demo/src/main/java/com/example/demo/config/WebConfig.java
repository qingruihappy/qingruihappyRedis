package com.example.demo.config;

import com.example.demo.filter.FirstFilter;
import com.example.demo.filter.SecondFilter;
import com.example.demo.filter.ThirdFilter;
import com.example.demo.interceptor.FirstInterceptor;
import com.example.demo.interceptor.SecondInterceptor;
import com.example.demo.interceptor.ThirdInterceptor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web配置类 - 注册过滤器和拦截器
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    /**
     * 注册第一个过滤器
     */
    @Bean
    public FilterRegistrationBean<FirstFilter> firstFilter() {
        FilterRegistrationBean<FirstFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new FirstFilter());
        registration.addUrlPatterns("/*");
        registration.setOrder(1); // 设置过滤器顺序
        registration.setName("firstFilter");
        return registration;
    }
    
    /**
     * 注册第二个过滤器
     */
    @Bean
    public FilterRegistrationBean<SecondFilter> secondFilter() {
        FilterRegistrationBean<SecondFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new SecondFilter());
        registration.addUrlPatterns("/*");
        registration.setOrder(2); // 设置过滤器顺序
        registration.setName("secondFilter");
        return registration;
    }
    
    /**
     * 注册第三个过滤器
     */
    @Bean
    public FilterRegistrationBean<ThirdFilter> thirdFilter() {
        FilterRegistrationBean<ThirdFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new ThirdFilter());
        registration.addUrlPatterns("/*");
        registration.setOrder(3); // 设置过滤器顺序
        registration.setName("thirdFilter");
        return registration;
    }
    
    /**
     * 注册拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 拦截器按照添加顺序执行
        registry.addInterceptor(new FirstInterceptor())
                .addPathPatterns("/**");
        
        registry.addInterceptor(new SecondInterceptor())
                .addPathPatterns("/**");
        
        registry.addInterceptor(new ThirdInterceptor())
                .addPathPatterns("/**");
    }
}
