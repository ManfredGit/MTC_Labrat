package com.microsoft.labrat.repository;

import com.microsoft.labrat.domain.Intervention;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Intervention entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InterventionRepository extends JpaRepository<Intervention,Long> {

}
