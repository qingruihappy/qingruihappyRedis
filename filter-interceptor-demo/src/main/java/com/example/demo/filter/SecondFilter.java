package com.example.demo.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 第二个过滤器 - 可以通过请求参数控制是否抛出异常
 */
public class SecondFilter implements Filter {
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("SecondFilter 初始化");
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        
        System.out.println("========== SecondFilter 前置处理 ==========");
        
        // 通过请求参数控制是否抛出异常
        String throwFilterException = req.getParameter("throwFilterException");
        if ("true".equals(throwFilterException)) {
            System.out.println("========== SecondFilter 抛出异常！==========");
            throw new ServletException("SecondFilter 故意抛出的异常");
        }
        
        // 继续执行过滤器链
        chain.doFilter(request, response);
        
        System.out.println("========== SecondFilter 后置处理 ==========");
    }
    
    @Override
    public void destroy() {
        System.out.println("SecondFilter 销毁");
    }
}
