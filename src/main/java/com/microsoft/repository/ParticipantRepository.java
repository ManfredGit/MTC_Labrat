package com.microsoft.repository;

import com.microsoft.domain.Participant;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Participant entity.
 */
@SuppressWarnings("unused")
public interface ParticipantRepository extends JpaRepository<Participant,Long> {

}
