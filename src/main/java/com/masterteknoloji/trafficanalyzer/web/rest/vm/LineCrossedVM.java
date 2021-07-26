package com.masterteknoloji.trafficanalyzer.web.rest.vm;

public class LineCrossedVM {

	String type;
	Long duration;
	Long lineId;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Long getDuration() {
		return duration;
	}
	public void setDuration(Long duration) {
		this.duration = duration;
	}
	public Long getLineId() {
		return lineId;
	}
	public void setLineId(Long lineId) {
		this.lineId = lineId;
	}
	
	
}
