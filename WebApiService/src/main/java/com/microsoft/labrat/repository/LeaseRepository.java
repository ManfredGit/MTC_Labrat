package com.microsoft.labrat.repository;

import com.microsoft.labrat.domain.Lease;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Lease entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LeaseRepository extends JpaRepository<Lease,Long> {

}
