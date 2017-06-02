package com.microsoft.labrat.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Device entity.
 */
public class DeviceDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 3)
    private String name;

    @NotNull
    private String manufacture;

    private String model;

    private String apiUserId;

    private String apiUriPrefix;

    private String apiToken;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManufacture() {
        return manufacture;
    }

    public void setManufacture(String manufacture) {
        this.manufacture = manufacture;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getApiUserId() {
        return apiUserId;
    }

    public void setApiUserId(String apiUserId) {
        this.apiUserId = apiUserId;
    }

    public String getApiUriPrefix() {
        return apiUriPrefix;
    }

    public void setApiUriPrefix(String apiUriPrefix) {
        this.apiUriPrefix = apiUriPrefix;
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DeviceDTO deviceDTO = (DeviceDTO) o;
        if(deviceDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), deviceDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DeviceDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", manufacture='" + getManufacture() + "'" +
            ", model='" + getModel() + "'" +
            ", apiUserId='" + getApiUserId() + "'" +
            ", apiUriPrefix='" + getApiUriPrefix() + "'" +
            ", apiToken='" + getApiToken() + "'" +
            "}";
    }
}
