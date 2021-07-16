package com.masterteknoloji.trafficanalyzer.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A AnalyzeOrderDetails.
 */
@Entity
@Table(name = "analyze_order_details")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AnalyzeOrderDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "session_id")
    private String sessionId;

    @Column(name = "video_path")
    private String videoPath;

    @Column(name = "count")
    private Boolean count;

    @Lob
    @Column(name = "classes")
    private String classes;

    @Lob
    @Column(name = "directions")
    private String directions;

    @Lob
    @Column(name = "speed")
    private String speed;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSessionId() {
        return sessionId;
    }

    public AnalyzeOrderDetails sessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public AnalyzeOrderDetails videoPath(String videoPath) {
        this.videoPath = videoPath;
        return this;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public Boolean isCount() {
        return count;
    }

    public AnalyzeOrderDetails count(Boolean count) {
        this.count = count;
        return this;
    }

    public void setCount(Boolean count) {
        this.count = count;
    }

    public String getClasses() {
        return classes;
    }

    public AnalyzeOrderDetails classes(String classes) {
        this.classes = classes;
        return this;
    }

    public void setClasses(String classes) {
        this.classes = classes;
    }

    public String getDirections() {
        return directions;
    }

    public AnalyzeOrderDetails directions(String directions) {
        this.directions = directions;
        return this;
    }

    public void setDirections(String directions) {
        this.directions = directions;
    }

    public String getSpeed() {
        return speed;
    }

    public AnalyzeOrderDetails speed(String speed) {
        this.speed = speed;
        return this;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
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
        AnalyzeOrderDetails analyzeOrderDetails = (AnalyzeOrderDetails) o;
        if (analyzeOrderDetails.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), analyzeOrderDetails.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AnalyzeOrderDetails{" +
            "id=" + getId() +
            ", sessionId='" + getSessionId() + "'" +
            ", videoPath='" + getVideoPath() + "'" +
            ", count='" + isCount() + "'" +
            ", classes='" + getClasses() + "'" +
            ", directions='" + getDirections() + "'" +
            ", speed='" + getSpeed() + "'" +
            "}";
    }
}
