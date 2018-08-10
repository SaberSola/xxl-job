package com.github.xxl.job.config;


import com.xxl.job.admin.controller.interceptor.CookieInterceptor;
import com.xxl.job.admin.controller.interceptor.PermissionInterceptor;
import com.xxl.job.admin.controller.resolver.WebExceptionResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * @Author zl
 */
@Configuration
public class SpringMvcConfig extends WebMvcConfigurerAdapter {


    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(new PermissionInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(new CookieInterceptor()).addPathPatterns("/**");

        super.addInterceptors(registry);
    }


    /**
     *
     * 添加异常处理
     * @param exceptionResolvers
     */
    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        exceptionResolvers.add(new WebExceptionResolver());
        super.configureHandlerExceptionResolvers(exceptionResolvers);
    }

    @Override
    public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        super.extendHandlerExceptionResolvers(exceptionResolvers);
    }
}
