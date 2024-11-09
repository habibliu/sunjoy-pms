package com.sunjoy.system.mapper.provider;

import com.sunjoy.system.domain.SysTenant;
import org.apache.ibatis.jdbc.SQL;

/**
 * SysTenant动态查询语句提供类
 *
 * @author Habib
 * @date 2024/10/30
 */
public class SysTenantSqlProvider {
    public String dynamicSelect(SysTenant tenant) {
        return new SQL() {{
            SELECT("*");
            FROM("sys_tenant");

            if (tenant.getTenantId() != null) {
                WHERE("tenant_id = #{tenantId}");
            }
            if (tenant.getRegion() != null) {
                WHERE("region LIKE CONCAT(#{region}, '%')");
            }
            if (tenant.getTenantName() != null && !tenant.getTenantName().isEmpty()) {
                WHERE("tenant_name LIKE CONCAT('%', #{tenantName}, '%')");
            }
            if (tenant.getTenantCode() != null && !tenant.getTenantCode().isEmpty()) {
                WHERE("tenant_code = #{tenantCode}");
            }
            if (tenant.getStatus() != null) {
                WHERE("status = #{status}");
            }
            if (tenant.getDelFlag() != null) {
                WHERE("del_flag = #{delFlag}");
            }
            // 可以根据需要添加更多条件
        }}.toString();
    }
}