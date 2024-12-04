package com.sunjoy.park.client.entity;

import com.sunjoy.mqtt.domain.MqttBrokerConfig;
import com.sunjoy.park.client.entity.converter.MqttBrokerConverter;
import jakarta.persistence.*;
import lombok.Data;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/29
 */
@Data
@Entity
@Table(name = "sys_platform")
public class PlatformInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String platformUrl;

    private String userName;

    private String password;


    @Convert(converter = MqttBrokerConverter.class)
    @Column(name = "mqtt_broker")
    private MqttBrokerConfig mqttBrokerConfig;


}