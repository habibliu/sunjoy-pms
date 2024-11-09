package com.sunjoy.system.service;

import com.sunjoy.system.domain.SysTenant;

import java.util.List;

/**
 * 租户服务接口
 *
 * @author Habib
 * @date 2024/10/30
 */
public interface ISysTenantService {

    final String KEY_REDIS_TENANT = "sys:tennant:map";

    Long addTenant(SysTenant tenant);

    SysTenant getTenantById(Long tenantId);

    List<SysTenant> listTenants(SysTenant tenant);

    void updateTenant(SysTenant tenant);

    void deleteTenant(Long tenantId);

    /**
     * 审批租户：
     * １、更改状态为１
     * ２、生成一条该租户的组只架构
     * ３、生成一个该租户的admin用户，默认密码
     * 4、生成该租户的admin角色
     * 5、给该角色分配全部业务功能
     *
     * @param tenant
     * @param menuList 菜单id列表
     */
    void auditTenant(SysTenant tenant, List<Long> menuList);
}