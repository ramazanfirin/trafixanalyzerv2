package com.masterteknoloji.trafficanalyzer.web.rest.vm.analyzeorderdetails;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "classes")
public class VehicleTypeVM {

	
//	Boolean car = true;
//	Boolean bus = true;
//	Boolean truck = true;
//	Boolean bike = true;
//	Boolean person = false;
	
	Boolean pedestrian = true;
	Boolean bicycle= true;
	Boolean animalDrivenCart= true;
	Boolean motorCycleScooter= true;
	Boolean rickshaw= true;
	Boolean qingqi= true;
	Boolean carTaxi= true;
	Boolean hiacewagon= true;
	Boolean mediumBus= true;
	Boolean busSpeedo= true;
	Boolean loaderPickup= true;
	Boolean loaderRickshaw= true;
	Boolean tractorTrolley= true;
	Boolean truck= true;
	Boolean other= true;
	
	@JsonProperty("Bicycle")
	public Boolean getBicycle() {
		return bicycle;
	}
	public void setBicycle(Boolean bicycle) {
		this.bicycle = bicycle;
	}
	
	@JsonProperty("Animal Driven Cart")
	public Boolean getAnimalDrivenCart() {
		return animalDrivenCart;
	}
	public void setAnimalDrivenCart(Boolean animalDrivenCart) {
		this.animalDrivenCart = animalDrivenCart;
	}
	
	@JsonProperty("MotorCycle/Scooter")
	public Boolean getMotorCycleScooter() {
		return motorCycleScooter;
	}
	public void setMotorCycleScooter(Boolean motorCycleScooter) {
		this.motorCycleScooter = motorCycleScooter;
	}
	
	@JsonProperty("Rickshaw")
	public Boolean getRickshaw() {
		return rickshaw;
	}
	public void setRickshaw(Boolean rickshaw) {
		this.rickshaw = rickshaw;
	}
	
	@JsonProperty("Qingqi")
	public Boolean getQingqi() {
		return qingqi;
	}
	public void setQingqi(Boolean qingqi) {
		this.qingqi = qingqi;
	}
	
	@JsonProperty("Car/Taxi")
	public Boolean getCarTaxi() {
		return carTaxi;
	}
	public void setCarTaxi(Boolean carTaxi) {
		this.carTaxi = carTaxi;
	}
	
	@JsonProperty("Hiace Wagon")
	public Boolean getHiacewagon() {
		return hiacewagon;
	}
	public void setHiacewagon(Boolean hiacewagon) {
		this.hiacewagon = hiacewagon;
	}
	
	@JsonProperty("Medium Bus")
	public Boolean getMediumBus() {
		return mediumBus;
	}
	public void setMediumBus(Boolean mediumBus) {
		this.mediumBus = mediumBus;
	}
	
	@JsonProperty("Bus/Speedo")
	public Boolean getBusSpeedo() {
		return busSpeedo;
	}
	public void setBusSpeedo(Boolean busSpeedo) {
		this.busSpeedo = busSpeedo;
	}
	
	@JsonProperty("Loader Pickup")
	public Boolean getLoaderPickup() {
		return loaderPickup;
	}
	public void setLoaderPickup(Boolean loaderPickup) {
		this.loaderPickup = loaderPickup;
	}
	
	@JsonProperty("Loader Rickshaw")
	public Boolean getLoaderRickshaw() {
		return loaderRickshaw;
	}
	public void setLoaderRickshaw(Boolean loaderRickshaw) {
		this.loaderRickshaw = loaderRickshaw;
	}
	
	@JsonProperty("Tractor/Trolley")
	public Boolean getTractorTrolley() {
		return tractorTrolley;
	}
	public void setTractorTrolley(Boolean tractorTrolley) {
		this.tractorTrolley = tractorTrolley;
	}
	
	@JsonProperty("Truck")
	public Boolean getTruck() {
		return truck;
	}
	public void setTruck(Boolean truck) {
		this.truck = truck;
	}
	
	@JsonProperty("other")
	public Boolean getOther() {
		return other;
	}
	public void setOther(Boolean other) {
		this.other = other;
	}
	@JsonProperty("pedestrian")
	public Boolean getPedestrian() {
		return pedestrian;
	}
	public void setPedestrian(Boolean pedestrian) {
		this.pedestrian = pedestrian;
	}
}
