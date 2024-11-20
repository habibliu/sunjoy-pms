package com.sunjoy.common.redis.service;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/20
 */
public class LocalCache<K, V> implements Cache<K, V> {
    private final ConcurrentHashMap<K, V> cache = new ConcurrentHashMap<>();

    @Override
    public V get(K key) {
        return cache.get(key);
    }

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
    }

    @Override
    public void remove(K key) {
        cache.remove(key);
    }
}