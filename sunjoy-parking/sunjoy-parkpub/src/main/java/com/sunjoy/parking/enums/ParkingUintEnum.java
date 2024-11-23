package com.sunjoy.parking.enums;

/**
 * 车场计费单位枚举
 *
 * @author Habib
 * @date 2024/11/22
 */
public enum ParkingUintEnum {
    TIMES("TIMES", "次"),
    MINUTE("MIN", "分钟"),
    HOUR("HOUR", "小时"),
    DAY("DAY", "天"),
    WEEK("WEEK", "周"),
    MONTH("MONTH", "月");
    private String code;
    private String desc;

    private ParkingUintEnum(String code, String desc) {
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