package com.sunjoy.parkmodel.mapper.provider;

import com.sunjoy.common.security.utils.SecurityUtils;
import com.sunjoy.parking.entity.PmsParkPrice;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/6
 */
public class PmsParkPriceSqlProvider {
    public String selectByCondition(final PmsParkPrice condition) {
        return new SQL() {{
            SELECT("l.*");
            FROM("pms_park_price l");

            if (condition.getPriceName() != null && !condition.getPriceName().isEmpty()) {
                WHERE(".price_name = #{priceName}");
            }
            if (condition.getTenantId() != null) {
                WHERE("l.tenant_id = #{tenantId}");
            }
            if (condition.getOpuId() != null) {
                List<Long> opuIds = SecurityUtils.getAuthDeptIds(SecurityUtils.getUserId(), condition.getOpuId());
                String inClause = opuIds.stream()
                        .map(String::valueOf) // 将 Long 转换为 String
                        .collect(Collectors.joining(", ")); // 连接成字符串
                WHERE("l.opu_id IN (" + inClause + ")");
            }
            if (condition.getUniformPrice() != null) {
                WHERE("l.uniform_price = #{uniformPrice}");
            }
            if (condition.getStatus() != null) {
                WHERE("l.status = #{status}");
            }
            if (condition.getFree() != null) {
                WHERE("l.free = #{free}");
            }


        }}.toString();
    }
}