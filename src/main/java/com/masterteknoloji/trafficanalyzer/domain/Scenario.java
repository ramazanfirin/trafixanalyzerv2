package com.masterteknoloji.trafficanalyzer.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Scenario.
 */
@Entity
@Table(name = "scenario")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Scenario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Lob
    @Column(name = "screen_shot")
    private byte[] screenShot;

    @Column(name = "screen_shot_content_type")
    private String screenShotContentType;
    
    @Column(name = "active")
    private Boolean active = true;

    @ManyToOne
    private Video video;

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

    public Scenario name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getScreenShot() {
        return screenShot;
    }

    public Scenario screenShot(byte[] screenShot) {
        this.screenShot = screenShot;
        return this;
    }

    public void setScreenShot(byte[] screenShot) {
        this.screenShot = screenShot;
    }

    public String getScreenShotContentType() {
        return screenShotContentType;
    }

    public Scenario screenShotContentType(String screenShotContentType) {
        this.screenShotContentType = screenShotContentType;
        return this;
    }

    public void setScreenShotContentType(String screenShotContentType) {
        this.screenShotContentType = screenShotContentType;
    }

    public Video getVideo() {
        return video;
    }

    public Scenario video(Video video) {
        this.video = video;
        return this;
    }

    public void setVideo(Video video) {
        this.video = video;
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
        Scenario scenario = (Scenario) o;
        if (scenario.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), scenario.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Scenario{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", screenShot='" + getScreenShot() + "'" +
            ", screenShotContentType='" + getScreenShotContentType() + "'" +
            "}";
    }

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
}
