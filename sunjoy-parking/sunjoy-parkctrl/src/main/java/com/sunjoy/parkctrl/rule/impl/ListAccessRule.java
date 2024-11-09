package com.sunjoy.parkctrl.rule.impl;

import com.sunjoy.parkctrl.rule.IAccessRule;
import com.sunjoy.parking.enums.DirectionEnum;
import com.sunjoy.parking.enums.NotificationMethod;
import com.sunjoy.parking.vo.VehiclePassage;

/**
 * 名单规则
 *
 * @author Habib
 * @date 2024/11/5
 */
public class ListAccessRule implements IAccessRule {
    @Override
    public String getRuleName() {
        return "车辆名单规则";
    }

    @Override
    public String getRuleCode() {
        return "PARK:RULE:Lis";
    }

    @Override
    public boolean isAllowed(VehiclePassage vehiclePassage) {
        return false;
    }

    @Override
    public DirectionEnum getAction() {
        return null;
    }

    @Override
    public String getNotifyMessage() {
        return "";
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