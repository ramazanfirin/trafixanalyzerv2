package com.masterteknoloji.trafficanalyzer.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private AnalyzeState state;

    @Lob
    @Column(name = "screen_shoot")
    private byte[] screenShoot;

    @Column(name = "screen_shoot_content_type")
    private String screenShootContentType;

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

    public byte[] getScreenShoot() {
        return screenShoot;
    }

    public AnalyzeOrder screenShoot(byte[] screenShoot) {
        this.screenShoot = screenShoot;
        return this;
    }

    public void setScreenShoot(byte[] screenShoot) {
        this.screenShoot = screenShoot;
    }

    public String getScreenShootContentType() {
        return screenShootContentType;
    }

    public AnalyzeOrder screenShootContentType(String screenShootContentType) {
        this.screenShootContentType = screenShootContentType;
        return this;
    }

    public void setScreenShootContentType(String screenShootContentType) {
        this.screenShootContentType = screenShootContentType;
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
            ", state='" + getState() + "'" +
            ", screenShoot='" + getScreenShoot() + "'" +
            ", screenShootContentType='" + getScreenShootContentType() + "'" +
            "}";
    }
}
