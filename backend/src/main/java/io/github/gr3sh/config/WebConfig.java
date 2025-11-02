package io.github.gr3sh.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        // 将本地上传目录映射到 /uploads/**
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:E:/Work/blog/database/uploads/");
    }
}
