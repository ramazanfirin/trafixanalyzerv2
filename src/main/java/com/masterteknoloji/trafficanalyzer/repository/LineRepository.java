package com.masterteknoloji.trafficanalyzer.repository;

import com.masterteknoloji.trafficanalyzer.domain.Line;
import com.masterteknoloji.trafficanalyzer.domain.Polygon;

import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Line entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LineRepository extends JpaRepository<Line, Long> {
     @Query("select v from Line v where v.scenario.id = ?1")
	 List<Line> getLineListByScenarioId(Long id);
     
     @Query("select v from Line v where v.startPolygon.id = ?1 or v.endPolygon.id = ?1")
	 List<Line> getLineListByPolygonId(Long id);
}
