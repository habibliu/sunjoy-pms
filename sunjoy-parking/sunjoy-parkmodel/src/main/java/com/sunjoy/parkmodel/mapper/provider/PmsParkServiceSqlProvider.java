package com.sunjoy.parkmodel.mapper.provider;

import com.sunjoy.parking.entity.PmsParkService;
import org.apache.ibatis.jdbc.SQL;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/8
 */
public class PmsParkServiceSqlProvider {
    public String buildSelectQuery(PmsParkService conditions) {
        return new SQL() {{
            SELECT("s.service_id,s.tenant_id,s.opu_id,s.price_id,s.park_id,s.expired_allowed,s.expired_duration,s.entry_message,s.exit_message,s.status,s.remark" +
                   ",p.price_name,p.free,p.free_duration,p.uniform_price,p.price,p.price_unit,p.price_quantity,p.max_fee,p.max_unit,p.max_quantity");
            FROM("pms_park_service s");
            INNER_JOIN("pms_park_price p on p.price_id=s.price_id and p.tenant_id=s.tenant_id");
            WHERE("s.del_flag = '0'"); // 默认过滤已删除记录

            if (conditions.getTenantId() != null) {
                WHERE("s.tenant_id = #{tenantId}");
            }
            if (conditions.getOpuId() != null) {
                WHERE("s.opu_id = #{opuId}");
            }
            if (conditions.getParkId() != null) {
                WHERE("s.park_id = #{parkId}");
            }
            if (conditions.getStatus() != null) {
                WHERE("s.status = #{status}");
            }

            // 可以根据需要添加更多条件
        }}.toString();
    }
}