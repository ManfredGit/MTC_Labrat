package com.microsoft.labrat.service.mapper;

import com.microsoft.labrat.domain.*;
import com.microsoft.labrat.service.dto.LeaseDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Lease and its DTO LeaseDTO.
 */
@Mapper(componentModel = "spring", uses = {DeviceMapper.class, ParticipantMapper.class, })
public interface LeaseMapper extends EntityMapper <LeaseDTO, Lease> {

    @Mapping(source = "device.id", target = "deviceId")
    @Mapping(source = "device.name", target = "deviceName")

    @Mapping(source = "participant.id", target = "participantId")
    LeaseDTO toDto(Lease lease); 

    @Mapping(source = "deviceId", target = "device")

    @Mapping(source = "participantId", target = "participant")
    Lease toEntity(LeaseDTO leaseDTO); 
    default Lease fromId(Long id) {
        if (id == null) {
            return null;
        }
        Lease lease = new Lease();
        lease.setId(id);
        return lease;
    }
}
