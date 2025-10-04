package com.example.demo.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 第二个拦截器 - 可以通过请求参数控制是否抛出异常
 */
public class SecondInterceptor implements HandlerInterceptor {
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) 
            throws Exception {
        System.out.println("---------- SecondInterceptor preHandle ----------");
        
        // 通过请求参数控制是否抛出异常
        String throwInterceptorException = request.getParameter("throwInterceptorException");
        if ("true".equals(throwInterceptorException)) {
            System.out.println("---------- SecondInterceptor 抛出异常！----------");
            throw new Exception("SecondInterceptor 故意抛出的异常");
        }
        
        return true;
    }
    
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, 
                          Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("---------- SecondInterceptor postHandle ----------");
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, 
                               Object handler, Exception ex) throws Exception {
        if (ex != null) {
            System.out.println("---------- SecondInterceptor afterCompletion (检测到异常: " 
                    + ex.getMessage() + ") ----------");
        } else {
            System.out.println("---------- SecondInterceptor afterCompletion (正常) ----------");
        }
    }
}
