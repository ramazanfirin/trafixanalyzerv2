package com.masterteknoloji.trafficanalyzer.web.rest.vm;

import java.math.BigInteger;

public class VideoRecordSummaryVM {

	Object date;
	String type;
	BigInteger count;
	String videoName;
	String directionName;
	
	public VideoRecordSummaryVM() {
		super();
		// TODO Auto-generated constructor stub
	}

	public VideoRecordSummaryVM(Object date, String type, BigInteger count) {
		super();
		this.date = date;
		this.type = type;
		this.count = count;
	}

	public VideoRecordSummaryVM(BigInteger count, String videoName, String directionName) {
		super();
		this.count = count;
		this.videoName = videoName;
		this.directionName = directionName;
	}


	public Object getDate() {
		return date;
	}

	public void setDate(Object date) {
		this.date = date;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public BigInteger getCount() {
		return count;
	}

	public void setCount(BigInteger count) {
		this.count = count;
	}

	public String getVideoName() {
		return videoName;
	}

	public void setVideoName(String videoName) {
		this.videoName = videoName;
	}

	public String getDirectionName() {
		return directionName;
	}

	public void setDirectionName(String directionName) {
		this.directionName = directionName;
	}
	
		
}
