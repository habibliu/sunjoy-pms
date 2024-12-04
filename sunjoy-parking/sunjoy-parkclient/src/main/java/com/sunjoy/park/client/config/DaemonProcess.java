package com.sunjoy.park.client.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunjoy.park.client.domain.LoginResponse;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 与平台通讯的守护程序
 *
 * @author Habib
 * @date 2024/11/29
 */
@Slf4j
@Data
public class DaemonProcess {
    private static volatile DaemonProcess instance = null;
    private final Timer timer;
    private String username;
    private String password;
    private String platformUrl;
    private boolean loggedIn = false;
    private HttpClient httpClient;

    private String token;
    private Integer expiresId;

    private DaemonProcess(String platformUrl, String username, String password) {
        this.platformUrl = platformUrl;
        this.username = username;
        this.password = password;
        this.timer = new Timer();
        // 创建 HTTP 客户端
        this.httpClient = HttpClient.newHttpClient();
    }

    // 公共静态方法，获取唯一实例
    public static DaemonProcess getInstance(String platformUrl, String username, String password) {
        // 第一层检查
        if (instance == null) {
            synchronized (DaemonProcess.class) {
                // 第二层检查
                if (instance == null) {
                    instance = new DaemonProcess(platformUrl, username, password);
                }
            }
        } else {
            if (!instance.platformUrl.equals(platformUrl) || !instance.username.equals(username) || !instance.password.equals(password)) {
                instance.setPlatformUrl(platformUrl);
                instance.setUsername(username);
                instance.setPassword(password);
                instance.loggedIn = false;
            }
        }
        return instance;
    }

    public static void main(String[] args) {
        DaemonProcess daemon = new DaemonProcess("http://example.com", "user", "pass");
        daemon.start();

        // 添加钩子以在 JVM 关闭时停止守护进程
        Runtime.getRuntime().addShutdownHook(new Thread(daemon::stop));
    }

    // 启动守护进程
    public void start() {
        if (!loggedIn) {
            login();
            scheduleHeartbeat();
        }
    }

    // 登录方法
    private void login() {
        try {
            // 假设登录的 API URL
            String loginUrl = platformUrl + "/auth/login";
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(loginUrl))
                    .header("Content-Type", "application/json")
                    .headers("ETag", "ADAPTER")
                    .POST(HttpRequest.BodyPublishers.ofString("{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}"))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                ObjectMapper objectMapper = new ObjectMapper();

                LoginResponse resp = objectMapper.readValue(response.body(), LoginResponse.class);
                if (resp.getCode() == 200 && resp.getData() != null) {
                    loggedIn = true;
                    token = resp.getData().getAccessToken();
                    //todo 将token放进缓存
                    log.info("登录成功");
                }

            } else {
                log.warn("登录失败，状态码:{} ", response.statusCode());
            }
           
        } catch (Exception e) {
            log.error("登录异常", e);
            loggedIn = false;
        }
    }

    // 定期发送心跳请求
    private void scheduleHeartbeat() {
        // 每20秒发送一次心跳
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (loggedIn) {
                    sendHeartbeat();
                } else {
                    // 如果未登录，尝试重新登录
                    login();
                }
            }
        }, 0, 20000);
    }

    // 发送心跳请求
    private void sendHeartbeat() {
        try {
            // 假设心跳的 API URL
            String heartbeatUrl = platformUrl + "/parkmodel/adapter/heartbeat";
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(heartbeatUrl))
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                log.warn("心跳请求失败，准备重新登录，状态码: " + response.statusCode());
                // 标记为未登录
                loggedIn = false;
                // 尝试重新登录
                login();
            }
            log.info("心跳请求成功！");
        } catch (Exception e) {
            log.error("心跳请求异常", e);
            // 标记为未登录
            loggedIn = false;
            // 尝试重新登录
            login();
        }
    }

    // 示例的登录方法，实际实现需要替换
    private boolean loginToPlatform(String username, String password) {
        // 实际登录逻辑
        // 假设登录成功
        return true;
    }

    // 示例的心跳请求方法，实际实现需要替换
    private boolean sendHeartbeatRequest() {
        // 实际心跳请求逻辑
        // 假设请求成功
        return true;
    }

    // 停止守护进程
    public void stop() {
        timer.cancel();
        log.info("守护进程已停止");
    }
}