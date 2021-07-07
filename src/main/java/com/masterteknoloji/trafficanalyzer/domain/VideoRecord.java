package com.masterteknoloji.trafficanalyzer.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import com.masterteknoloji.trafficanalyzer.domain.enumeration.ProcessType;

/**
 * A VideoRecord.
 */
@Entity
@Table(name = "video_record")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class VideoRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "insert_date", nullable = false)
    private Instant insertDate;

    @NotNull
    @Column(name = "vehicle_type", nullable = false)
    private String vehicleType;

    @NotNull
    @Column(name = "duration", nullable = false)
    private Long duration;

    @Column(name = "speed")
    private Double speed;

    @Enumerated(EnumType.STRING)
    @Column(name = "process_type")
    private ProcessType processType;

    @ManyToOne
    private Video video;

    @ManyToOne
    private Line line;

    @ManyToOne
    private Direction direction;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getInsertDate() {
        return insertDate;
    }

    public VideoRecord insertDate(Instant insertDate) {
        this.insertDate = insertDate;
        return this;
    }

    public void setInsertDate(Instant insertDate) {
        this.insertDate = insertDate;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public VideoRecord vehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
        return this;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Long getDuration() {
        return duration;
    }

    public VideoRecord duration(Long duration) {
        this.duration = duration;
        return this;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Double getSpeed() {
        return speed;
    }

    public VideoRecord speed(Double speed) {
        this.speed = speed;
        return this;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public ProcessType getProcessType() {
        return processType;
    }

    public VideoRecord processType(ProcessType processType) {
        this.processType = processType;
        return this;
    }

    public void setProcessType(ProcessType processType) {
        this.processType = processType;
    }

    public Video getVideo() {
        return video;
    }

    public VideoRecord video(Video video) {
        this.video = video;
        return this;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    public Line getLine() {
        return line;
    }

    public VideoRecord line(Line line) {
        this.line = line;
        return this;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public Direction getDirection() {
        return direction;
    }

    public VideoRecord direction(Direction direction) {
        this.direction = direction;
        return this;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
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
        VideoRecord videoRecord = (VideoRecord) o;
        if (videoRecord.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), videoRecord.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "VideoRecord{" +
            "id=" + getId() +
            ", insertDate='" + getInsertDate() + "'" +
            ", vehicleType='" + getVehicleType() + "'" +
            ", duration=" + getDuration() +
            ", speed=" + getSpeed() +
            ", processType='" + getProcessType() + "'" +
            "}";
    }
}
