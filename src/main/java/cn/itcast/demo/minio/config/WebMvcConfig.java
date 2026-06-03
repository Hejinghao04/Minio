package cn.itcast.demo.minio.config;

import cn.itcast.demo.minio.interceptor.JwtTokenAdminInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final JwtTokenAdminInterceptor jwtTokenAdminInterceptor;

    public WebMvcConfig(JwtTokenAdminInterceptor jwtTokenAdminInterceptor) {
        this.jwtTokenAdminInterceptor = jwtTokenAdminInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtTokenAdminInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns(
                        "/api/login",
                        "/api/register",
                        "/api/enterPriseRegister",
                        "/api/getSate"
                );
    }
}
