package com.sunjoy.parking.vo;

import com.sunjoy.common.core.utils.uuid.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 计费时依赖的数据对象
 *
 * @author Habib
 * @date 2024/11/22
 */
@Data
@AllArgsConstructor
public class BillingDependency implements Serializable {

    /**
     * 计费ID,通常是uuid
     */
    private String billingId;

    private Long parkId;
    
    private String licensePlate;
    /**
     * 服务ID，也是收费标准ID
     */
    private Long serviceId;
    /**
     * 开始时间
     */
    private LocalDateTime startTime;
    /**
     * 计费结束时间
     */
    private LocalDateTime endTime;

    public BillingDependency() {
        this.billingId = UUID.fastUUID().toString().replace("-", "");
    }
}