package com.masterteknoloji.trafficanalyzer.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Line.
 */
@Entity
@Table(name = "line")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Line implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "start_point_x")
    private Long startPointX;

    @Column(name = "start_point_y")
    private Long startPointY;

    @Column(name = "end_point_x")
    private Long endPointX;

    @Column(name = "end_point_y")
    private Long endPointY;

    @ManyToOne
    private Scenario scenario;

    @ManyToOne
    private Direction direction;

    @ManyToOne
    private Polygon startPolygon;

    @ManyToOne
    private Polygon endPolygon;

    @ManyToOne
    private Polygon calculatedPolygon;

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

    public Line name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getStartPointX() {
        return startPointX;
    }

    public Line startPointX(Long startPointX) {
        this.startPointX = startPointX;
        return this;
    }

    public void setStartPointX(Long startPointX) {
        this.startPointX = startPointX;
    }

    public Long getStartPointY() {
        return startPointY;
    }

    public Line startPointY(Long startPointY) {
        this.startPointY = startPointY;
        return this;
    }

    public void setStartPointY(Long startPointY) {
        this.startPointY = startPointY;
    }

    public Long getEndPointX() {
        return endPointX;
    }

    public Line endPointX(Long endPointX) {
        this.endPointX = endPointX;
        return this;
    }

    public void setEndPointX(Long endPointX) {
        this.endPointX = endPointX;
    }

    public Long getEndPointY() {
        return endPointY;
    }

    public Line endPointY(Long endPointY) {
        this.endPointY = endPointY;
        return this;
    }

    public void setEndPointY(Long endPointY) {
        this.endPointY = endPointY;
    }

    public Scenario getScenario() {
        return scenario;
    }

    public Line scenario(Scenario scenario) {
        this.scenario = scenario;
        return this;
    }

    public void setScenario(Scenario scenario) {
        this.scenario = scenario;
    }

    public Direction getDirection() {
        return direction;
    }

    public Line direction(Direction direction) {
        this.direction = direction;
        return this;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Polygon getStartPolygon() {
        return startPolygon;
    }

    public Line startPolygon(Polygon polygon) {
        this.startPolygon = polygon;
        return this;
    }

    public void setStartPolygon(Polygon polygon) {
        this.startPolygon = polygon;
    }

    public Polygon getEndPolygon() {
        return endPolygon;
    }

    public Line endPolygon(Polygon polygon) {
        this.endPolygon = polygon;
        return this;
    }

    public void setEndPolygon(Polygon polygon) {
        this.endPolygon = polygon;
    }

    public Polygon getCalculatedPolygon() {
        return calculatedPolygon;
    }

    public Line calculatedPolygon(Polygon polygon) {
        this.calculatedPolygon = polygon;
        return this;
    }

    public void setCalculatedPolygon(Polygon polygon) {
        this.calculatedPolygon = polygon;
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
        Line line = (Line) o;
        if (line.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), line.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Line{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", startPointX=" + getStartPointX() +
            ", startPointY=" + getStartPointY() +
            ", endPointX=" + getEndPointX() +
            ", endPointY=" + getEndPointY() +
            "}";
    }
}
