package com.microsoft.labrat.service.mapper;

import com.microsoft.labrat.domain.*;
import com.microsoft.labrat.service.dto.DeviceDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Device and its DTO DeviceDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DeviceMapper {

    DeviceDTO deviceToDeviceDTO(Device device);

    List<DeviceDTO> devicesToDeviceDTOs(List<Device> devices);

    Device deviceDTOToDevice(DeviceDTO deviceDTO);

    List<Device> deviceDTOsToDevices(List<DeviceDTO> deviceDTOs);
    /**
     * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
     * creating a new attribute to know if the entity has any relationship from some other entity
     *
     * @param id id of the entity
     * @return the entity instance
     */
     
    default Device deviceFromId(Long id) {
        if (id == null) {
            return null;
        }
        Device device = new Device();
        device.setId(id);
        return device;
    }
    

}
