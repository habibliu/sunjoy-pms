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

    Long addTenant(SysTenant tenant);

    SysTenant getTenantById(Long tenantId);

    List<SysTenant> listTenants(SysTenant tenant);

    void updateTenant(SysTenant tenant);

    void deleteTenant(Long tenantId);
}