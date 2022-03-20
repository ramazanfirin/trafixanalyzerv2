package com.masterteknoloji.trafficanalyzer.repository;

import com.masterteknoloji.trafficanalyzer.domain.Video;
import com.masterteknoloji.trafficanalyzer.domain.VideoRecord;
import org.springframework.stereotype.Repository;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;


/**
 * Spring Data JPA repository for the VideoRecord entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VideoRecordRepository extends JpaRepository<VideoRecord, Long> {
	 
	  @Query(value="Select \n"
	  		+ "vehicle_type as type, \n"
	  		+ "from_unixtime(FLOOR(UNIX_TIMESTAMP(insert_date)/(:interval*60))*(:interval*60)) GroupTime,\n"
	  		+ "line.name as line,\n"
	  		+ "COUNT(*) as counts\n"
	  		+ "FROM video_record i\n"
	  		+ "INNER JOIN line as line ON i.line_id=line.id\n"
	  		+ "where i.analyze_id= :analyzeOrderId\n"
	  		+ "GROUP BY GroupTime,vehicle_type,line.name "
	  		+ "order by GroupTime",nativeQuery=true)
		public  Iterable<Map<String,Object>> getResultOfOrderReport(@Param("analyzeOrderId") Long analyzeOrderId,@Param("interval") Long interval);
	  
	  @Query(value="Select \n"
		  		+ "vehicle_type as type, \n"
		  		+ "from_unixtime(FLOOR(UNIX_TIMESTAMP(insert_date)/(:interval*60))*(:interval*60)) GroupTime,\n"
		  		+ "line.name as line,\n"
		  		+ "COUNT(*) as counts\n"
		  		+ "FROM video_record i\n"
		  		+ "INNER JOIN direction as line ON i.direction_id=line.id\n"
		  		+ "where i.analyze_id= :analyzeOrderId\n"
		  		+ "GROUP BY GroupTime,vehicle_type,line.name "
		  		+ "order by GroupTime",nativeQuery=true)
			public  Iterable<Map<String,Object>> getResultOfOrderReportForDirection(@Param("analyzeOrderId") Long analyzeOrderId,@Param("interval") Long interval);
	  
	  @Query(value="Select \n"
		  		+ "vehicle_type as type, \n"
		  		+ "from_unixtime(FLOOR(UNIX_TIMESTAMP(insert_date)/(:interval*60))*(:interval*60)) GroupTime,\n"
		  		+ "line.name as line,\n"
		  		+ "COUNT(*) as counts\n"
		  		+ "FROM video_record i\n"
		  		+ "INNER JOIN line as line ON i.line_id=line.id\n"
		  		+ "where i.analyze_id= :analyzeOrderId and line.id = :lineId \n" 
		  		+ "GROUP BY GroupTime,vehicle_type,line.name "
		  		+ "order by GroupTime",nativeQuery=true)
	  public  Iterable<Map<String,Object>> getResultOfOrderReport(@Param("analyzeOrderId") Long analyzeOrderId,@Param("interval") Long interval,@Param("lineId") Long lineId);
//	  
	  @Query(value="Select \n"
		  		+ "vehicle_type as type, \n"
		  		+ "from_unixtime(FLOOR(UNIX_TIMESTAMP(insert_date)/(:interval*60))*(:interval*60)) GroupTime,\n"
		  		+ "line.name as line,\n"
		  		+ "COUNT(*) as counts\n"
		  		+ "FROM video_record i\n"
		  		+ "INNER JOIN direction as line ON i.direction_id=line.id\n"
		  		+ "where i.analyze_id= :analyzeOrderId and line.id = :directionId \n" 
		  		+ "GROUP BY GroupTime,vehicle_type,line.name "
		  		+ "order by GroupTime",nativeQuery=true)
	  public  Iterable<Map<String,Object>> getResultOfOrderReportForDirection(@Param("analyzeOrderId") Long analyzeOrderId,@Param("interval") Long interval,@Param("directionId") Long directionId);
	  
	  @Query(value="SELECT line.name as name,vehicle_type,count(*) as count FROM video_record "
	  		     + "INNER JOIN line as line ON line_id=line.id "
	  		     + "where "
	  		     + "analyze_id=:analyzeOrderId "
	  		     + "group by name,vehicle_type;",nativeQuery=true)
	  public  Iterable<Map<String,Object>> getVehicleTypeGroups(@Param("analyzeOrderId") Long analyzeOrderId);
	  
	  @Query(value="SELECT direction.name as name,vehicle_type,count(*) as count FROM video_record\n"
	  		+ "INNER JOIN direction as direction ON direction_id=direction.id\n"
	  		+ "where\n"
	  		+ "analyze_id = :analyzeOrderId "
	  		+ "group by name,vehicle_type",nativeQuery=true)
	  public  Iterable<Map<String,Object>> getVehicleTypeGroupsForDirection(@Param("analyzeOrderId") Long analyzeOrderId);
	  
	  @Query(value="SELECT line.name as lineName,avg(speed) as speed FROM video_record "
	  			 + "INNER JOIN line as line ON line_id=line.id "
	  			 + "where analyze_id=:analyzeOrderId  "
	  			 + "group by line.name;",nativeQuery=true)
	  public  Iterable<Map<String,Object>> getAverageSpeed(@Param("analyzeOrderId") Long analyzeOrderId);
	  
	  @Query(value="Select "
		  		+ "vehicle_type as type, \n"
		  		+ "duration as duration,\n"
		  		+ "speed as speed,\n"
		  		+ "line_id as line \n"
		  		+ "FROM video_record i\n"
		  		+ "where i.analyze_id= :analyzeOrderId\n"
		  		+ "order by id",nativeQuery=true)
	 public  Iterable<Map<String,Object>> getVisulationData(@Param("analyzeOrderId") Long analyzeOrderId);
	  
	  @Query(value="Select "
		  		+ "vehicle_type as type, \n"
		  		+ "duration as duration,\n"
		  		+ "speed as speed,\n"
		  		+ "direction.end_line_id as line, \n"
		  		+ "direction.name as directionname \n"
		  		+ "FROM video_record i\n"
		  		+ "INNER JOIN direction as direction ON i.direction_id=direction.id "
		  		+ "where i.analyze_id= :analyzeOrderId\n"
		  		+ "order by i.id",nativeQuery=true)
	 public  Iterable<Map<String,Object>> getVisulationDataForDirection(@Param("analyzeOrderId") Long analyzeOrderId); 

	 @Query(value="Select DISTINCT \n" + 
	 		"            startLine.name as startLineName,\n" + 
	 		"            endLine.name as endLineName,\n" +  
	 		"            direction.name as directionName "+
	 		"            ,(select count(*) from video_record where line_id =startLine.id) as startLineCount\n" + 
	 		"            ,(select count(*) from video_record where line_id =endLine.id) as endLineCount\n" + 
	 		"            ,(select count(*) from video_record where direction_id =direction.id) as count\n" +
	 		"            FROM video_record i\n" + 
	 		"	  		INNER JOIN direction as direction ON i.direction_id=direction.id\n" + 
	 		"            INNER JOIN line as startLine ON direction.start_line_id=startLine.id\n" + 
	 		"            INNER JOIN line as endLine ON direction.end_line_id=endLine.id\n" + 
	 		"	  		where i.analyze_id= :analyzeOrderId\n" + 
	 		"			order by direction.name",nativeQuery=true)
	 public  Iterable<Map<String,Object>> getDirectionReportData(@Param("analyzeOrderId") Long analyzeOrderId);

	 @Query(value="select direction.name,count(*) as count ,\n"
	 		+ "startLine.name as lineName\n"
	 		+ "from video_record i \n"
	 		+ "INNER JOIN direction as direction ON i.direction_id=direction.id\n"
	 		+ "INNER JOIN line as startLine ON direction.start_line_id=startLine.id \n"
	 		+ "INNER JOIN line as endLine ON direction.end_line_id=endLine.id\n"
	 		+ "where i.analyze_id=:analyzeOrderId\n"
	 		+ "group by lineName",nativeQuery=true)
	 Iterable<Map<String,Object>> getStartLineCrossCountForDirection(@Param("analyzeOrderId") Long analyzeOrderId);
	 
	 @Query(value="select direction.name,count(*) as count ,\n"
		 		+ "endLine.name as lineName\n"
		 		+ "from video_record i \n"
		 		+ "INNER JOIN direction as direction ON i.direction_id=direction.id\n"
		 		+ "INNER JOIN line as startLine ON direction.start_line_id=startLine.id \n"
		 		+ "INNER JOIN line as endLine ON direction.end_line_id=endLine.id\n"
		 		+ "where i.analyze_id=:analyzeOrderId\n"
		 		+ "group by lineName",nativeQuery=true)
	  Iterable<Map<String,Object>> getEndLineCrossCountForDirection(@Param("analyzeOrderId") Long analyzeOrderId);
	 

	 @Query(value="select direction.name,count(*) as count,\n"
	 		+ "startLine.name as startLineName,\n"
	 		+ "endLine.name as endLineName\n"
	 		+ "from video_record i \n"
	 		+ "INNER JOIN direction as direction ON i.direction_id=direction.id\n"
	 		+ "INNER JOIN line as startLine ON direction.start_line_id=startLine.id \n"
	 		+ "INNER JOIN line as endLine ON direction.end_line_id=endLine.id\n"
	 		+ "where i.analyze_id=:analyzeOrderId\n"
	 		+ "group by startLineName,endLineName",nativeQuery=true)
	  Iterable<Map<String,Object>> getDirectionCrossCountForDirection(@Param("analyzeOrderId") Long analyzeOrderId);
	 
	 @Query(value="select i.analyze_id,scenario.name as scenarioName, "
	 		+ "     direction.name as directionName,"
	 		+ "     video.id ,video.name as videoName ,"
	 		+ "     video.start_date as startdate,"
	 		+ "     video.end_date as enddate,"
	 		+ "     count(*) as count\n"
	 		+ "from video_record i\n"
	 		+ "INNER JOIN direction as direction ON i.direction_id=direction.id\n"
	 		+ "INNER JOIN analyze_order as analyze_order ON i.analyze_id=analyze_order.id\n"
	 		+ "INNER JOIN scenario as scenario ON analyze_order.scenario_id=scenario.id\n"
	 		+ "INNER JOIN video as video ON analyze_order.video_id=video.id\n"
	 		+ "where scenario.id=:scenarioId\n and video.start_date is not null and video.end_date is not null "
	 		+ "group by i.analyze_id,scenario.id,direction.name",nativeQuery=true)
	 Iterable<Map<String,Object>> getDirectionReportByScnario(@Param("scenarioId") Long scenarioId);
	 
	 @Query(value="select \n"
	 		+ "i.analyze_id,\n"
	 		+ "scenario.name as scenarioName, \n"
	 		+ "line.name as directionName,\n"
	 		+ "video.id ,video.name as videoName ,\n"
	 		+ "video.start_date as startdate,\n"
	 		+ "video.end_date as enddate,\n"
	 		+ "count(*) as count\n"
	 		+ "from video_record i\n"
	 		+ "INNER JOIN line as line ON i.line_id=line.id\n"
	 		+ "INNER JOIN analyze_order as analyze_order ON i.analyze_id=analyze_order.id\n"
	 		+ "INNER JOIN scenario as scenario ON analyze_order.scenario_id=scenario.id\n"
	 		+ "INNER JOIN video as video ON analyze_order.video_id=video.id\n"
	 		+ "where scenario.id=:scenarioId and video.start_date is not null and video.end_date is not null\n"
	 		+ "group by i.analyze_id,scenario.id,line.name\n"
	 		+ "order by i.analyze_id desc",nativeQuery=true)
		 Iterable<Map<String,Object>> getLineReportByScnario(@Param("scenarioId") Long scenarioId);
		 
}
