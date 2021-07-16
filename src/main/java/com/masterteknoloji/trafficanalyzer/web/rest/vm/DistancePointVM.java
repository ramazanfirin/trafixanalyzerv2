package com.masterteknoloji.trafficanalyzer.web.rest.vm;

import java.awt.Point;

public class DistancePointVM implements Comparable<DistancePointVM>{
	
	Double distance;
	
	Point point;

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
	}

	@Override
	public int compareTo(DistancePointVM o) {
		return (int)(this.distance - o.getDistance());
	}
	
	
}
