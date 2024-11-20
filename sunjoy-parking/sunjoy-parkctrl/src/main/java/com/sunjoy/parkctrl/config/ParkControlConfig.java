package com.sunjoy.parkctrl.config;

import com.sunjoy.mqtt.config.SunjoyMqttClient;
import com.sunjoy.parkctrl.listener.CarCapturedDevcieListener;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

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
    private CarCapturedDevcieListener carCapturedDevcieListener;

    @Autowired
    private SunjoyMqttClient parkClientMqttClient;
    

    @EventListener(ApplicationReadyEvent.class)
    protected void initSubscribe() {
        try {
            /**
             * 订阅车场设备消息
             */
            parkClientMqttClient.addSubscriber(carCapturedDevcieListener);


        } catch (Exception e) {

            log.error("订阅主题失败!", e.getMessage());
            e.printStackTrace();
        }

    }
}