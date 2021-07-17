package com.masterteknoloji.trafficanalyzer.web.rest.vm.analyzeorderdetails;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "speed")
public class SpeedVM {
	
	String label;
	
	@JsonProperty("distance_in_meters")
	Long distance;
	
	List<PointsVM> points =new ArrayList<PointsVM>();

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Long getDistance() {
		return distance;
	}

	public void setDistance(Long distance) {
		this.distance = distance;
	}

	public List<PointsVM> getPoints() {
		return points;
	}

	public void setPoints(List<PointsVM> points) {
		this.points = points;
	}
	
	
}
