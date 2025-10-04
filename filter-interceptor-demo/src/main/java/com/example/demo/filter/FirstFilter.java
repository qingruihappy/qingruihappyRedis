package com.example.demo.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 第一个过滤器
 */
public class FirstFilter implements Filter {
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("FirstFilter 初始化");
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        
        System.out.println("========== FirstFilter 前置处理 ==========");
        System.out.println("请求路径: " + req.getRequestURI());
        
        try {
            // 继续执行过滤器链
            chain.doFilter(request, response);
            
            System.out.println("========== FirstFilter 后置处理 ==========");
        } catch (Exception e) {
            System.out.println("========== FirstFilter 捕获到异常: " + e.getMessage() + " ==========");
            // 过滤器可以处理异常，也可以继续抛出
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("Filter捕获异常: " + e.getMessage());
        }
    }
    
    @Override
    public void destroy() {
        System.out.println("FirstFilter 销毁");
    }
}
