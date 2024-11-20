package com.sunjoy.common.redis.service;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/20
 */
public interface Cache<K, V> {
    V get(K key);

    void put(K key, V value);

    void remove(K key);
}