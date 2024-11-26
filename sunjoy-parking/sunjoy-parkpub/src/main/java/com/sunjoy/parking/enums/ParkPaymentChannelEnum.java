package com.sunjoy.parking.enums;

import lombok.Getter;

/**
 * 支付通道
 *
 * @author Habib
 * @date 2024/11/25
 */
@Getter
public enum ParkPaymentChannelEnum {
    WECHAT("WECHAT", "微信"),
    ALIPAY("ALIPAY", "支付宝");
    private final String code;
    private final String desc;

    private ParkPaymentChannelEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}