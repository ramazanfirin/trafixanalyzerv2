package com.masterteknoloji.trafficanalyzer.web.rest.vm;

import java.math.BigInteger;

public class DirectionReportDetail {

	String directionName;
	BigInteger count;
	public String getDirectionName() {
		return directionName;
	}
	public void setDirectionName(String directionName) {
		this.directionName = directionName;
	}
	public BigInteger getCount() {
		return count;
	}
	public void setCount(BigInteger count) {
		this.count = count;
	}
}
