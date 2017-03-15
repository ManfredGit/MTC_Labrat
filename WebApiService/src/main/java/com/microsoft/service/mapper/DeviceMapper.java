package com.microsoft.service.mapper;

import com.microsoft.domain.*;
import com.microsoft.service.dto.DeviceDTO;

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
}
