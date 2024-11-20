package com.sunjoy.common.redis.service;

/**
 * 本地缓存与缓存服务器合用
 *
 * @author Habib
 * @date 2024/11/20
 */
public class UniversalCache<K, V> {
    private final LocalCache<K, V> localCache;
    private final CacheServer<K, V> cacheServer;

    public UniversalCache(CacheServer<K, V> cacheServer) {
        this.localCache = new LocalCache<>();
        this.cacheServer = cacheServer;
    }

    public static void main(String[] args) {

        // 模拟缓存服务器
        CacheServer<String, String> cacheServer = new CacheServer<String, String>() {
            @Override
            public String fetchFromServer(String key) {
                // 模拟从服务器获取数据,你可以调用redis缓存服务器去取数据
                System.out.println("Fetching from cache server for key: " + key);
                return "ValueFor_" + key; // 示例返回
            }
        };

        // 创建通用缓存
        UniversalCache<String, String> cache = new UniversalCache<>(cacheServer);

        // 获取数据
        System.out.println(cache.get("key1")); // 从服务器获取
        System.out.println(cache.get("key1")); // 从本地缓存获取
        System.out.println(cache.get("key2")); // 从服务器获取

    }

    public V get(K key) {
        // 先从本地缓存获取
        V value = localCache.get(key);

        if (value != null) {
            // 如果本地缓存存在，直接返回
            return value;
        }

        // 如果本地缓存不存在，从缓存服务器获取,并确保从服务器获取时的线程安全
        synchronized (this) {
            // 可能在此期间被其他线程放入缓存
            value = localCache.get(key);

            if (value == null) {
                value = cacheServer.fetchFromServer(key);

                // 更新本地缓存
                if (value != null) {
                    localCache.put(key, value);
                }
            }
        }
        // 返回从缓存服务器获取的值
        return value;
    }

    public void put(K key, V value) {
        localCache.put(key, value);
    }

    public void remove(K key) {
        localCache.remove(key);
    }
}