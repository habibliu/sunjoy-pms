package com.sunjoy.parkctrl.utils;

import java.util.Map;
import java.util.TreeMap;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/26
 */
public class SignUtils {
    public static String createSign(TreeMap<String, String> params, String key) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (entry.getValue() != null && !entry.getValue().isEmpty()) {
                sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
        }
        // 添加商户密钥
        sb.append("key=").append(key);
        // 计算签名并转为大写
        return MD5Util.md5(sb.toString()).toUpperCase();
    }
}