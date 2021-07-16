package com.masterteknoloji.trafficanalyzer.web.rest.util;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.springframework.util.StringUtils;

import com.masterteknoloji.trafficanalyzer.domain.Polygon;

public class Util {
	
	public static List<Point> convertToPointList(String  points){
		
		List<Point>  result = new ArrayList<Point>();
		if(StringUtils.isEmpty(points))
			return result;
		
		String[] p1Points = points.split(";");
		
		for (int i = 0; i < p1Points.length; i++) {
			String[] p1Point = p1Points[i].split(",");
			int x1 = Integer.parseInt(p1Point[0]);
			int y1 = Integer.parseInt(p1Point[1]);
			result.add(new Point(x1, y1));
		}
		
		return result;
	}
	
	public static Point findCentroid(List<Point> points) {
	    int x = 0;
	    int y = 0;
	    for (Point p : points) {
	        x += p.x;
	        y += p.y;
	    }
	    Point center = new Point(0, 0);
	    center.x = x / points.size();
	    center.y = y / points.size();
	    return center;
	}
	
	private static List<Point> sortPointClockwise(List<Point> calculatedPoints) {
		
		Point center = findCentroid(calculatedPoints);
		
		Collections.sort(calculatedPoints, (a, b) -> {
            double a1 = (Math.toDegrees(Math.atan2(a.x - center.x, a.y - center.y)) + 360) % 360;
            double a2 = (Math.toDegrees(Math.atan2(b.x - center.x, b.y - center.y)) + 360) % 360;
            return (int) (a1 - a2);
        });
		return calculatedPoints;
	}
	
	public static String sortPoygonPoints(String points) {
		
		String result = "";
		
		List<Point> pointList = convertToPointList(points);
		sortPointClockwise(pointList);
		
		for (Iterator iterator = pointList.iterator(); iterator.hasNext();) {
			Point point = (Point) iterator.next();
			if(StringUtils.isEmpty(result))
				result = point.getX()+","+point.getY();
			else
				result = result+";"+point.getX()+","+point.getY();
			
        }
		
		return result;
	}
	
	public static String sortPoygonPoints(List<Point> pointList) {
		
		String result = "";
		
		sortPointClockwise(pointList);
		
		for (Iterator iterator = pointList.iterator(); iterator.hasNext();) {
			Point point = (Point) iterator.next();
			if(StringUtils.isEmpty(result))
				result = point.getX()+","+point.getY();
			else
				result = result+";"+point.getX()+","+point.getY();
			
        }
		
		return result;
	}
}
