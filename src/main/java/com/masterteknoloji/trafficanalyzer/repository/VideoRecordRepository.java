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
	  		+ "FROM trafficanalyzer3.video_record i\n"
	  		+ "INNER JOIN trafficanalyzer3.line as line ON i.line_id=line.id\n"
	  		+ "where i.analyze_id= :analyzeOrderId\n"
	  		+ "GROUP BY GroupTime,vehicle_type,line.name "
	  		+ "order by line.name",nativeQuery=true)
		public  Iterable<Map<String,Object>> getResultOfOrderReport(@Param("analyzeOrderId") Long analyzeOrderId);
}
