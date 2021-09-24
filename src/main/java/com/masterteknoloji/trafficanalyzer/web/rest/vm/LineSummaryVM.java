package com.masterteknoloji.trafficanalyzer.web.rest.vm;

public class LineSummaryVM {

	Long id;
	String name;
	String points;
	String entryPolygonPoints;
	String exitPolygonPoints;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPoints() {
		return points;
	}
	public void setPoints(String points) {
		this.points = points;
	}
	public String getExitPolygonPoints() {
		return exitPolygonPoints;
	}
	public void setExitPolygonPoints(String exitPolygonPoints) {
		this.exitPolygonPoints = exitPolygonPoints;
	}
	public String getEntryPolygonPoints() {
		return entryPolygonPoints;
	}
	public void setEntryPolygonPoints(String entryPolygonPoints) {
		this.entryPolygonPoints = entryPolygonPoints;
	}
}
