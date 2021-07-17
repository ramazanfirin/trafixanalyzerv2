package com.masterteknoloji.trafficanalyzer.web.rest.vm.analyzeorderdetails;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "directions")
public class DirectionVM {

	List<RegionVM> regions = new ArrayList<RegionVM>();
	
	List<ConnectionVM> connections = new ArrayList<ConnectionVM>();

	public List<RegionVM> getRegions() {
		return regions;
	}

	public void setRegions(List<RegionVM> regions) {
		this.regions = regions;
	}

	public List<ConnectionVM> getConnections() {
		return connections;
	}

	public void setConnections(List<ConnectionVM> connections) {
		this.connections = connections;
	}
	
	
}
