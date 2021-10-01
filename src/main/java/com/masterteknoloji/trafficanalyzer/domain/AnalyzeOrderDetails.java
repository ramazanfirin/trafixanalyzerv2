package com.masterteknoloji.trafficanalyzer.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import com.masterteknoloji.trafficanalyzer.domain.enumeration.AnalyzeState;

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

    @Lob
    @Column(name = "json_data")
    private String jsonData;

    @Column(name = "error_message")
    private String errorMessage;

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

    @Column(name = "show_visulation_window")
    private Boolean showVisulationWindow;

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

    public String getJsonData() {
        return jsonData;
    }

    public AnalyzeOrderDetails jsonData(String jsonData) {
        this.jsonData = jsonData;
        return this;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public AnalyzeOrderDetails errorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public AnalyzeOrderDetails startDate(Instant startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public AnalyzeOrderDetails endDate(Instant endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public Long getProcessDuration() {
        return processDuration;
    }

    public AnalyzeOrderDetails processDuration(Long processDuration) {
        this.processDuration = processDuration;
        return this;
    }

    public void setProcessDuration(Long processDuration) {
        this.processDuration = processDuration;
    }

    public Long getVideoDuration() {
        return videoDuration;
    }

    public AnalyzeOrderDetails videoDuration(Long videoDuration) {
        this.videoDuration = videoDuration;
        return this;
    }

    public void setVideoDuration(Long videoDuration) {
        this.videoDuration = videoDuration;
    }

    public AnalyzeState getState() {
        return state;
    }

    public AnalyzeOrderDetails state(AnalyzeState state) {
        this.state = state;
        return this;
    }

    public void setState(AnalyzeState state) {
        this.state = state;
    }

    public Boolean isShowVisulationWindow() {
        return showVisulationWindow;
    }

    public AnalyzeOrderDetails showVisulationWindow(Boolean showVisulationWindow) {
        this.showVisulationWindow = showVisulationWindow;
        return this;
    }

    public void setShowVisulationWindow(Boolean showVisulationWindow) {
        this.showVisulationWindow = showVisulationWindow;
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
            ", jsonData='" + getJsonData() + "'" +
            ", errorMessage='" + getErrorMessage() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", processDuration=" + getProcessDuration() +
            ", videoDuration=" + getVideoDuration() +
            ", state='" + getState() + "'" +
            ", showVisulationWindow='" + isShowVisulationWindow() + "'" +
            "}";
    }
}
