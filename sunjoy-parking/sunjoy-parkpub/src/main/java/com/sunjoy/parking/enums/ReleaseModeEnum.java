package com.sunjoy.parking.enums;

/**
 * 开闸方式
 *
 * @author Habib
 * @date 2024/11/20
 */
public enum ReleaseModeEnum {
    NORMAL("0", "正常开闸"),
    UNNORMAL("1", "非正常开闸");
    private String code;
    private String desc;

    ReleaseModeEnum(String code, String desc) {
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