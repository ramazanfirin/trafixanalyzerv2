package com.masterteknoloji.trafficanalyzer.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

import com.masterteknoloji.trafficanalyzer.domain.enumeration.PolygonType;

/**
 * A Polygon.
 */
@Entity
@Table(name = "polygon")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Polygon implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "points")
    private String points;

    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_type")
    private PolygonType type;

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

    public Polygon name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPoints() {
        return points;
    }

    public Polygon points(String points) {
        this.points = points;
        return this;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public PolygonType getType() {
        return type;
    }

    public Polygon type(PolygonType type) {
        this.type = type;
        return this;
    }

    public void setType(PolygonType type) {
        this.type = type;
    }

    public Scenario getScenario() {
        return scenario;
    }

    public Polygon scenario(Scenario scenario) {
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
        Polygon polygon = (Polygon) o;
        if (polygon.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), polygon.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Polygon{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", points='" + getPoints() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
