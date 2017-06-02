package com.microsoft.labrat.service.mapper;

import com.microsoft.labrat.domain.*;
import com.microsoft.labrat.service.dto.ParticipantDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Participant and its DTO ParticipantDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ParticipantMapper extends EntityMapper <ParticipantDTO, Participant> {
    
    
    default Participant fromId(Long id) {
        if (id == null) {
            return null;
        }
        Participant participant = new Participant();
        participant.setId(id);
        return participant;
    }
}
