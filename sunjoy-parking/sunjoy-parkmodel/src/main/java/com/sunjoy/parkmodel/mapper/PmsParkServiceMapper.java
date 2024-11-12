package com.sunjoy.parkmodel.mapper;

import com.sunjoy.parking.entity.PmsParkService;
import com.sunjoy.parkmodel.mapper.provider.PmsParkServiceSqlProvider;
import com.sunjoy.parkmodel.pojo.ParkServicePojo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import java.util.List;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/8
 */
@Mapper
public interface PmsParkServiceMapper {
    @SelectProvider(type = PmsParkServiceSqlProvider.class, method = "buildSelectQuery")
    List<ParkServicePojo> selectByConditions(PmsParkService conditions);

    @Insert("INSERT INTO pms_park_service (tenant_id, opu_id, park_id, price_id, expired_allowed, " +
            "expired_duration, entry_message, exit_message, status, del_flag, create_by, create_time, " +
            "update_by, update_time, remark) " +
            "VALUES (#{tenantId}, #{opuId}, #{parkId}, #{priceId}, #{expiredAllowed}, " +
            "#{expiredDuration}, #{entryMessage}, #{exitMessage}, #{status}, #{delFlag}, " +
            "#{createBy}, #{createTime}, #{updateBy}, #{updateTime}, #{remark})")
    int insert(PmsParkService parkService);


    @UpdateProvider(type = PmsParkServiceSqlProvider.class, method = "updatePmsParkService")
    void update(PmsParkService parkService);
}