package com.sunjoy.common.core.enums;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/8
 */
public enum YesNoEnum {
    Yes("Y", "是"),
    No("N", "否");

    private String code;
    private String desc;

    private YesNoEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }
}