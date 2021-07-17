package com.masterteknoloji.trafficanalyzer.web.rest.util;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.masterteknoloji.trafficanalyzer.domain.AnalyzeOrder;
import com.masterteknoloji.trafficanalyzer.domain.AnalyzeOrderDetails;
import com.masterteknoloji.trafficanalyzer.web.rest.vm.analyzeorderdetails.ConnectionVM;
import com.masterteknoloji.trafficanalyzer.web.rest.vm.analyzeorderdetails.DirectionVM;
import com.masterteknoloji.trafficanalyzer.web.rest.vm.analyzeorderdetails.PointsVM;
import com.masterteknoloji.trafficanalyzer.web.rest.vm.analyzeorderdetails.RegionVM;
import com.masterteknoloji.trafficanalyzer.web.rest.vm.analyzeorderdetails.VehicleTypeVM;

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
	
	public static ByteArrayOutputStream getScreenshotOfVideo(String filePath) throws IOException {
		File file = new File(filePath);
    	file.exists();
    	
    	
    	FFmpegFrameGrabber g = new FFmpegFrameGrabber(filePath);
		g.start();

		Java2DFrameConverter bimConverter = new Java2DFrameConverter();
		
		BufferedImage image=null;
		for (int i = 0 ; i < 5 ; i++) {
		    
		Frame f =	g.grabFrame();
			image = bimConverter.convert(f);
			
		}

		
		
		g.stop();
		
		bimConverter.close();
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		  ImageIO.write(image, "jpg", baos);
		  
		return  baos;
	}
	
	public static AnalyzeOrderDetails prepareAnalyzeOrderDetails(ObjectMapper objectMapper,AnalyzeOrder analyzeOrder) throws JsonProcessingException {
		AnalyzeOrderDetails analyzeOrderDetails = new AnalyzeOrderDetails();
		analyzeOrderDetails.setClasses(objectMapper.writeValueAsString(new VehicleTypeVM()));
		analyzeOrderDetails.setCount(true);
		analyzeOrderDetails.setDirections(null);
		analyzeOrderDetails.setSessionId(analyzeOrder.getId().toString());
		analyzeOrderDetails.setSpeed(null);
		analyzeOrderDetails.setVideoPath(analyzeOrder.getVideo().getPath());
		
		return analyzeOrderDetails;
	}
	
	public static String prepareDirections(ObjectMapper objectMapper,AnalyzeOrder analyzeOrder) {
		DirectionVM directionVM = new DirectionVM();
		
		List<RegionVM> regionList = new ArrayList<RegionVM>();
//		analyzeOrder.getScenario().
//		directionVM.setRegions(regionList);
		
		List<ConnectionVM> connectionVMs = new  ArrayList<ConnectionVM>();
		directionVM.setConnections(connectionVMs);
		return null;
	}
	
	public static RegionVM prepareRegions(String label) {
		RegionVM regionVM = new RegionVM();
    	regionVM.setLabel("regions1");
    	
    	regionVM.getPoints().add(preparePoints(100l, 100l));
    	regionVM.getPoints().add(preparePoints(200l,200l));
    	
    	return regionVM;
	}
	
	 public static PointsVM preparePoints(Long x,Long y) {
	    	PointsVM pointsVM2 = new PointsVM();
	    	pointsVM2.setX(x);
	    	pointsVM2.setY(y);
	    	return pointsVM2;
	    } 
	public static String prepareSpeed(ObjectMapper objectMapper,AnalyzeOrder analyzeOrder) {
		return null;
	}
}
