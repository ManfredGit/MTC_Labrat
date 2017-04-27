package com.microsoft.labrat.repository;

import com.microsoft.labrat.domain.Lease;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Lease entity.
 */
@SuppressWarnings("unused")
public interface LeaseRepository extends JpaRepository<Lease,Long> {

}
