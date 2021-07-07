package com.masterteknoloji.trafficanalyzer.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Direction.
 */
@Entity
@Table(name = "direction")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Direction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "path_coordinates")
    private String pathCoordinates;

    @ManyToOne
    private Scenario scenario;

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

    public Direction name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPathCoordinates() {
        return pathCoordinates;
    }

    public Direction pathCoordinates(String pathCoordinates) {
        this.pathCoordinates = pathCoordinates;
        return this;
    }

    public void setPathCoordinates(String pathCoordinates) {
        this.pathCoordinates = pathCoordinates;
    }

    public Scenario getScenario() {
        return scenario;
    }

    public Direction scenario(Scenario scenario) {
        this.scenario = scenario;
        return this;
    }

    public void setScenario(Scenario scenario) {
        this.scenario = scenario;
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
        Direction direction = (Direction) o;
        if (direction.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), direction.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Direction{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", pathCoordinates='" + getPathCoordinates() + "'" +
            "}";
    }
}
