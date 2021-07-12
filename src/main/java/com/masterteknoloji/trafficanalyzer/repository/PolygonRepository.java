package com.masterteknoloji.trafficanalyzer.repository;

import com.masterteknoloji.trafficanalyzer.domain.Polygon;
import com.masterteknoloji.trafficanalyzer.domain.enumeration.PolygonType;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Polygon entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PolygonRepository extends JpaRepository<Polygon, Long> {
	 @Query("select v from Polygon v where v.scenario.id = ?1 and v.type = ?2")
	 List<Polygon> getPolygonListByScenarioId(Long id,PolygonType type);
}
