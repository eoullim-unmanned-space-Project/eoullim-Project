package org.example.eoullimback._common.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.example.eoullimback._common.interceptor.LoginInterceptor;
import org.example.eoullimback._common.interceptor.SessionInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final LoginInterceptor loginInterceptor;
    private final SessionInterceptor sessionInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sessionInterceptor)
                .addPathPatterns("/user/**")
                .excludePathPatterns(
                        "/auth/**",
                        "/js/**",
                        "/",
                        "/css/**",
                        "/img/**",
                        "/favicon.ico"
                );

        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/user/**")
                .excludePathPatterns(
                        "/auth/**",
                        "/js/**",
                        "/",
                        "/css/**",
                        "/img/**",
                        "/favicon.ico"
                );
    }
}
