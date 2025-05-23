package com.sunjoy.parkmodel.mapper.provider;


import com.sunjoy.common.security.utils.SecurityUtils;
import com.sunjoy.parking.entity.PmsLane;
import com.sunjoy.parkmodel.pojo.LanePojo;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 车道sql提供类
 *
 * @author Habib
 * @date 2024/10/25
 */
public class PmsLaneSqlProvider {
    public String selectPmsLanesByCondition(final LanePojo condition) {
        return new SQL() {{
            SELECT("l.*");
            FROM("pms_lane l");
            INNER_JOIN("pms_park_lane p on l.lane_id = p.lane_id");
            if (condition.getLaneName() != null && !condition.getLaneName().isEmpty()) {
                WHERE(".lane_name = #{laneName}");
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
            if (condition.getLinkOuter() != null) {
                WHERE("l.link_outer = #{linkOuter}");
            }
            if (condition.getStatus() != null) {
                WHERE("l.status = #{status}");
            }
            if (condition.getParkId() != null) {
                WHERE("p.park_id = #{parkId}");
            }
            ORDER_BY("l.lane_id DESC");

        }}.toString();
    }

    public String updatePmsLane(final PmsLane pmsLane) {
        return new SQL() {{
            UPDATE("pms_lane");
            if (pmsLane.getLaneName() != null && !pmsLane.getLaneName().isEmpty()) {
                SET("lane_name = #{laneName}");
            }
            if (pmsLane.getOpuId() != null) {
                SET("opu_id = #{opuId}");
            }
            if (pmsLane.getRap() != null && !pmsLane.getRap().isEmpty()) {
                SET("rap = #{rap}");
            }
            if (pmsLane.getLinkOuter() != null && !pmsLane.getLinkOuter().isEmpty()) {
                SET("link_outer = #{linkOuter}");
            }
            if (pmsLane.getStatus() != null && !pmsLane.getStatus().isEmpty()) {
                SET("status = #{status}");
            }

            if (pmsLane.getUpdateBy() != null && !pmsLane.getUpdateBy().isEmpty()) {
                SET("update_by = #{updateBy}");
            }
            if (pmsLane.getUpdateTime() != null) {
                SET("update_time = #{updateTime}");
            }
            if (pmsLane.getRemark() != null && !pmsLane.getRemark().isEmpty()) {
                SET("remark = #{remark}");
            }
            WHERE("lane_id = #{laneId}");
        }}.toString();
    }
}