package com.sunjoy.system.domain;

import com.sunjoy.common.core.web.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 租户表
 *
 * @author Habib
 * @date 2024/10/30
 */


@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysTenant extends BaseEntity {

    private Long tenantId;

    private String tenantName;

    private String tenantCode;

    private String leader;

    private String phone;

    private String email;

    private String region;

    private String address;

    private Date registrationDate;

    private String paymentMethod;

    private String settlementCycle;

    private Integer settlementDate;

    private String wechatAccount;

    private String alipayAccount;

    private String status;

    private String delFlag;

}