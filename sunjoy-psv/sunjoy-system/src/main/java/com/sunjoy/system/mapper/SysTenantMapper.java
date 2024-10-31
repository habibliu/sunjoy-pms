package com.sunjoy.system.mapper;

import com.sunjoy.system.domain.SysTenant;
import com.sunjoy.system.mapper.provider.SysTenantSqlProvider;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/10/30
 */
@Mapper
public interface SysTenantMapper {

    @Insert("INSERT INTO sys_tenant (tenant_name, tenant_code, leader, phone, email, bank, bank_acc, registration_date, payment_method, settlement_cycle, settlement_date, wechat_account, alipay_account, status, del_flag, create_by, create_time) " +
            "VALUES (#{tenantName}, #{tenantCode}, #{leader}, #{phone}, #{email}, #{bank}, #{bankAcc}, #{registrationDate}, #{paymentMethod}, #{settlementCycle}, #{settlementDate}, #{wechatAccount}, #{alipayAccount}, #{status}, #{delFlag}, #{createBy}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "tenantId")
    void insert(SysTenant tenant);

    @Select("SELECT * FROM sys_tenant WHERE tenant_id = #{tenantId}")
    SysTenant selectById(Long tenantId);

    @Select("SELECT * FROM sys_tenant")
    List<SysTenant> selectAll();

    @Update("UPDATE sys_tenant SET tenant_name = #{tenantName}, tenant_code = #{tenantCode}, leader = #{leader}, phone = #{phone}, email = #{email}, bank = #{bank}, bank_acc = #{bankAcc}, registration_date = #{registrationDate}, payment_method = #{paymentMethod}, settlement_cycle = #{settlementCycle}, settlement_date = #{settlementDate}, wechat_account = #{wechatAccount}, alipay_account = #{alipayAccount}, status = #{status}, del_flag = #{delFlag}, update_by = #{updateBy}, update_time = #{updateTime} WHERE tenant_id = #{tenantId}")
    void update(SysTenant tenant);

    @Delete("DELETE FROM sys_tenant WHERE tenant_id = #{tenantId}")
    void delete(Long tenantId);

    @SelectProvider(type = SysTenantSqlProvider.class, method = "dynamicSelect")
    List<SysTenant> selectByConditions(SysTenant tenant);
}