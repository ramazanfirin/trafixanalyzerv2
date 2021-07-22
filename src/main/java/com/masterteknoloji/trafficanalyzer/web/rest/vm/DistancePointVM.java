package com.masterteknoloji.trafficanalyzer.web.rest.vm;

import java.awt.Point;

public class DistancePointVM implements Comparable<DistancePointVM>{
	
	Double distance;
	
	Point startPoint;

	Point endPoint;
	
	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	@Override
	public int compareTo(DistancePointVM o) {
		return (int)(this.distance - o.getDistance());
	}

	public Point getStartPoint() {
		return startPoint;
	}

	public void setStartPoint(Point startPoint) {
		this.startPoint = startPoint;
	}

	public Point getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(Point endPoint) {
		this.endPoint = endPoint;
	}
	
	
}
