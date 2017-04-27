package com.microsoft.labrat.service.mapper;

import com.microsoft.labrat.domain.*;
import com.microsoft.labrat.service.dto.LeaseDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Lease and its DTO LeaseDTO.
 */
@Mapper(componentModel = "spring", uses = {DeviceMapper.class, ParticipantMapper.class, })
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
    /**
     * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
     * creating a new attribute to know if the entity has any relationship from some other entity
     *
     * @param id id of the entity
     * @return the entity instance
     */
     
    default Lease leaseFromId(Long id) {
        if (id == null) {
            return null;
        }
        Lease lease = new Lease();
        lease.setId(id);
        return lease;
    }
    

}
