package com.sunjoy.park.client.repository;

import com.sunjoy.park.client.entity.PlatformInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/29
 */
@Repository
public interface SystemSettingRepository extends JpaRepository<PlatformInfo, Long> {
}