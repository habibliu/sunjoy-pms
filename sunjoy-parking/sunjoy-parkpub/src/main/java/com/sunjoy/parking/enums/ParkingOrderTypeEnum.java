package com.sunjoy.parking.enums;

/**
 * 订单类型
 *
 * @author Habib
 * @date 2024/11/25
 */
public enum ParkingOrderTypeEnum {
    UnregistedVechicle("1", "临停车"),
    Registed("2", "月租车");
    private String code;
    private String desc;

    private ParkingOrderTypeEnum(String code, String desc) {
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