package com.example.demo.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class DemoInterceptor implements HandlerInterceptor {
    private static final Logger log = LoggerFactory.getLogger(DemoInterceptor.class);

    /**
     * 【前置】Controller执行之前调用
     * return true：放行；return false：中断请求，不再进入Controller
     */
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        log.info("拦截器 preHandle，请求地址：{}", request.getRequestURI());

        // 示例：简单拦截逻辑，如果访问 /api/block 直接拦截
        if("/api/block".equals(request.getRequestURI())){
            response.setStatus(403);
            response.getWriter().write("被拦截，禁止访问");
            return false;
        }
        return true;
    }

    /**
     * 【后置】Controller执行完成后，视图渲染之前
     */
    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView) throws Exception {
        log.info("拦截器 postHandle");
    }

    /**
     * 【完成】整个请求结束之后（视图渲染完毕，异常也会进这里）
     */
    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) throws Exception {
        log.info("拦截器 afterCompletion");
    }
}

