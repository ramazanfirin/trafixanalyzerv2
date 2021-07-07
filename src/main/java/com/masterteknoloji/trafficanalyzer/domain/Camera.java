package com.masterteknoloji.trafficanalyzer.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Camera.
 */
@Entity
@Table(name = "camera")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Camera implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "ip")
    private String ip;

    @Column(name = "port")
    private String port;

    @Column(name = "connection_url")
    private String connectionUrl;

    @Column(name = "usename")
    private String usename;

    @Column(name = "jhi_password")
    private String password;

    @ManyToOne
    private Location location;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Camera name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public Camera ip(String ip) {
        this.ip = ip;
        return this;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public Camera port(String port) {
        this.port = port;
        return this;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getConnectionUrl() {
        return connectionUrl;
    }

    public Camera connectionUrl(String connectionUrl) {
        this.connectionUrl = connectionUrl;
        return this;
    }

    public void setConnectionUrl(String connectionUrl) {
        this.connectionUrl = connectionUrl;
    }

    public String getUsename() {
        return usename;
    }

    public Camera usename(String usename) {
        this.usename = usename;
        return this;
    }

    public void setUsename(String usename) {
        this.usename = usename;
    }

    public String getPassword() {
        return password;
    }

    public Camera password(String password) {
        this.password = password;
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Location getLocation() {
        return location;
    }

    public Camera location(Location location) {
        this.location = location;
        return this;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Camera camera = (Camera) o;
        if (camera.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), camera.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Camera{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", ip='" + getIp() + "'" +
            ", port='" + getPort() + "'" +
            ", connectionUrl='" + getConnectionUrl() + "'" +
            ", usename='" + getUsename() + "'" +
            ", password='" + getPassword() + "'" +
            "}";
    }
}
