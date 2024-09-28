package com.d2csgame.custom;

import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;

public class CustomCacheErrorHandler implements CacheErrorHandler {

    @Override
    public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
        // Ghi log lỗi
        System.err.println("Unable to get from cache " + cache.getName() + " : " + exception.getMessage());
        // Có thể thêm logic khác nếu cần
    }

    @Override
    public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
        // Ghi log lỗi
        System.err.println("Unable to put into cache " + cache.getName() + " : " + exception.getMessage());
    }

    @Override
    public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
        // Ghi log lỗi
        System.err.println("Unable to evict from cache " + cache.getName() + " : " + exception.getMessage());
    }

    @Override
    public void handleCacheClearError(RuntimeException exception, Cache cache) {
        // Ghi log lỗi
        System.err.println("Unable to clear cache " + cache.getName() + " : " + exception.getMessage());
    }
}
