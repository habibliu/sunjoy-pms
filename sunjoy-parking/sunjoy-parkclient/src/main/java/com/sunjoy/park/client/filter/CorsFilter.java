package com.sunjoy.park.client.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * 跨域过滤器
 *
 * @author Habib
 * @date 2024/11/29
 */


public class CorsFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        // 允许的源
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:9000");
        // 允许的方法
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        // 允许的头部
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        // 预检请求的缓存时间
        response.setHeader("Access-Control-Max-Age", "3600");

        // 如果是预检请求，直接返回
        if ("OPTIONS".equalsIgnoreCase(((HttpServletRequest) req).getMethod())) {
            return;
        }
        // 继续处理请求
        chain.doFilter(req, res);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 初始化方法
    }

    @Override
    public void destroy() {
        // 清理方法
    }
}