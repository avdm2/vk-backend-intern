package com.vk.components;

import com.vk.dto.CacheLogDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class CacheService {

    @Value("${app.cache.size}")
    private Integer cacheSize;

    private final Map<CacheLogDto, String> cache = new LinkedHashMap<>();

    public void put(CacheLogDto log, String response) {
        if (cache.size() == cacheSize) {
            cache.remove(cache.keySet().iterator().next());
        }
        cache.put(log, response);
    }

    public String get(CacheLogDto log) {
        return cache.get(log);
    }
}
