package com.sunjoy.parkmodel.mapper.provider;

import com.sunjoy.common.security.utils.SecurityUtils;
import com.sunjoy.parking.entity.PmsParkService;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.stream.Collectors;

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
                List<Long> opuIds = SecurityUtils.getAuthDeptIds(SecurityUtils.getUserId(), conditions.getOpuId());
                String inClause = opuIds.stream()
                        .map(String::valueOf) // 将 Long 转换为 String
                        .collect(Collectors.joining(", ")); // 连接成字符串
                WHERE("s.opu_id IN (" + inClause + ")");
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

    public String updatePmsParkService(PmsParkService parkService) {
        return new SQL() {{
            UPDATE("pms_park_service");

            if (parkService.getTenantId() != null) {
                SET("tenant_id = #{tenantId}");
            }
            if (parkService.getOpuId() != null) {
                SET("opu_id = #{opuId}");
            }
            if (parkService.getParkId() != null) {
                SET("park_id = #{parkId}");
            }
            if (parkService.getPriceId() != null) {
                SET("price_id = #{priceId}");
            }
            if (parkService.getStartDate() != null) {
                SET("start_date = #{startDate}");
            }
            if (parkService.getEndDate() != null) {
                SET("end_date = #{endDate}");
            }
            if (parkService.getExpiredAllowed() != null) {
                SET("expired_allowed = #{expiredAllowed}");
            }
            if (parkService.getExpiredDuration() != null) {
                SET("expired_duration = #{expiredDuration}");
            }
            if (parkService.getEntryMessage() != null) {
                SET("entry_message = #{entryMessage}");
            }
            if (parkService.getExitMessage() != null) {
                SET("exit_message = #{exitMessage}");
            }
            if (parkService.getStatus() != null) {
                SET("status = #{status}");
            }
            if (parkService.getDelFlag() != null) {
                SET("del_flag = #{delFlag}");
            }

            WHERE("service_id = #{serviceId}");
        }}.toString();
    }
}