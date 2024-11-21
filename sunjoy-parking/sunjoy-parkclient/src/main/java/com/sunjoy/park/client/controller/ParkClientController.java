package com.sunjoy.park.client.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunjoy.common.core.utils.uuid.UUID;
import com.sunjoy.common.core.web.controller.BaseController;
import com.sunjoy.common.core.web.domain.AjaxResult;
import com.sunjoy.mqtt.config.SunjoyMqttClient;
import com.sunjoy.mqtt.domain.VehicleArrivedPayload;
import com.sunjoy.parking.utils.MqttTopics;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/19
 */
@RestController
@RequestMapping("/client")
public class ParkClientController extends BaseController {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private SunjoyMqttClient mqttConnectionPool;

    @PostMapping(value = "/catch", consumes = "application/json")
    public AjaxResult add(@Validated @RequestBody VehicleArrivedPayload payload) throws MqttException, JsonProcessingException {
        payload.setUuid(UUID.fastUUID().toString().replace("-", ""));
        String jsonPayload = objectMapper.writeValueAsString(payload);
        mqttConnectionPool.publish(MqttTopics.TOPIC_CAR_CAPTURED, jsonPayload);
        // 创建消息

        return toAjax(1);
    }
}