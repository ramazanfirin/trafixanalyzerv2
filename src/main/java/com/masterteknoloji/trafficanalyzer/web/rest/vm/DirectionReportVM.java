package com.masterteknoloji.trafficanalyzer.web.rest.vm;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DirectionReportVM {

	BigInteger analyzeId;
	String  scenarioName;
	Date videoStartDate;
	Date videoEndDate;
	String videoName;
	
	List<DirectionReportDetail> details = new ArrayList<DirectionReportDetail>();

	public BigInteger getAnalyzeId() {
		return analyzeId;
	}

	public void setAnalyzeId(BigInteger analyzeId) {
		this.analyzeId = analyzeId;
	}

	public String getScenarioName() {
		return scenarioName;
	}

	public void setScenarioName(String scenarioName) {
		this.scenarioName = scenarioName;
	}

	public Date getVideoStartDate() {
		return videoStartDate;
	}

	public void setVideoStartDate(Date videoStartDate) {
		this.videoStartDate = videoStartDate;
	}

	public Date getVideoEndDate() {
		return videoEndDate;
	}

	public void setVideoEndDate(Date videoEndDate) {
		this.videoEndDate = videoEndDate;
	}

	public String getVideoName() {
		return videoName;
	}

	public void setVideoName(String videoName) {
		this.videoName = videoName;
	}

	public List<DirectionReportDetail> getDetails() {
		return details;
	}

	public void setDetails(List<DirectionReportDetail> details) {
		this.details = details;
	}
	
	
	
	
}
