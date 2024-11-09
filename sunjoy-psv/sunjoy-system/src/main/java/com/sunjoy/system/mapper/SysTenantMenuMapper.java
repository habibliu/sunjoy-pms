package com.sunjoy.system.mapper;


import com.sunjoy.system.domain.SysTenantMenu;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/1
 */
@Mapper
public interface SysTenantMenuMapper {

    /**
     * 插入租户菜单关联
     *
     * @param sysTenantMenu 租户菜单关联对象
     * @return 插入的行数
     */
    int insert(SysTenantMenu sysTenantMenu);

    /**
     * 批量插入租户菜单关联
     *
     * @param sysTenantMenus 租户菜单关联对象列表
     * @return 插入的行数
     */
    @Insert({
            "<script>",
            "INSERT INTO sys_tenant_menu (tenant_id, menu_id) VALUES ",
            "<foreach collection='sysTenantMenus' item='item' separator=','>",
            "(#{item.tenantId}, #{item.menuId})",
            "</foreach>",
            "</script>"
    })
    int batchInsert(@Param("sysTenantMenus") List<SysTenantMenu> sysTenantMenus);
}