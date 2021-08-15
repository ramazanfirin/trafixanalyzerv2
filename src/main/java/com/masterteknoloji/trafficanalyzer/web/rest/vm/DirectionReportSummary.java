package com.masterteknoloji.trafficanalyzer.web.rest.vm;

public class DirectionReportSummary {

	Long count;
	String directionName;
	String startLineName;
	String endLineName;
	Long startLineCount;
	Long endLineCount;
	Long startLineRate;
	Long endLineRate;
	
	public Long getCount() {
		return count;
	}
	public void setCount(Long count) {
		this.count = count;
	}
	public String getDirectionName() {
		return directionName;
	}
	public void setDirectionName(String directionName) {
		this.directionName = directionName;
	}
	public String getStartLineName() {
		return startLineName;
	}
	public void setStartLineName(String startLineName) {
		this.startLineName = startLineName;
	}
	public String getEndLineName() {
		return endLineName;
	}
	public void setEndLineName(String endLineName) {
		this.endLineName = endLineName;
	}
	public Long getStartLineCount() {
		return startLineCount;
	}
	public void setStartLineCount(Long startLineCount) {
		this.startLineCount = startLineCount;
	}
	public Long getEndLineCount() {
		return endLineCount;
	}
	public void setEndLineCount(Long endLineCount) {
		this.endLineCount = endLineCount;
	}
	public Long getStartLineRate() {
		return startLineRate;
	}
	public void setStartLineRate(Long startLineRate) {
		this.startLineRate = startLineRate;
	}
	public Long getEndLineRate() {
		return endLineRate;
	}
	public void setEndLineRate(Long endLineRate) {
		this.endLineRate = endLineRate;
	}
	
	
}
