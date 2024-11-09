package com.sunjoy.system.domain;

import lombok.Data;

/**
 * 租户和菜单关联表
 *
 * @author Habib
 * @date 2024/11/1
 */
@Data
public class SysTenantMenu {
    /**
     * 角色ID
     */
    private Long tenantId;

    /**
     * 菜单ID
     */
    private Long menuId;
}