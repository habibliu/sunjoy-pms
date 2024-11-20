package com.sunjoy.parkctrl.rule.impl;

import com.sunjoy.common.core.enums.YesNoEnum;
import com.sunjoy.parkctrl.rule.IAccessRule;
import com.sunjoy.parking.entity.PmsParkRule;
import com.sunjoy.parking.enums.DirectionEnum;
import com.sunjoy.parking.enums.NotificationMethod;
import com.sunjoy.parking.enums.RuleApplyLevelEnum;
import com.sunjoy.parking.vo.VehiclePassage;

/**
 * 通行规则基本实现类
 *
 * @author Habib
 * @date 2024/11/5
 */
public class BaseAccessRuleImpl implements IAccessRule {
    private PmsParkRule rule;


    public BaseAccessRuleImpl(PmsParkRule rule) {
        this.rule = rule;
    }

    @Override
    public String getRuleName() {
        return rule.getRuleName();
    }

    @Override
    public PmsParkRule getRule() {
        return this.rule;
    }


    /**
     * 通行逻辑的基本实现
     *
     * @param vehiclePassage
     * @return
     */
    @Override
    public boolean isAllowed(VehiclePassage vehiclePassage) {
        //校验是否地点受限
        boolean levelValid = this.getRule().getLevel().equals(RuleApplyLevelEnum.PARK.getValue()) || new LaneMatcher().match(vehiclePassage, this);
        //校验是否身份受限
        boolean serviceValid = new IndentifyMatcher().match(vehiclePassage, this);
        //校验是否周期管控
        boolean dayRangeValid = this.getRule().getDayRange().equals(YesNoEnum.No.getCode()) || new DayRangeMatcher().match(vehiclePassage, this);
        //校验是否时段管控
        boolean timeRangeVaid = this.getRule().getTimeRange().equals(YesNoEnum.No.getCode()) || new TimeRangeMatcher().match(vehiclePassage, this);
        //校验是否车场满位管控
        boolean parkFullValid = this.getRule().getParkFull().equals(YesNoEnum.No.getCode()) || new ParkFullMatcher().match(vehiclePassage, this);
        //校验是否身份过期管控
        boolean serviceExpiredValid = this.getRule().getTargetExpire().equals(YesNoEnum.No.getCode()) || new ServiceExpiredMatcher().match(vehiclePassage, this);

        return (levelValid && serviceValid && dayRangeValid && timeRangeVaid && parkFullValid && serviceExpiredValid) && rule.getAllowPass().equals(YesNoEnum.Yes.getCode());
    }

    @Override
    public DirectionEnum getDirection() {
        return DirectionEnum.fromValue(rule.getDirection());
    }

    @Override
    public String getNotifyMessage() {
        return rule.getNotifyMessage();
    }


    @Override
    public NotificationMethod[] getNotifyMethods() {
        return new NotificationMethod[0];
    }

    @Override
    public boolean isForbidden() {
        return false;
    }


}