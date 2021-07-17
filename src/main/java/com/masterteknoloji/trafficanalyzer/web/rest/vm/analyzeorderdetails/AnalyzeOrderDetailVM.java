package com.masterteknoloji.trafficanalyzer.web.rest.vm.analyzeorderdetails;

public class AnalyzeOrderDetailVM {

	String sessionId;
	
	String path;
	
	Boolean count=true;
	
	VehicleTypeVM vehicleTypeVM = new VehicleTypeVM();
	
	DirectionVM directions = new DirectionVM();
	
	SpeedVM speed = new SpeedVM();

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

	public VehicleTypeVM getVehicleTypeVM() {
		return vehicleTypeVM;
	}

	public void setVehicleTypeVM(VehicleTypeVM vehicleTypeVM) {
		this.vehicleTypeVM = vehicleTypeVM;
	}

	public DirectionVM getDirections() {
		return directions;
	}

	public void setDirections(DirectionVM directions) {
		this.directions = directions;
	}

	public SpeedVM getSpeed() {
		return speed;
	}

	public void setSpeed(SpeedVM speed) {
		this.speed = speed;
	}
	
	
}
