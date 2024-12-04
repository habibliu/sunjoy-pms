package com.sunjoy.park.client.config;

import com.sunjoy.mqtt.config.SunjoyMqttClient;
import com.sunjoy.park.client.entity.LaneDevice;
import com.sunjoy.park.client.listener.CommandListener;
import com.sunjoy.park.client.service.LaneDeviceService;
import com.sunjoy.parking.utils.MqttTopics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.util.List;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/27
 */
@Slf4j
@Configuration
public class TopicSubscribeConfig {
    @Autowired
    private SunjoyMqttClient parkClientMqttClient;
    @Autowired
    private LaneDeviceService laneDeviceService;

    @EventListener(ApplicationReadyEvent.class)
    protected void initSubscribe() {
        try {
            //todo 获取本车场所有通道
            List<LaneDevice> allLanes = laneDeviceService.findAll();
            allLanes.forEach(item -> {
                CommandListener listener = new CommandListener(MqttTopics.TOPIC_LANE_DIRECTIVE + item.getLaneId());
                parkClientMqttClient.addSubscriber(listener);
            });
            /**
             * 订阅车场设备接收指令消息
             */


        } catch (Exception e) {

            log.error("订阅主题失败!", e.getMessage());
            e.printStackTrace();
        }

    }
}