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
import java.util.Random;

import javax.imageio.ImageIO;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.masterteknoloji.trafficanalyzer.domain.AnalyzeOrderDetails;
import com.masterteknoloji.trafficanalyzer.domain.Direction;
import com.masterteknoloji.trafficanalyzer.domain.Line;
import com.masterteknoloji.trafficanalyzer.domain.Polygon;
import com.masterteknoloji.trafficanalyzer.domain.enumeration.AnalyzeState;
import com.masterteknoloji.trafficanalyzer.domain.enumeration.PolygonDirectionType;
import com.masterteknoloji.trafficanalyzer.service.util.RandomUtil;
import com.masterteknoloji.trafficanalyzer.web.rest.vm.analyzeorderdetails.AnalyzeOrderDetailVM;
import com.masterteknoloji.trafficanalyzer.web.rest.vm.analyzeorderdetails.ConnectionVM;
import com.masterteknoloji.trafficanalyzer.web.rest.vm.analyzeorderdetails.DirectionVM;
import com.masterteknoloji.trafficanalyzer.web.rest.vm.analyzeorderdetails.PointsVM;
import com.masterteknoloji.trafficanalyzer.web.rest.vm.analyzeorderdetails.RegionVM;
import com.masterteknoloji.trafficanalyzer.web.rest.vm.analyzeorderdetails.SpeedVM;
import com.masterteknoloji.trafficanalyzer.web.rest.vm.analyzeorderdetails.VehicleTypeVM;

public class Util {
	
	public static Double multiplierOnScreen = 13d; // width = 64x13 = 832, heigth= 36x13= 468 
	
	public static Double multiplierOnAnalyze = 20d; // width = 64x20 = 1280, heigth= 36x13= 720
	
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
    	if(!file.exists())
    		throw new RuntimeException("file not found :"+ filePath);	
    	
    	
    	FFmpegFrameGrabber g;
		try {
			g = new FFmpegFrameGrabber(filePath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);	
			
		}
		g.start();

		Java2DFrameConverter bimConverter = new Java2DFrameConverter();
		
		BufferedImage image=null;
		for (int i = 0 ; i < 5 ; i++) {
		    
		Frame f =	g.grabFrame();
			image = bimConverter.convert(f);
			
		}

		System.out.println(image.getWidth());
		System.out.println(image.getHeight());
		
		g.stop();
		
		//bimConverter.close();
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		  ImageIO.write(image, "jpg", baos);
		  
		return  baos;
	}
	
	public static AnalyzeOrderDetails prepareAnalyzeOrderDetails(ObjectMapper objectMapper,String sessionId,String videoPath,List<Line> lineList,List<Direction> directionList,
			List<Polygon> speedPolygonList,Boolean showVisulationWindow,String videoType) throws Exception {
		AnalyzeOrderDetails result = new AnalyzeOrderDetails();
		
		VehicleTypeVM vehicleTypeVM = new VehicleTypeVM();
		result.setClasses(objectMapper.writeValueAsString(vehicleTypeVM));
		
		DirectionVM directionVM = prepareDirections(objectMapper, lineList,directionList);
		result.setDirections(objectMapper.writeValueAsString(directionVM));
		
		List<SpeedVM> speedVMs = prepareSpeeds(objectMapper, speedPolygonList);
		result.setSpeed(objectMapper.writeValueAsString(speedVMs));
		
		result.setCount(true);
		//result.setStartDate(Instant.now());
		result.setSessionId(sessionId);
		result.setVideoPath(videoPath);
		result.setState(AnalyzeState.NOT_STARTED);
		result.setShowVisulationWindow(showVisulationWindow);
		
		AnalyzeOrderDetailVM analyzeOrderDetailVM = prepareAllData(vehicleTypeVM, directionVM, speedVMs, videoPath, sessionId,showVisulationWindow,videoType);
		result.setJsonData(objectMapper.writeValueAsString(analyzeOrderDetailVM));
		
		return result;
	}
	
	public static AnalyzeOrderDetailVM prepareAllData(VehicleTypeVM vehicleTypeVM,DirectionVM directionVM,List<SpeedVM> speed,String path,String sessionId,Boolean showVisulationWindow,String videoType) {
		AnalyzeOrderDetailVM analyzeOrderDetailVM = new AnalyzeOrderDetailVM();
		analyzeOrderDetailVM.setCount(true);
		analyzeOrderDetailVM.setDirections(directionVM);
		analyzeOrderDetailVM.setPath(path);
		analyzeOrderDetailVM.setSessionId(sessionId);
		analyzeOrderDetailVM.setSpeed(speed);
		analyzeOrderDetailVM.setClasses(vehicleTypeVM);
		analyzeOrderDetailVM.setShowVisulationWindow(showVisulationWindow);
		analyzeOrderDetailVM.setVideoType(videoType);
		
		return analyzeOrderDetailVM;
	}
	
	public static void addToRegionsList(DirectionVM directionVM,RegionVM regionVM) {
		for (Iterator iterator = directionVM.getRegions().iterator(); iterator.hasNext();) {
			RegionVM type = (RegionVM) iterator.next();
			if(type.getLabel().equals(regionVM.getLabel()))
				return;
		}
		
		directionVM.getRegions().add(regionVM);
	}
	
	public static DirectionVM prepareDirections(ObjectMapper objectMapper,List<Line> lineList,List<Direction> directionList) throws Exception {
		DirectionVM directionVM = new DirectionVM();
		
		for (Iterator iterator = directionList.iterator(); iterator.hasNext();) {
			Direction direction = (Direction) iterator.next();
			
			RegionVM startRegionVM = prepareRegions(direction.getStartLine(). getEndPolygon(),null,PolygonDirectionType.ENTRY.toString());
			addToRegionsList(directionVM,startRegionVM);
			
			RegionVM endRegionVM = prepareRegions(direction.getEndLine(). getStartPolygon(),null,PolygonDirectionType.EXIT.toString());
			addToRegionsList(directionVM,endRegionVM);
			
			directionVM.getConnections().add(prepareConnections(direction));
		}
		
		for (Iterator iterator = lineList.iterator(); iterator.hasNext();) {
			Line line = (Line) iterator.next();
		    if(isUsedInDirections(directionList,line))
		    	continue;
		    
			RegionVM startRegionVM = prepareRegions(line.getStartPolygon(),null,PolygonDirectionType.ENTRY.toString());
			directionVM.getRegions().add(startRegionVM);
			
			RegionVM endRegionVM = prepareRegions(line.getEndPolygon(),null,PolygonDirectionType.EXIT.toString());
			directionVM.getRegions().add(endRegionVM);
			
			directionVM.getConnections().add(prepareConnections(line));
			
		}
		
		
		
		
		
		return directionVM;
	}
	
	public static Boolean isUsedInDirections(List<Direction> directionList,Line line) {
		for (Iterator iterator = directionList.iterator(); iterator.hasNext();) {
			Direction direction = (Direction) iterator.next();
			if(direction.getStartLine().getId().longValue() == line.getId() || direction.getEndLine().getId().longValue() == line.getId()) {
				return true;
			}
		}
		return false;
	}
	
	public static List<SpeedVM> prepareSpeeds(ObjectMapper objectMapper,List<Polygon> speedPolygonList) throws Exception {
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
		
		return result;
	}
	
	public static RegionVM prepareRegions(Polygon polygon,String randomId,String type) {
		RegionVM regionVM = new RegionVM();
    	if(randomId == null)
    		regionVM.setLabel(polygon.getId().toString());
    	else
    		regionVM.setLabel(polygon.getId().toString()+"-"+randomId);
        
    		
    	List<Point> pointList = convertToPointList(polygon.getPoints());
    	for (Iterator iterator = pointList.iterator(); iterator.hasNext();) {
			Point point = (Point) iterator.next();
			regionVM.getPoints().add(preparePoints((new Double(point.getX())).longValue(), (new Double(point.getY())).longValue()));
		}
    	
    	regionVM.setType(type);
    	return regionVM;
	}
	
	public static ConnectionVM prepareConnections(Line line) {
		ConnectionVM connectionVM = new ConnectionVM();
		connectionVM.setEntry(line.getStartPolygon().getId().toString());
		connectionVM.setExit(line.getEndPolygon().getId().toString());
    	
		return connectionVM;
	}
	
	public static ConnectionVM prepareConnections(Direction direction) {
		ConnectionVM connectionVM = new ConnectionVM();
		connectionVM.setEntry(direction.getStartLine().getEndPolygon().getId().toString());
		connectionVM.setExit(direction.getEndLine().getStartPolygon().getId().toString());
    	
		return connectionVM;
	}
	
	 public static PointsVM preparePoints(Long x,Long y) {
	    	PointsVM pointsVM2 = new PointsVM();
	    	pointsVM2.setX(convertCoordinate(x));
	    	pointsVM2.setY(convertCoordinate(y));
	    	return pointsVM2;
	    } 
	
	public static Long convertCoordinate(Long value) {
		
		Double result = (value/multiplierOnScreen) * multiplierOnAnalyze;
		
		return result.longValue();
	}
	
	public static Long calculatePertencile(Long total,Long part) {
		Long result = (100 * part)/total;
		return result ;
	}
}
