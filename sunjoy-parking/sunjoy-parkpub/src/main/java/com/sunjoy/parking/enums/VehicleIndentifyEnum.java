package com.sunjoy.parking.enums;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/18
 */
public enum VehicleIndentifyEnum {
    ALL("0", "所有车"),
    UnRegisted("1", "临停车"),
    Registed("2", "登记车");
    private String code;
    private String desc;

    VehicleIndentifyEnum(String code, String desc) {
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