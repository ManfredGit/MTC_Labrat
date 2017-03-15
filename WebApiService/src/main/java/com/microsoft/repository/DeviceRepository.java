package com.microsoft.repository;

import com.microsoft.domain.Device;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Device entity.
 */
@SuppressWarnings("unused")
public interface DeviceRepository extends JpaRepository<Device,Long> {

}
