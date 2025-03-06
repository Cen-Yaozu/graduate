package com.example.serve.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC 配置
 * 用于配置静态资源映射等
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${spring.upload.photo-path}")
    private String uploadPhotoPath;

    @Value("${spring.upload.photo-url-prefix}")
    private String photoUrlPrefix;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 输出配置信息
        System.out.println("===== 静态资源路径配置 =====");
        System.out.println("uploadPhotoPath: " + uploadPhotoPath);
        System.out.println("photoUrlPrefix: " + photoUrlPrefix);
        
        // 只保留一个清晰的映射
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:D:/Users/Merri/Desktop/graduate/uploads/");
        
        System.out.println("已配置映射: /uploads/** -> file:D:/Users/Merri/Desktop/graduate/uploads/");
        System.out.println("===== 映射配置完成 =====");
    }
} 