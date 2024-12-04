package com.sunjoy.park.client.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunjoy.common.core.utils.uuid.UUID;
import com.sunjoy.common.core.web.controller.BaseController;
import com.sunjoy.common.core.web.domain.AjaxResult;
import com.sunjoy.mqtt.config.SunjoyMqttClient;
import com.sunjoy.mqtt.domain.VehicleArrivedPayload;
import com.sunjoy.park.client.entity.PlatformInfo;
import com.sunjoy.park.client.service.SystemSettingService;
import com.sunjoy.park.client.service.TextToSpeechService;
import com.sunjoy.parking.utils.MqttTopics;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    private TextToSpeechService textToSpeechService;
    @Autowired
    private SystemSettingService systemSettingService;

    /**
     * 模拟车辆被识别
     *
     * @param payload
     * @return
     * @throws MqttException
     * @throws JsonProcessingException
     */
    @PostMapping(value = "/catch", consumes = "application/json")
    public AjaxResult add(@Validated @RequestBody VehicleArrivedPayload payload) throws MqttException, JsonProcessingException {
        payload.setUuid(UUID.fastUUID().toString().replace("-", ""));
        String jsonPayload = objectMapper.writeValueAsString(payload);
        mqttConnectionPool.publish(MqttTopics.TOPIC_CAR_CAPTURED, jsonPayload);
        // 创建消息

        return toAjax(1);
    }

    @GetMapping("/tts")
    public ResponseEntity<String> synthesize(@RequestParam String text) {
        textToSpeechService.localSpeech(text);
        //byte[] audio = textToSpeechService.synthesizeSpeech(text);
        // 根据需要设置音频类型
        return ResponseEntity.ok()
                .header("Content-Type", "audio/wav")
                .body("OK");
    }

    @PostMapping(value = "/setting/save", consumes = "application/json")
    public AjaxResult saveSystemSetting(@Validated @RequestBody PlatformInfo platformInfo) {
        systemSettingService.saveSystemSetting(platformInfo);
        return toAjax(1);
    }

    @GetMapping(value = "/setting/get")
    public ResponseEntity<PlatformInfo> getSystemSetting() {
        PlatformInfo platformInfo = systemSettingService.getSystemSetting();
        PlatformInfo newPlatformInfo = null;
        if (platformInfo != null) {
            newPlatformInfo = new PlatformInfo();
            newPlatformInfo.setId(platformInfo.getId());
            newPlatformInfo.setPlatformUrl(platformInfo.getPlatformUrl());
            newPlatformInfo.setUserName(platformInfo.getUserName());
            newPlatformInfo.setPassword(platformInfo.getPassword());
            return ResponseEntity.ok().body(newPlatformInfo);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(null); // 404 Not Found
    }

}