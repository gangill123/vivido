package com.vivido.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

 @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/thumbnails/**")
        .addResourceLocations("file:/C:/uploads/thumbnails/");
    
        // Summernote 이미지 업로드 경로 추가
        registry.addResourceHandler("/uploads/summer/**")
                .addResourceLocations("file:/C:/uploads/summer/");
 
 
 }
 

	 
	
}
