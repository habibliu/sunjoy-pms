package com.sunjoy.parkctrl.pojo;

import java.security.Timestamp;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/4
 */
public class CarEntryPojo {
    private int id;
    private Long parkingId;
    private Long parkId;
    private Long parentParkId;
    private Long entryLaneId;
    private Long exitLaneId;
    private Long entryDeviceId;
    private Long exitDeviceId;
    private String licenseNumber;
    private String licenseType;
    private Timestamp entryTime;
    private Timestamp exitTime;
    //停留时间
    private Long stayTime;
    private String identify;
}