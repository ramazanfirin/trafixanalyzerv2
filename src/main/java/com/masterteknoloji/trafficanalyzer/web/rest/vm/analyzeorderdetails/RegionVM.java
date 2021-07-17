package com.masterteknoloji.trafficanalyzer.web.rest.vm.analyzeorderdetails;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "regions")
public class RegionVM {

	String label;
	
	List<PointsVM> points = new ArrayList<PointsVM>();

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public List<PointsVM> getPoints() {
		return points;
	}

	public void setPoints(List<PointsVM> points) {
		this.points = points;
	}
	
	
}
