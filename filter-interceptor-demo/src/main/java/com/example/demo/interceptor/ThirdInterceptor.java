package com.example.demo.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 第三个拦截器
 */
public class ThirdInterceptor implements HandlerInterceptor {
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) 
            throws Exception {
        System.out.println("---------- ThirdInterceptor preHandle ----------");
        return true;
    }
    
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, 
                          Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("---------- ThirdInterceptor postHandle ----------");
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, 
                               Object handler, Exception ex) throws Exception {
        if (ex != null) {
            System.out.println("---------- ThirdInterceptor afterCompletion (检测到异常: " 
                    + ex.getMessage() + ") ----------");
        } else {
            System.out.println("---------- ThirdInterceptor afterCompletion (正常) ----------");
        }
    }
}
