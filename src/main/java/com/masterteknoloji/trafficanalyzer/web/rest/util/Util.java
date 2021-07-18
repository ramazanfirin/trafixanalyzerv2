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
import com.masterteknoloji.trafficanalyzer.domain.Line;
import com.masterteknoloji.trafficanalyzer.domain.Polygon;
import com.masterteknoloji.trafficanalyzer.web.rest.vm.analyzeorderdetails.ConnectionVM;
import com.masterteknoloji.trafficanalyzer.web.rest.vm.analyzeorderdetails.DirectionVM;
import com.masterteknoloji.trafficanalyzer.web.rest.vm.analyzeorderdetails.PointsVM;
import com.masterteknoloji.trafficanalyzer.web.rest.vm.analyzeorderdetails.RegionVM;
import com.masterteknoloji.trafficanalyzer.web.rest.vm.analyzeorderdetails.SpeedVM;
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
	
	public static AnalyzeOrderDetails prepareAnalyzeOrderDetails(ObjectMapper objectMapper,String sessionId,String videoPath,List<Line> lineList,List<Polygon> speedPolygonList) throws Exception {
		AnalyzeOrderDetails analyzeOrderDetails = new AnalyzeOrderDetails();
		analyzeOrderDetails.setClasses(objectMapper.writeValueAsString(new VehicleTypeVM()));
		analyzeOrderDetails.setCount(true);
		analyzeOrderDetails.setDirections(prepareDirections(objectMapper, lineList));
		analyzeOrderDetails.setSessionId(sessionId);
		analyzeOrderDetails.setSpeed(prepareSpeeds(objectMapper, speedPolygonList));
		analyzeOrderDetails.setVideoPath(videoPath);
		
		return analyzeOrderDetails;
	}
	
	public static String prepareDirections(ObjectMapper objectMapper,List<Line> lineList) throws Exception {
		DirectionVM directionVM = new DirectionVM();
		
		
		for (Iterator iterator = lineList.iterator(); iterator.hasNext();) {
			Line line = (Line) iterator.next();
			
			RegionVM startRegionVM = prepareRegions(line.getStartPolygon());
			directionVM.getRegions().add(startRegionVM);
			
			RegionVM endRegionVM = prepareRegions(line.getEndPolygon());
			directionVM.getRegions().add(endRegionVM);
			
			directionVM.getConnections().add(prepareConnections(line));
			
		}
		
		return objectMapper.writeValueAsString(directionVM);
	}
	
	public static String prepareSpeeds(ObjectMapper objectMapper,List<Polygon> speedPolygonList) throws Exception {
		List<SpeedVM> result = new ArrayList<SpeedVM>();
		
		for (Iterator iterator = speedPolygonList.iterator(); iterator.hasNext();) {
			Polygon polygon = (Polygon) iterator.next();
			
			SpeedVM speedVM = new SpeedVM();
			List<Point> pointList = convertToPointList(polygon.getPoints());
	    	for (Iterator iterator2 = pointList.iterator(); iterator2.hasNext();) {
				Point point = (Point) iterator2.next();
				speedVM.getPoints().add(preparePoints((new Double(point.getX())).longValue(), (new Double(point.getY())).longValue()));
			}
			
	    	speedVM.setDistance(polygon.getWidth().longValue());
	    	speedVM.setLabel(polygon.getId().toString());
	    	
	    	result.add(speedVM);
		}
		
		return objectMapper.writeValueAsString(result);
	}
	
	public static RegionVM prepareRegions(Polygon polygon) {
		RegionVM regionVM = new RegionVM();
    	regionVM.setLabel(polygon.getId().toString());
    	
    	List<Point> pointList = convertToPointList(polygon.getPoints());
    	for (Iterator iterator = pointList.iterator(); iterator.hasNext();) {
			Point point = (Point) iterator.next();
			regionVM.getPoints().add(preparePoints((new Double(point.getX())).longValue(), (new Double(point.getY())).longValue()));
		}
    	
    	return regionVM;
	}
	
	public static ConnectionVM prepareConnections(Line line) {
		ConnectionVM connectionVM = new ConnectionVM();
		connectionVM.setEntry(line.getStartPolygon().getId().toString());
		connectionVM.setExit(line.getEndPolygon().getId().toString());
    	
		return connectionVM;
	}
	
	 public static PointsVM preparePoints(Long x,Long y) {
	    	PointsVM pointsVM2 = new PointsVM();
	    	pointsVM2.setX(x);
	    	pointsVM2.setY(y);
	    	return pointsVM2;
	    } 
	
}
