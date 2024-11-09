package com.sunjoy.system.service.impl;

import com.sunjoy.common.redis.service.RedisService;
import com.sunjoy.common.security.utils.SecurityUtils;
import com.sunjoy.system.api.domain.SysDept;
import com.sunjoy.system.api.domain.SysRole;
import com.sunjoy.system.api.domain.SysUser;
import com.sunjoy.system.domain.SysTenant;
import com.sunjoy.system.domain.SysTenantMenu;
import com.sunjoy.system.mapper.SysTenantMapper;
import com.sunjoy.system.mapper.SysTenantMenuMapper;
import com.sunjoy.system.service.ISysDeptService;
import com.sunjoy.system.service.ISysRoleService;
import com.sunjoy.system.service.ISysTenantService;
import com.sunjoy.system.service.ISysUserService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
    @Autowired
    private SysTenantMenuMapper sysTenantMenuMapper;
    @Autowired
    private ISysRoleService sysRoleService;
    @Autowired
    private ISysDeptService sysDeptService;
    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private RedisService redisService;

    @PostConstruct
    private void initCache() {
        SysTenant condition = new SysTenant();
        condition.setStatus("1");//生效的租户
        List<SysTenant> allTenants = sysTenantMapper.selectByConditions(condition);
        if (allTenants != null && !allTenants.isEmpty()) {
            Map<String, SysTenant> cacheMap = new HashMap<String, SysTenant>();
            allTenants.forEach(tenant -> {
                cacheMap.put(tenant.getTenantId().toString(), tenant);
            });
            redisService.setCacheMap(ISysTenantService.KEY_REDIS_TENANT, cacheMap);
        }
    }

    @Override
    public Long addTenant(SysTenant tenant) {
        tenant.setCreateBy(SecurityUtils.getUsername());
        tenant.setCreateTime(new Date());
        sysTenantMapper.insert(tenant);
        return tenant.getTenantId();
    }

    @Override
    public SysTenant getTenantById(Long tenantId) {
        return sysTenantMapper.selectById(tenantId);
    }

    @Override
    public List<SysTenant> listTenants(SysTenant tenant) {
        if (tenant.getRegion() != null) {
            String result = tenant.getRegion().replaceAll("0+$", "");
            tenant.setRegion(result);
        }
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

    /**
     * 审批租户：
     * １、更改状态为１
     * 2、生成当前租户的一份基本菜单，即购买的服务
     * 3、生成一条该租户的组只架构
     * 4、生成该租户的admin角色,并分配全部业务功能
     * 5、生成一个该租户的admin用户，默认密码
     * 6、给该角色分配全部业务功能
     *
     * @param tenant
     * @param menuList 菜单id列表
     */
    @Override
    @Transactional
    public void auditTenant(SysTenant tenant, List<Long> menuList) {
        //step 1, 更新并更改状态为已生效
        //判断是否有重复的tenant_code
        justify(tenant);
        tenant.setStatus("1");//状态为已生效
        tenant.setDelFlag("0");
        tenant.setUpdateBy(SecurityUtils.getUsername());
        tenant.setUpdateTime(new Date());
        this.sysTenantMapper.update(tenant);
        //将对象放进缓存中
        redisService.setCacheMapValue(ISysTenantService.KEY_REDIS_TENANT, tenant.getTenantId().toString(), tenant);

        //step 生成当前租户的一份基本菜单，即购买的服务
        batchSaveTenantMenu(tenant.getTenantId(), menuList);
        //step 3 生成一条该租户的组只架构
        SysDept org = createTenantOrganization(tenant);
        //step 4,生成该租户的admin角色,包括角色－组织，角色－菜单关系
        SysRole role = createTenantAdminRole(tenant, org, menuList);
        //
        createAdminUserFormTenant(tenant, org, role);
    }

    private void justify(SysTenant tenant) {

    }

    private void batchSaveTenantMenu(Long tenantId, List<Long> menuList) {
        List<SysTenantMenu> tenanMenuList = new ArrayList<SysTenantMenu>();
        menuList.forEach(menuId -> {
            SysTenantMenu tenantMenu = new SysTenantMenu();
            tenantMenu.setTenantId(tenantId);
            tenantMenu.setMenuId(menuId);
            tenanMenuList.add(tenantMenu);
        });
        sysTenantMenuMapper.batchInsert(tenanMenuList);
    }

    private SysDept createTenantOrganization(SysTenant tenant) {
        SysDept sysDept = new SysDept();
        sysDept.setTenantId(tenant.getTenantId());
        sysDept.setDeptName(tenant.getTenantName());
        sysDept.setParentId(0L);
        sysDept.setAncestors("0");
        sysDept.setDelFlag("0");
        sysDept.setLeader(tenant.getLeader());
        sysDept.setPhone(tenant.getPhone());
        sysDept.setEmail(tenant.getEmail());
        sysDept.setOrderNum(0);
        sysDept.setStatus("0");
        sysDept.setCreateBy(SecurityUtils.getUsername());
        sysDept.setCreateTime(new Date());
        this.sysDeptService.insertDept(sysDept);
        return sysDept;
    }

    private SysRole createTenantAdminRole(SysTenant tenant, SysDept org, List<Long> menuList) {
        SysRole sysRole = new SysRole();
        sysRole.setTenantId(tenant.getTenantId());
        sysRole.setRoleName("系统管理员");
        sysRole.setDataScope("1");//本租户下所有数据
        sysRole.setRoleKey("common");
        sysRole.setCreateBy(SecurityUtils.getUsername());
        sysRole.setCreateTime(new Date());
        sysRole.setDelFlag("0");
        sysRole.setStatus("0");
        sysRole.setDeptCheckStrictly(true);
        sysRole.setMenuCheckStrictly(true);
        sysRole.setRoleSort(0);
        sysRole.setRemark("系统管理员");
        sysRole.setMenuIds(menuList.toArray(new Long[0]));//用于创建角色－菜单关系
        this.sysRoleService.insertRole(sysRole);
        //创建角色－组织关系
        Long[] array = {org.getDeptId()};
        sysRole.setDeptIds(array);
        this.sysRoleService.authDataScope(sysRole);
        return sysRole;
    }


    private void createAdminUserFormTenant(SysTenant tenant, SysDept org, SysRole sysRole) {
        SysUser user = new SysUser();
        user.setTenantId(tenant.getTenantId());
        user.setNickName("admin");
        user.setUserName("admin");
        user.setPassword("123456");
        user.setDeptId(org.getDeptId());
        user.setDelFlag("0");
        user.setCreateBy(SecurityUtils.getUsername());
        user.setCreateTime(new Date());
        user.setStatus("0");
        //设置角色
        Long[] array = {sysRole.getRoleId()};
        user.setRoleIds(array);
        user.setRemark("系统管理员");
        sysUserService.insertUser(user);
    }
}