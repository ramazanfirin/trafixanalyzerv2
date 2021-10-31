package com.masterteknoloji.trafficanalyzer.web.rest.vm.analyzeorderdetails;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "classes")
public class VehicleTypeVM {

	Boolean car = true;
	Boolean bus = true;
	Boolean truck = true;
	Boolean bike = true;
	Boolean person = false;
	
	public Boolean getCar() {
		return car;
	}
	public void setCar(Boolean car) {
		this.car = car;
	}
	public Boolean getBus() {
		return bus;
	}
	public void setBus(Boolean bus) {
		this.bus = bus;
	}
	public Boolean getTruck() {
		return truck;
	}
	public void setTruck(Boolean truck) {
		this.truck = truck;
	}
	public Boolean getBike() {
		return bike;
	}
	public void setBike(Boolean bike) {
		this.bike = bike;
	}
	public Boolean getPerson() {
		return person;
	}
	public void setPerson(Boolean person) {
		this.person = person;
	}
	
	
}
