package com.example.demo;

import com.example.demo.Interceptors.LoggerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created by bang on 2018/5/11.
 */
@Configuration
public class LoggerConfiguration implements WebMvcConfigurer{
    @Autowired
    //日志拦截器
    private LoggerInterceptor loggerInterceptor;
    //配置了静态资源的路径为/example/9/resources/**，
    // 那么只要访问地址前缀是/example/9/resources/，
    // 就会被自动转到项目根目录下的static文件夹内。
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/example/9/resources/**").addResourceLocations("classpath:/static/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loggerInterceptor).addPathPatterns("/**");

    }
}
