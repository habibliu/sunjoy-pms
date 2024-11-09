package com.sunjoy.parking.entity;

import com.sunjoy.common.core.web.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/5
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PmsParkRule extends BaseEntity {
    /**
     * 规则id
     */
    private Long ruleId;

    /**
     * 租户id
     */
    private Long tenantId;

    /**
     * 车场id
     */
    private Long parkId;

    /**
     * 规则代码，对应于后台类名
     */
    private String ruleCode;

    /**
     * 规则名称
     */
    private String ruleName;

    /**
     * 规则适用级别:0-车场，1-道通
     */
    private String applyLevel;

    /**
     * 规则适用目标对象:0-临时车，1-登记车，2-名单里车
     */
    private String applyTargets;

    /**
     * 规则参数，json格式数据
     */
    private String ruleParams;

    /**
     * 提示信息
     */
    private String notifyMessage;

    /**
     * 提示方式:0-屏显，1-语音，2-灯光
     */
    private String notifyMethods;

    /**
     * 允许通行:1－是，2－否
     */
    private String allowPass;

    /**
     * 通行方向:0--出场，1－入场，2－双方向
     */
    private String direction;

    /**
     * 时间范围
     */
    private String timeRange;

    /**
     * 状态（0正常 1停用）
     */
    private String status;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;
}