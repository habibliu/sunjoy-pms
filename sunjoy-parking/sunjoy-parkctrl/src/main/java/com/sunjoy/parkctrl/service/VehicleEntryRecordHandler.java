package com.sunjoy.parkctrl.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunjoy.mqtt.service.BaseMqttMessageListener;
import com.sunjoy.parking.vo.VehiclePassage;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Service;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/18
 */
@Slf4j
@Service
public class VehicleEntryRecordHandler implements BaseMqttMessageListener {
    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
        String message = new String(mqttMessage.getPayload());
        // 处理接收车辆入场的消息
        log.info("Received vehicle entry message: " + message);
        //转为车辆出入场对象，并安排对应的服务类处理
        ObjectMapper objectMapper = new ObjectMapper();
        VehiclePassage vehiclePassage = objectMapper.readValue(message, VehiclePassage.class);
        //todo 构造出入场流水对象
        try {
            //todo 调用mapper接口，完成入库
        } catch (Exception e) {
            log.error(e.getMessage());
            //todo 将对象放到缓存中，等待处理
        }
    }


}