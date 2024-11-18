package com.sunjoy.parking.entity;

import com.sunjoy.common.core.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 车场通行规则实体类
 *
 * @author Habib
 * @date 2024/11/5
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class PmsParkRule extends BaseEntity {
    /**
     * 规则id
     */
    private Long ruleId;

    /**
     * 规则名称
     */
    private String ruleName;

    /**
     * 租户id
     */
    private Long tenantId;

    /**
     * 经营单位id
     */
    private Long opuId;

    /**
     * 车场id
     */
    private Long parkId;
    /**
     * 车场名称
     */
    private String parkName;

    /**
     * 规则适用级别:0-车场，1-道通,如果是通道级别，在params段中以json对象存储:{lanes:[{laneId:laneName},....]}
     */
    private String level;

    /**
     * 规则适用目标对象:0-所有车，1-临时车，2-登记车,包括各种收费标准的车辆,在params段中以json对象存储:{services:[{serviceId:serviceName},....]}
     */
    private String target;

    /**
     * 周期管制，0-全周，1-具体日,如果是具体日，在params段中以json对象存储:{days:[{1:周一},....]}
     */
    private String dayRange;

    /**
     * 指定时间范围,0-否，1-是，如果是，在params段中以json对象存储:{times:[{startTime:08:00,endTime:12:00},....]}
     */
    private String timeRange;

    /**
     * 允许通行:1－是，0－否
     */
    private String allowPass;

    /**
     * 通行方向:0--出场，1－入场，2－双方向
     */
    private String direction;

    /**
     * 车场是否满位,0--否，1--是
     */
    private String parkFull;

    /**
     * 服务是否过期：0-否，1--是
     */
    private String targetExpire;

    /**
     * 提示信息
     */
    private String notifyMessage;

    /**
     * 提示方式:0-屏显，1-语音，2-灯光
     */
    private String notifyMethods;
    /**
     * 明细参数，json格式字符串
     */
    private String detailParams;
    /**
     * 状态（0正常 1停用）
     */
    private String status;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

}