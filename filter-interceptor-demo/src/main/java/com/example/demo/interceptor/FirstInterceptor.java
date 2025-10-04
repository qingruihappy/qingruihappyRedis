package com.example.demo.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 第一个拦截器
 */
public class FirstInterceptor implements HandlerInterceptor {
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) 
            throws Exception {
        System.out.println("---------- FirstInterceptor preHandle ----------");
        // 返回true表示继续执行，返回false表示拦截
        return true;
    }
    
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, 
                          Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("---------- FirstInterceptor postHandle ----------");
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, 
                               Object handler, Exception ex) throws Exception {
        if (ex != null) {
            System.out.println("---------- FirstInterceptor afterCompletion (检测到异常: " 
                    + ex.getMessage() + ") ----------");
        } else {
            System.out.println("---------- FirstInterceptor afterCompletion (正常) ----------");
        }
    }
}
