package com.sunjoy.park.client.entity.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunjoy.mqtt.domain.MqttBrokerConfig;
import jakarta.persistence.AttributeConverter;

import java.io.IOException;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/29
 */
public class MqttBrokerConverter implements AttributeConverter<MqttBrokerConfig, String> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(MqttBrokerConfig deviceDetail) {
        if (deviceDetail == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(deviceDetail);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error converting DeviceDetail to JSON", e);
        }
    }

    @Override
    public MqttBrokerConfig convertToEntityAttribute(String json) {
        if (json == null) {
            return null;
        }
        try {
            return objectMapper.readValue(json, MqttBrokerConfig.class);
        } catch (IOException e) {
            throw new IllegalArgumentException("Error converting JSON to DeviceDetail", e);
        }
    }
}