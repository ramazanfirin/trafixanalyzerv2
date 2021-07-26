package com.masterteknoloji.trafficanalyzer.web.rest.vm;

import java.util.Date;

public class AnalyzeOrderSummaryVM {
	
	Long id;
	
	String state;
	
	String videoName;
	
	String videoPath;
	
	String scenarioPath;
	
	String startDate;

	String endDate;
	
	Long scenarioId;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getVideoName() {
		return videoName;
	}

	public void setVideoName(String videoName) {
		this.videoName = videoName;
	}

	public String getVideoPath() {
		return videoPath;
	}

	public void setVideoPath(String videoPath) {
		this.videoPath = videoPath;
	}

	public String getScenarioPath() {
		return scenarioPath;
	}

	public void setScenarioPath(String scenarioPath) {
		this.scenarioPath = scenarioPath;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public Long getScenarioId() {
		return scenarioId;
	}

	public void setScenarioId(Long scenarioId) {
		this.scenarioId = scenarioId;
	}
	
	

}
