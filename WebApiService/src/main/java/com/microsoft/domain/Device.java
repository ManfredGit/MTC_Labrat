package com.microsoft.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Device.
 */
@Entity
@Table(name = "device")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Device implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(min = 3)
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "manufacture", nullable = false)
    private String manufacture;

    @Column(name = "model")
    private String model;

    @Column(name = "api_user_id")
    private String apiUserId;

    @Column(name = "api_uri_prefix")
    private String apiUriPrefix;

    @Column(name = "api_token")
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

    public Device name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManufacture() {
        return manufacture;
    }

    public Device manufacture(String manufacture) {
        this.manufacture = manufacture;
        return this;
    }

    public void setManufacture(String manufacture) {
        this.manufacture = manufacture;
    }

    public String getModel() {
        return model;
    }

    public Device model(String model) {
        this.model = model;
        return this;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getApiUserId() {
        return apiUserId;
    }

    public Device apiUserId(String apiUserId) {
        this.apiUserId = apiUserId;
        return this;
    }

    public void setApiUserId(String apiUserId) {
        this.apiUserId = apiUserId;
    }

    public String getApiUriPrefix() {
        return apiUriPrefix;
    }

    public Device apiUriPrefix(String apiUriPrefix) {
        this.apiUriPrefix = apiUriPrefix;
        return this;
    }

    public void setApiUriPrefix(String apiUriPrefix) {
        this.apiUriPrefix = apiUriPrefix;
    }

    public String getApiToken() {
        return apiToken;
    }

    public Device apiToken(String apiToken) {
        this.apiToken = apiToken;
        return this;
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
        Device device = (Device) o;
        if (device.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, device.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Device{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", manufacture='" + manufacture + "'" +
            ", model='" + model + "'" +
            ", apiUserId='" + apiUserId + "'" +
            ", apiUriPrefix='" + apiUriPrefix + "'" +
            ", apiToken='" + apiToken + "'" +
            '}';
    }
}
