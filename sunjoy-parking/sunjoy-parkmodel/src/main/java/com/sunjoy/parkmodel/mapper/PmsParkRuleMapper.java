package com.sunjoy.parkmodel.mapper;

import com.sunjoy.parking.entity.PmsParkRule;
import com.sunjoy.parkmodel.mapper.provider.PmsParkRuleSqlProvider;
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
     * 插入新规则
     *
     * @param parkRule 车场通行规则对象
     * @return 插入记录的行数
     */
    @Insert("INSERT INTO pms_park_rule(rule_name, tenant_id, opu_id, park_id, level, target, " +
            "day_range, time_range, allow_pass, direction, park_full, target_expire, " +
            "notify_message, notify_methods, detail_params, status, del_flag, create_by, create_time, " +
            "remark) " +
            "VALUES (#{ruleName}, #{tenantId}, #{opuId}, #{parkId}, #{level}, #{target}, " +
            "#{dayRange}, #{timeRange}, #{allowPass}, #{direction}, #{parkFull}, #{targetExpire}, " +
            "#{notifyMessage}, #{notifyMethods}, #{detailParams}, #{status}, #{delFlag}, #{createBy}, " +
            "#{createTime},#{remark})")
    @Options(useGeneratedKeys = true, keyProperty = "ruleId")
    int insert(PmsParkRule parkRule);

    /**
     * 根据规则ID查询规则
     *
     * @param ruleId 规则ID
     * @return 车场通行规则对象
     */
    @Select("SELECT * FROM pms_park_rule WHERE rule_id = #{ruleId}")
    PmsParkRule selectById(Long ruleId);

    /**
     * 更新规则，仅更新有值的属性
     *
     * @param parkRule 车场通行规则对象
     * @return 更新记录的行数
     */
    @UpdateProvider(type = PmsParkRuleSqlProvider.class, method = "update")
    int update(PmsParkRule parkRule);

    /**
     * 删除规则
     *
     * @param ruleId 规则ID
     * @return 删除记录的行数
     */
    @Delete("DELETE FROM pms_park_rule WHERE rule_id = #{ruleId}")
    int delete(Long ruleId);

    /**
     * 条件查询规则
     *
     * @param parkRule 车场通行规则对象，包含查询条件
     * @return 车场通行规则列表
     */
    @SelectProvider(type = PmsParkRuleSqlProvider.class, method = "selectAll")
    List<PmsParkRule> selectParkRulesByCriteria(PmsParkRule parkRule);


    /**
     * 根据规则ID查询通行规则
     */
    @Select("SELECT * FROM pms_park_rule WHERE rule_id = #{ruleId}")
    PmsParkRule selectParkRuleById(Long ruleId);


    /**
     * 根据规则ID删除通行规则
     */
    @Delete("DELETE FROM pms_park_rule WHERE rule_id = #{ruleId}")
    void deleteParkRule(Long ruleId);


}