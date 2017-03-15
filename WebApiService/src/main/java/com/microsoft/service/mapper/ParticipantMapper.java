package com.microsoft.service.mapper;

import com.microsoft.domain.*;
import com.microsoft.service.dto.ParticipantDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Participant and its DTO ParticipantDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ParticipantMapper {

    ParticipantDTO participantToParticipantDTO(Participant participant);

    List<ParticipantDTO> participantsToParticipantDTOs(List<Participant> participants);

    Participant participantDTOToParticipant(ParticipantDTO participantDTO);

    List<Participant> participantDTOsToParticipants(List<ParticipantDTO> participantDTOs);
}
