package com.masterteknoloji.trafficanalyzer.web.rest.vm.analyzeorderdetails;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "points")
public class PointsVM {
	
	Long x;
	Long y;
	
	public Long getX() {
		return x;
	}
	public void setX(Long x) {
		this.x = x;
	}
	public Long getY() {
		return y;
	}
	public void setY(Long y) {
		this.y = y;
	}
	
	

}
