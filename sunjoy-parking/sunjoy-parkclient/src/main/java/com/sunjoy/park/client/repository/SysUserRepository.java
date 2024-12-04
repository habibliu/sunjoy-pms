package com.sunjoy.park.client.repository;

import com.sunjoy.park.client.entity.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 系统用户
 *
 * @author Habib
 * @date 2024/11/28
 */
@Repository
public interface SysUserRepository extends JpaRepository<SysUser, Long> {
}