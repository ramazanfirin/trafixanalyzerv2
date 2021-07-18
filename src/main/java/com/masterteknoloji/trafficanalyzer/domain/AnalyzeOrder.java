package com.masterteknoloji.trafficanalyzer.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A AnalyzeOrder.
 */
@Entity
@Table(name = "analyze_order")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AnalyzeOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Video video;

    @ManyToOne
    private Scenario scenario;

    @ManyToOne
    private AnalyzeOrderDetails orderDetails;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Video getVideo() {
        return video;
    }

    public AnalyzeOrder video(Video video) {
        this.video = video;
        return this;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    public Scenario getScenario() {
        return scenario;
    }

    public AnalyzeOrder scenario(Scenario scenario) {
        this.scenario = scenario;
        return this;
    }

    public void setScenario(Scenario scenario) {
        this.scenario = scenario;
    }

    public AnalyzeOrderDetails getOrderDetails() {
        return orderDetails;
    }

    public AnalyzeOrder orderDetails(AnalyzeOrderDetails analyzeOrderDetails) {
        this.orderDetails = analyzeOrderDetails;
        return this;
    }

    public void setOrderDetails(AnalyzeOrderDetails analyzeOrderDetails) {
        this.orderDetails = analyzeOrderDetails;
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
        AnalyzeOrder analyzeOrder = (AnalyzeOrder) o;
        if (analyzeOrder.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), analyzeOrder.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AnalyzeOrder{" +
            "id=" + getId() +
            "}";
    }
}
