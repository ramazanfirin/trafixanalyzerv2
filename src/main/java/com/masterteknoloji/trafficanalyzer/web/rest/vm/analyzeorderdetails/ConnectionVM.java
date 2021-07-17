package com.masterteknoloji.trafficanalyzer.web.rest.vm.analyzeorderdetails;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "connections")
public class ConnectionVM {
	
	String entry;
	String exit;
	
	public String getEntry() {
		return entry;
	}
	public void setEntry(String entry) {
		this.entry = entry;
	}
	public String getExit() {
		return exit;
	}
	public void setExit(String exit) {
		this.exit = exit;
	}
	
	
}
