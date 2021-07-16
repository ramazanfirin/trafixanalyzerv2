package com.masterteknoloji.trafficanalyzer.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A RawRecord.
 */
@Entity
@Table(name = "raw_record")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class RawRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "session_id")
    private String sessionID;

    @Column(name = "jhi_time")
    private Instant time;

    @Column(name = "object_type")
    private String objectType;

    @Column(name = "speed")
    private Double speed;

    @Column(name = "jhi_entry")
    private String entry;

    @Column(name = "jhi_exit")
    private String exit;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSessionID() {
        return sessionID;
    }

    public RawRecord sessionID(String sessionID) {
        this.sessionID = sessionID;
        return this;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public Instant getTime() {
        return time;
    }

    public RawRecord time(Instant time) {
        this.time = time;
        return this;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    public String getObjectType() {
        return objectType;
    }

    public RawRecord objectType(String objectType) {
        this.objectType = objectType;
        return this;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public Double getSpeed() {
        return speed;
    }

    public RawRecord speed(Double speed) {
        this.speed = speed;
        return this;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public String getEntry() {
        return entry;
    }

    public RawRecord entry(String entry) {
        this.entry = entry;
        return this;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }

    public String getExit() {
        return exit;
    }

    public RawRecord exit(String exit) {
        this.exit = exit;
        return this;
    }

    public void setExit(String exit) {
        this.exit = exit;
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
        RawRecord rawRecord = (RawRecord) o;
        if (rawRecord.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), rawRecord.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RawRecord{" +
            "id=" + getId() +
            ", sessionID='" + getSessionID() + "'" +
            ", time='" + getTime() + "'" +
            ", objectType='" + getObjectType() + "'" +
            ", speed=" + getSpeed() +
            ", entry='" + getEntry() + "'" +
            ", exit='" + getExit() + "'" +
            "}";
    }
}
