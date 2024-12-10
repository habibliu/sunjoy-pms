package com.sunjoy.parking.enums;

import lombok.Getter;

/**
 * 车场服务订单类型
 *
 * @author Habib
 * @date 2024/12/9
 */
@Getter
public enum ParkOrderTypeEnum {
    Registed("1", "登记车"),
    UnRegisted("0", "临停车"),
    Other("9", "其他");
    private String code;
    private String desc;

    private ParkOrderTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}