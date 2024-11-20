package com.sunjoy.common.redis.service;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/20
 */
public interface CacheServer<K, V> {
    V fetchFromServer(K key);
}