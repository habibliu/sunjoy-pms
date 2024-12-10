package com.sunjoy.parkctrl.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/12/6
 */
@Slf4j
@Service
public class WebSocketService {
    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendMessage(String topic, String message) {
        // 发送消息到/topic/messages主题
        messagingTemplate.convertAndSend(topic, message);
        log.info("向主题：{}, 发送了消息 :{}", topic, message);
    }
}