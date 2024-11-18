package com.sunjoy.parking.enums;

/**
 * 车场通行规则应用级别
 *
 * @author Habib
 * @date 2024/11/18
 */
public enum RuleApplyLevelEnum {
    PARK("0", "车场"),
    LANE("1", "通道");
    private String value;
    private String desc;

    private RuleApplyLevelEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}