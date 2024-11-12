package com.sunjoy.parking.enums;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/11
 */
public enum EntityStatusEnum {
    NEW("0", "未启用"),
    ENABLED("1", "已启用"),
    DISABLED("9", "已注销");
    private String status;
    private String desc;

    private EntityStatusEnum(String status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public String getStatus() {
        return status;
    }

    public String getDescription() {
        return desc;
    }
}