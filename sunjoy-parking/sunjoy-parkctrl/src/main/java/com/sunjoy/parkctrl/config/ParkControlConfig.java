package com.sunjoy.parkctrl.config;

import com.sunjoy.mqtt.service.MqttService;
import com.sunjoy.parkctrl.listener.CarCapturedDevcieListener;
import com.sunjoy.parkctrl.listener.CarEntryParkListenter;
import com.sunjoy.parkctrl.listener.CarExitParkListener;
import com.sunjoy.parking.utils.MqttTopics;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * 消息消费者统一在此处注册
 *
 * @author Habib
 * @date 2024/11/4
 */
@Configuration
@Data
@Slf4j
public class ParkControlConfig {

    @Autowired
    private MqttService mqttService;

    @Autowired
    private CarCapturedDevcieListener carCapturedDevcieListener;
    @Autowired
    private CarEntryParkListenter carEntryParkListenter;
    @Autowired
    private CarExitParkListener carExitParkListener;


    @PostConstruct
    protected void initSubscribe() {
        try {
            mqttService.subscribe(MqttTopics.TOPIC_CAR_CAPTURED, carCapturedDevcieListener);

            mqttService.subscribe(MqttTopics.TOPIC_CAR_ARRIVED, carEntryParkListenter);

            mqttService.subscribe(MqttTopics.TOPIC_CAR_DEPARTED, carExitParkListener);
        } catch (Exception e) {

            log.error("订阅主题失败!", e.getMessage());
            e.printStackTrace();
        }

    }
}