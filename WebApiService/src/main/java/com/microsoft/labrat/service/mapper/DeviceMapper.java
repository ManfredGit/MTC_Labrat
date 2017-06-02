package com.microsoft.labrat.service.mapper;

import com.microsoft.labrat.domain.*;
import com.microsoft.labrat.service.dto.DeviceDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Device and its DTO DeviceDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DeviceMapper extends EntityMapper <DeviceDTO, Device> {
    
    
    default Device fromId(Long id) {
        if (id == null) {
            return null;
        }
        Device device = new Device();
        device.setId(id);
        return device;
    }
}
