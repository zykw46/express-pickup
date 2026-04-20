package com.express.pickup.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 跨域配置类
 */
@Configuration
public class CorsConfig {

    /**
     * 跨域过滤器
     * @return
     * setAllowCredentials(true)：允许携带凭证（如Cookie、Authorization头等）
     * addAllowedOriginPattern("*")：允许所有来源的跨域请求
     * addAllowedHeader("*")：允许所有请求头
     * addAllowedMethod("*")：允许所有HTTP方法
     * addExposedHeader("Authorization")：暴露Authorization响应头给前端
     * registerCorsConfiguration("/**", config)：将配置应用到所有路径
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("http://localhost:3000");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.addExposedHeader("Authorization");
        config.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        
        return new CorsFilter(source);
    }
}
