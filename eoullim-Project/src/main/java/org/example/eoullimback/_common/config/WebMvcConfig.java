package org.example.eoullimback._common.config;

import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.interceptor.AdminInterceptor;
import org.example.eoullimback._common.interceptor.LoginInterceptor;
import org.example.eoullimback._common.interceptor.SessionInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final LoginInterceptor loginInterceptor;
    private final SessionInterceptor sessionInterceptor;
    private final AdminInterceptor adminInterceptor;

    private static final String[] EXCLUDE_PATHS = {
            "/auth/**",
            "/",
            "/user/kakao",
            "/api/find/**",
            "/css/**",
            "/js/**",
            "/img/**",
            "/favicon.ico"
    };

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sessionInterceptor)
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/css/**",
                        "/js/**",
                        "/img/**",
                        "/error/**",
                        "/favicon.ico"
                );

        registry.addInterceptor(loginInterceptor)
                .order(2)
                .addPathPatterns("/user/**", "/admin/**")
                .excludePathPatterns(EXCLUDE_PATHS);

        registry.addInterceptor(adminInterceptor)
                .order(3)
                .addPathPatterns("/admin/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:images/", "file:images/roomImages/");
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
