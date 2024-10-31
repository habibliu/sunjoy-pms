package com.sunjoy.system.service.impl;

import com.sunjoy.system.domain.SysTenant;
import com.sunjoy.system.mapper.SysTenantMapper;
import com.sunjoy.system.service.ISysTenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 租户服务类
 *
 * @author Habib
 * @date 2024/10/30
 */
@Service
public class SysTenantServiceImpl implements ISysTenantService {

    @Autowired
    private SysTenantMapper sysTenantMapper;

    @Override
    public Long addTenant(SysTenant tenant) {
        sysTenantMapper.insert(tenant);
        return tenant.getTenantId();
    }

    @Override
    public SysTenant getTenantById(Long tenantId) {
        return sysTenantMapper.selectById(tenantId);
    }

    @Override
    public List<SysTenant> listTenants(SysTenant tenant) {
        return sysTenantMapper.selectByConditions(tenant);
    }

    @Override
    public void updateTenant(SysTenant tenant) {
        sysTenantMapper.update(tenant);
    }

    @Override
    public void deleteTenant(Long tenantId) {
        sysTenantMapper.delete(tenantId);
    }
}