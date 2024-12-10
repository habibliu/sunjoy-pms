package com.sunjoy.parkmodel.mapper.provider;

import com.sunjoy.common.security.utils.SecurityUtils;
import com.sunjoy.parking.entity.PmsParkRule;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/14
 */
public class PmsParkRuleSqlProvider {
    /**
     * 动态生成更新 SQL
     *
     * @param parkRule 车场通行规则对象
     * @return 更新 SQL
     */
    public String update(PmsParkRule parkRule) {
        return new SQL() {{
            UPDATE("pms_park_rule");
            if (parkRule.getRuleName() != null) SET("rule_name = #{ruleName}");
            if (parkRule.getTenantId() != null) SET("tenant_id = #{tenantId}");
            if (parkRule.getOpuId() != null) SET("opu_id = #{opuId}");
            if (parkRule.getParkId() != null) SET("park_id = #{parkId}");
            if (parkRule.getLevel() != null) SET("level = #{level}");
            if (parkRule.getTarget() != null) SET("target = #{target}");
            if (parkRule.getDayRange() != null) SET("day_range = #{dayRange}");
            if (parkRule.getTimeRange() != null) SET("time_range = #{timeRange}");
            if (parkRule.getAllowPass() != null) SET("allow_pass = #{allowPass}");
            if (parkRule.getDirection() != null) SET("direction = #{direction}");
            if (parkRule.getParkFull() != null) SET("park_full = #{parkFull}");
            if (parkRule.getTargetExpire() != null) SET("target_expire = #{targetExpire}");
            if (parkRule.getNotifyMessage() != null) SET("notify_message = #{notifyMessage}");
            if (parkRule.getNotifyMethods() != null) SET("notify_methods = #{notifyMethods}");
            if (parkRule.getDetailParams() != null) SET("detail_params = #{detailParams}");
            if (parkRule.getStatus() != null) SET("status = #{status}");
            if (parkRule.getDelFlag() != null) SET("del_flag = #{delFlag}");
            if (parkRule.getUpdateBy() != null) SET("update_by = #{updateBy}");
            if (parkRule.getUpdateTime() != null) SET("update_time = #{updateTime}");
            if (parkRule.getRemark() != null) SET("remark = #{remark}");
            WHERE("rule_id = #{ruleId}");
        }}.toString();
    }

    /**
     * 动态生成条件查询 SQL
     *
     * @param parkRule 车场通行规则对象，包含查询条件
     * @return 查询 SQL
     */
    public String selectAll(PmsParkRule parkRule) {
        return new SQL() {{
            SELECT("r.*,p.park_name");
            FROM("pms_park_rule r");
            LEFT_OUTER_JOIN("pms_park p on p.park_id=r.park_id");
            if (parkRule.getRuleName() != null) WHERE("r.rule_name = #{ruleName}");
            if (parkRule.getTenantId() != null) WHERE("r.tenant_id = #{tenantId}");
            if (parkRule.getOpuId() != null) {
                List<Long> opuIds = SecurityUtils.getAuthDeptIds(SecurityUtils.getUserId(), parkRule.getOpuId());
                String inClause = opuIds.stream()
                        .map(String::valueOf) // 将 Long 转换为 String
                        .collect(Collectors.joining(", ")); // 连接成字符串
                WHERE("r.opu_id IN (" + inClause + ")");

            }
            if (parkRule.getParkId() != null) WHERE("r.park_id = #{parkId}");
            if (parkRule.getLevel() != null) WHERE("r.level = #{level}");
            if (parkRule.getTarget() != null) WHERE("r.target = #{target}");
            if (parkRule.getDayRange() != null) WHERE("r.day_range = #{dayRange}");
            if (parkRule.getTimeRange() != null) WHERE("r.time_range = #{timeRange}");
            if (parkRule.getAllowPass() != null) WHERE("r.allow_pass = #{allowPass}");
            if (parkRule.getDirection() != null) WHERE("r.direction = #{direction}");
            if (parkRule.getParkFull() != null) WHERE("r.park_full = #{parkFull}");
            if (parkRule.getTargetExpire() != null) WHERE("r.target_expire = #{targetExpire}");
            if (parkRule.getStatus() != null) WHERE("r.status = #{status}");
            if (parkRule.getDelFlag() != null) WHERE("r.del_flag = #{delFlag}");
            ORDER_BY("r.rule_id DESC");
        }}.toString();
    }
}