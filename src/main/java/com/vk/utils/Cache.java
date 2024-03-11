package com.vk.utils;

import com.vk.entities.Log;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class Cache {

    // k - request, v - {params, response}
    private Map<Pair<String, String>, Object> cache;

    // TODO
    public Object getResponseFromCache(Log log) {
        return null;
    }
}
