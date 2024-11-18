package com.sunjoy.parking.enums;

import lombok.Getter;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/4
 */
@Getter
public enum DirectionEnum {


    EXIT("0", "出场"),
    ENTRY("1", "入场"),
    BOTH("2", "入场");

    // 获取值
    private final String value;
    // 获取描述
    private final String description;

    // 构造函数
    DirectionEnum(String value, String description) {
        this.value = value;
        this.description = description;
    }

    // 根据值获取枚举类型
    public static DirectionEnum fromValue(String value) {
        for (DirectionEnum type : DirectionEnum.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("无效的值: " + value);
    }

}