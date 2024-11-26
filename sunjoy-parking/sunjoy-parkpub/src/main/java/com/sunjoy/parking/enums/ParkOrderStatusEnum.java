package com.sunjoy.parking.enums;

/**
 * 订单状态
 * 订单状态
 *
 * @author Habib
 * @date 2024/11/25
 */
public enum ParkOrderStatusEnum {
    Unpaid("0", "未支付"),
    Processing("1", "支付中"),
    Paid("2", "已支付");
    private String code;
    private String desc;

    private ParkOrderStatusEnum(String code, String desc) {
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