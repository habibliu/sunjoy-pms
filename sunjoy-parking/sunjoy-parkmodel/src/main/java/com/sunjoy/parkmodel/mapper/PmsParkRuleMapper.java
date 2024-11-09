package com.sunjoy.parkmodel.mapper;

import com.sunjoy.parking.entity.PmsParkRule;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/5
 */

@Mapper
public interface PmsParkRuleMapper {
    /**
     * 插入新的通行规则
     */
    @Insert("INSERT INTO pms_park_rule (tenant_id, park_id, rule_code, rule_name, apply_level, " + "apply_targets, rule_params, notify_message, notify_methods, allow_pass, time_range," + "direction, status, del_flag, create_by, create_time, update_by, update_time, remark) " + "VALUES (#{tenantId}, #{parkId}, #{ruleCode}, #{ruleName}, #{applyLevel}, " + "#{applyTargets}, #{ruleParams}, #{notifyMessage}, #{notifyMethods}, #{allowPass},#{timeRange} " + "#{direction}, #{status}, #{delFlag}, #{createBy}, #{createTime}, #{updateBy}, #{updateTime}, #{remark})")
    void insertParkRule(PmsParkRule parkRule);

    /**
     * 根据规则ID查询通行规则
     */
    @Select("SELECT * FROM pms_park_rule WHERE rule_id = #{ruleId}")
    PmsParkRule selectParkRuleById(Long ruleId);

    /**
     * 更新通行规则
     */
    @Update("UPDATE pms_park_rule SET tenant_id = #{tenantId}, park_id = #{parkId}, rule_code = #{ruleCode}, " + "rule_name = #{ruleName}, apply_level = #{applyLevel}, apply_targets = #{applyTargets}, " + "rule_params = #{ruleParams}, notify_message = #{notifyMessage}, notify_methods = #{notifyMethods}, " + "allow_pass = #{allowPass},time_range = #{timeRange}, direction = #{direction}, status = #{status}, del_flag = #{delFlag}, " + "update_by = #{updateBy}, update_time = #{updateTime}, remark = #{remark} " + "WHERE rule_id = #{ruleId}")
    void updateParkRule(PmsParkRule parkRule);

    /**
     * 根据规则ID删除通行规则
     */
    @Delete("DELETE FROM pms_park_rule WHERE rule_id = #{ruleId}")
    void deleteParkRule(Long ruleId);

    /**
     * 查询所有通行规则
     */
    @Select("SELECT * FROM pms_park_rule")
    List<PmsParkRule> selectAllParkRules();

    /**
     * 根据ParkRule对象的属性条件查询通行规则
     */
    @Select("""
            <script>
            SELECT * FROM pms_park_rule 
            WHERE 1=1 
            <if test='rule.tenantId != null'> AND tenant_id = #{rule.tenantId} </if>
            <if test='rule.parkId != null'> AND park_id = #{rule.parkId} </if>
            <if test='rule.ruleCode != null and rule.ruleCode != ""'> AND rule_code = #{rule.ruleCode} </if>
            <if test='rule.ruleName != null and rule.ruleName != ""'> AND rule_name = #{rule.ruleName} </if>
            <if test='rule.applyLevel != null'> AND apply_level = #{rule.applyLevel} </if>
            <if test='rule.applyTargets != null and rule.applyTargets != ""'> AND apply_targets = #{rule.applyTargets} </if>
            <if test='rule.status != null'> AND status = #{rule.status} </if>
            </script>
            """)
    List<PmsParkRule> selectParkRulesByCriteria(@Param("rule") PmsParkRule parkRule);
}