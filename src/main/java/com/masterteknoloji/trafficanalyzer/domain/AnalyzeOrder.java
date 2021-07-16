package com.masterteknoloji.trafficanalyzer.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import com.masterteknoloji.trafficanalyzer.domain.enumeration.AnalyzeState;

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

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "end_date")
    private Instant endDate;

    @Column(name = "process_duration")
    private Long processDuration;

    @Column(name = "video_duration")
    private Long videoDuration;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private AnalyzeState state;

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

    public Instant getStartDate() {
        return startDate;
    }

    public AnalyzeOrder startDate(Instant startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public AnalyzeOrder endDate(Instant endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public Long getProcessDuration() {
        return processDuration;
    }

    public AnalyzeOrder processDuration(Long processDuration) {
        this.processDuration = processDuration;
        return this;
    }

    public void setProcessDuration(Long processDuration) {
        this.processDuration = processDuration;
    }

    public Long getVideoDuration() {
        return videoDuration;
    }

    public AnalyzeOrder videoDuration(Long videoDuration) {
        this.videoDuration = videoDuration;
        return this;
    }

    public void setVideoDuration(Long videoDuration) {
        this.videoDuration = videoDuration;
    }

    public AnalyzeState getState() {
        return state;
    }

    public AnalyzeOrder state(AnalyzeState state) {
        this.state = state;
        return this;
    }

    public void setState(AnalyzeState state) {
        this.state = state;
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
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", processDuration=" + getProcessDuration() +
            ", videoDuration=" + getVideoDuration() +
            ", state='" + getState() + "'" +
            "}";
    }
}
