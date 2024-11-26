package com.sunjoy.parking.enums;

import lombok.Getter;

/**
 * 支付方式
 *
 * @author Habib
 * @date 2024/11/25
 */
@Getter
public enum ParkPaymentMethods {
    CASH("0", "现金"),
    CHECK("1", "支票"),
    BANK("2", "银联"),
    ONLINE("3", "在线");
    private final String code;
    private final String desc;

    private ParkPaymentMethods(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}