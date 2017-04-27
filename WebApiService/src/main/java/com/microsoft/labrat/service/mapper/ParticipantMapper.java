package com.microsoft.labrat.service.mapper;

import com.microsoft.labrat.domain.*;
import com.microsoft.labrat.service.dto.ParticipantDTO;

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
    /**
     * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
     * creating a new attribute to know if the entity has any relationship from some other entity
     *
     * @param id id of the entity
     * @return the entity instance
     */
     
    default Participant participantFromId(Long id) {
        if (id == null) {
            return null;
        }
        Participant participant = new Participant();
        participant.setId(id);
        return participant;
    }
    

}
