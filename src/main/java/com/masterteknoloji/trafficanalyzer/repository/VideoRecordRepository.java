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
}
