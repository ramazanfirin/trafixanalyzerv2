package com.masterteknoloji.trafficanalyzer.web.rest.vm.analyzeorderdetails;

import java.util.ArrayList;
import java.util.List;

public class AnalyzeOrderDetailVM {

	String sessionId;
	
	String path;
	
	Boolean count=true;
	
	VehicleTypeVM classes = new VehicleTypeVM();
	
	DirectionVM directions = new DirectionVM();
	
	List<SpeedVM> speed = new ArrayList<SpeedVM>();
	
	Boolean showVisulationWindow;
	
	String videoType;

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Boolean getCount() {
		return count;
	}

	public void setCount(Boolean count) {
		this.count = count;
	}

	public DirectionVM getDirections() {
		return directions;
	}

	public void setDirections(DirectionVM directions) {
		this.directions = directions;
	}

	public List<SpeedVM> getSpeed() {
		return speed;
	}

	public void setSpeed(List<SpeedVM> speed) {
		this.speed = speed;
	}

	public VehicleTypeVM getClasses() {
		return classes;
	}

	public void setClasses(VehicleTypeVM classes) {
		this.classes = classes;
	}

	public Boolean getShowVisulationWindow() {
		return showVisulationWindow;
	}

	public void setShowVisulationWindow(Boolean showVisulationWindow) {
		this.showVisulationWindow = showVisulationWindow;
	}

	public String getVideoType() {
		return videoType;
	}

	public void setVideoType(String videoType) {
		this.videoType = videoType;
	}

		
	
}
