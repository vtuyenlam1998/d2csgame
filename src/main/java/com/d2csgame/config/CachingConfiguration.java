package com.d2csgame.config;

import com.d2csgame.custom.CustomCacheErrorHandler;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CachingConfiguration extends CachingConfigurerSupport {
    @Override
    public CacheErrorHandler errorHandler() {
        // Xử lý lỗi khi k connect dc Redis
        return new CustomCacheErrorHandler();
    }
}