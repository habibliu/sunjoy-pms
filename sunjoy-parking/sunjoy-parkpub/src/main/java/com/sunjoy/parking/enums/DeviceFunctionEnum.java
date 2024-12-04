package com.sunjoy.parking.enums;

import lombok.Getter;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/27
 */
@Getter
public enum DeviceFunctionEnum {
    REL("REL", "放行"),
    DIS("DIS", "显示"),
    VOI("VOI", "语音"),
    SEN("SEN", "感应");

    private final String code;
    private final String desc;

    private DeviceFunctionEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}