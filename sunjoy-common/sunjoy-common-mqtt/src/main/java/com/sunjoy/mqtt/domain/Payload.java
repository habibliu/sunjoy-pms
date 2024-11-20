package com.sunjoy.mqtt.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/4
 */
@Data
@NoArgsConstructor
public class Payload implements Serializable {
    /**
     * 唯一识别编号
     */

    private String uuid;

    /**
     * 时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventTime;

    /**
     * 其他传递信息
     */
    private Map<String, Object> data = new HashMap<>();
    /**
     * 版本号
     */
    private String version = "1.0";

    @JsonCreator
    public Payload(
            @JsonProperty("uuid") String uuid,
            @JsonProperty("eventTime") LocalDateTime eventTime,
            @JsonProperty("data") Map<String, Object> data,
            @JsonProperty("version") String version) {
        this.uuid = uuid;
        this.data = data;
        this.version = version;

        this.eventTime = eventTime;
        if (eventTime == null) {
            this.eventTime = LocalDateTime.now();
        }
    }
}