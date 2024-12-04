package com.sunjoy.park.client.repository;

import com.sunjoy.park.client.entity.LaneDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Class description
 *
 * @author Habib
 * @date 2024/11/27
 */
@Repository
public interface LaneDeviceRepository extends JpaRepository<LaneDevice, Long> {
}