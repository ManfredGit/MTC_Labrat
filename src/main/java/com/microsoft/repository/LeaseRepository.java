package com.microsoft.repository;

import com.microsoft.domain.Lease;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Lease entity.
 */
@SuppressWarnings("unused")
public interface LeaseRepository extends JpaRepository<Lease,Long> {

}
