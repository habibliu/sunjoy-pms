package com.sunjoy.park.client.utils;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/12/1
 */


public class CreateUserExample {
    public static void main(String[] args) {
        String url = "http://192.168.2.11:8083/api/v4/users"; // 替换为你的 EMQX API 地址
        String username = "habib"; // 新用户的用户名
        String password = "123456"; // 新用户的密码

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(url);
            post.setHeader("Content-Type", "application/json");

            String json = String.format("{\"username\": \"%s\", \"password\": \"%s\"}", username, password);
            post.setEntity(new StringEntity(json));

            try (CloseableHttpResponse response = client.execute(post)) {
                String responseBody = EntityUtils.toString(response.getEntity());
                System.out.println("Response: " + responseBody);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}