package com.sunjoy.system.domain;

import lombok.Data;

/**
 * 中国地区实体类
 *
 * @author Habib
 * @date 2024/10/30
 */
@Data
public class SysRegion {

    /**
     * 自增主键
     */
    private Integer id;

    /**
     * 区域id
     */
    private String regionId;

    /**
     * 父区域id
     */
    private String parentId;

    /**
     * 祖级列表
     */
    private String ancestors;

    /**
     * 区域名称
     */
    private String regionName;

    /**
     * 状态（0正常 1停用）
     */
    private String status;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

}