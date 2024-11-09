package com.sunjoy.parkmodel.mapper.provider;

import com.sunjoy.common.security.utils.SecurityUtils;
import com.sunjoy.parking.entity.PmsPark;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/8
 */
public class PmsParkSqlProvider {
    public String selectByCondition(final PmsPark condition) {
        return new SQL() {{
            SELECT("*");
            FROM("pms_park ");

            if (condition.getParkName() != null && !condition.getParkName().isEmpty()) {
                WHERE(".park_name = #{parkName}");
            }
            if (condition.getTenantId() != null) {
                WHERE("tenant_id = #{tenantId}");
            }
            if (condition.getOpuId() != null) {
                List<Long> opuIds = SecurityUtils.getAuthDeptIds(SecurityUtils.getUserId(), condition.getOpuId());
                String inClause = opuIds.stream()
                        .map(String::valueOf) // 将 Long 转换为 String
                        .collect(Collectors.joining(", ")); // 连接成字符串
                WHERE("opu_id IN (" + inClause + ")");
            }
            if (condition.getParkType() != null) {
                WHERE("park_type = #{parkType}");
            }
            if (condition.getStatus() != null) {
                WHERE("status = #{status}");
            }
            if (condition.getLeader() != null) {
                WHERE("leader = #{leader}");
            }
            if (condition.getPhone() != null) {
                WHERE("phone = #{phone}");
            }

        }}.toString();
    }
}