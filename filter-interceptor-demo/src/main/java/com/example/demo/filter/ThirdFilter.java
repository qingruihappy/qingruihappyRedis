package com.example.demo.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 第三个过滤器
 */
public class ThirdFilter implements Filter {
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("ThirdFilter 初始化");
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        
        System.out.println("========== ThirdFilter 前置处理 ==========");
        
        // 继续执行过滤器链（进入DispatcherServlet）
        chain.doFilter(request, response);
        
        System.out.println("========== ThirdFilter 后置处理 ==========");
    }
    
    @Override
    public void destroy() {
        System.out.println("ThirdFilter 销毁");
    }
}
