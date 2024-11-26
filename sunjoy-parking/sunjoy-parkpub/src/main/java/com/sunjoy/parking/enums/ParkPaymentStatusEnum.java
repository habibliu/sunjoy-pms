package com.sunjoy.parking.enums;

/**
 * 支付凭证状态
 *
 * @author Habib
 * @date 2024/11/25
 */
public enum ParkPaymentStatusEnum {
    PENDING("0", "阻塞中"),
    COMPLETED("1", "已支付"),
    REFUNDED("2", "已退款"),
    FAILED("9", "失败");
    private String code;
    private String desc;

    private ParkPaymentStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}