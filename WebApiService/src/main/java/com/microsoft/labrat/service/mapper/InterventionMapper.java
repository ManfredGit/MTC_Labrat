package com.microsoft.labrat.service.mapper;

import com.microsoft.labrat.domain.*;
import com.microsoft.labrat.service.dto.InterventionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Intervention and its DTO InterventionDTO.
 */
@Mapper(componentModel = "spring", uses = {LeaseMapper.class, })
public interface InterventionMapper extends EntityMapper <InterventionDTO, Intervention> {

    @Mapping(source = "lease.id", target = "leaseId")
    InterventionDTO toDto(Intervention intervention); 

    @Mapping(source = "leaseId", target = "lease")
    Intervention toEntity(InterventionDTO interventionDTO); 
    default Intervention fromId(Long id) {
        if (id == null) {
            return null;
        }
        Intervention intervention = new Intervention();
        intervention.setId(id);
        return intervention;
    }
}
