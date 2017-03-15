package com.microsoft.service.mapper;

import com.microsoft.domain.*;
import com.microsoft.service.dto.LeaseDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Lease and its DTO LeaseDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface LeaseMapper {

    @Mapping(source = "device.id", target = "deviceId")
    @Mapping(source = "device.name", target = "deviceName")
    @Mapping(source = "participant.id", target = "participantId")
    LeaseDTO leaseToLeaseDTO(Lease lease);

    List<LeaseDTO> leasesToLeaseDTOs(List<Lease> leases);

    @Mapping(source = "deviceId", target = "device")
    @Mapping(source = "participantId", target = "participant")
    Lease leaseDTOToLease(LeaseDTO leaseDTO);

    List<Lease> leaseDTOsToLeases(List<LeaseDTO> leaseDTOs);

    default Device deviceFromId(Long id) {
        if (id == null) {
            return null;
        }
        Device device = new Device();
        device.setId(id);
        return device;
    }

    default Participant participantFromId(Long id) {
        if (id == null) {
            return null;
        }
        Participant participant = new Participant();
        participant.setId(id);
        return participant;
    }
}
