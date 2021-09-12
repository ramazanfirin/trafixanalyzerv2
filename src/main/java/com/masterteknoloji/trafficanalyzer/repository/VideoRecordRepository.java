package com.masterteknoloji.trafficanalyzer.repository;

import com.masterteknoloji.trafficanalyzer.domain.VideoRecord;
import org.springframework.stereotype.Repository;

import java.util.Map;

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
	  		+ "from_unixtime(FLOOR(UNIX_TIMESTAMP(insert_date)/(1*60))*(1*60)) GroupTime,\n"
	  		+ "line.name as line,\n"
	  		+ "COUNT(*) as counts\n"
	  		+ "FROM video_record i\n"
	  		+ "INNER JOIN line as line ON i.line_id=line.id\n"
	  		+ "where i.analyze_id= :analyzeOrderId\n"
	  		+ "GROUP BY GroupTime,vehicle_type,line.name "
	  		+ "order by line.name",nativeQuery=true)
		public  Iterable<Map<String,Object>> getResultOfOrderReport(@Param("analyzeOrderId") Long analyzeOrderId);
	  
	  @Query(value="Select \n"
		  		+ "vehicle_type as type, \n"
		  		+ "from_unixtime(FLOOR(UNIX_TIMESTAMP(insert_date)/(1*60))*(1*60)) GroupTime,\n"
		  		+ "line.name as line,\n"
		  		+ "COUNT(*) as counts\n"
		  		+ "FROM video_record i\n"
		  		+ "INNER JOIN line as line ON i.line_id=line.id\n"
		  		+ "where i.analyze_id= :analyzeOrderId and line.id = :lineId \n" 
		  		+ "GROUP BY GroupTime,vehicle_type,line.name "
		  		+ "order by line.name",nativeQuery=true)
	  public  Iterable<Map<String,Object>> getResultOfOrderReport(@Param("analyzeOrderId") Long analyzeOrderId,@Param("lineId") Long lineId);
	  
	  @Query(value="SELECT line.name as lineName,vehicle_type,count(*) as count FROM video_record "
	  		     + "INNER JOIN line as line ON line_id=line.id "
	  		     + "where "
	  		     + "analyze_id=:analyzeOrderId "
	  		     + "group by line.name,vehicle_type;",nativeQuery=true)
	  public  Iterable<Map<String,Object>> getVehicleTypeGroups(@Param("analyzeOrderId") Long analyzeOrderId);
	  
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


}
