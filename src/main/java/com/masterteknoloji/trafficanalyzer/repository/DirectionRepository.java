package com.masterteknoloji.trafficanalyzer.repository;

import com.masterteknoloji.trafficanalyzer.domain.Direction;
import com.masterteknoloji.trafficanalyzer.domain.Line;

import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Direction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DirectionRepository extends JpaRepository<Direction, Long> {
	
	@Query("select v from Direction v where v.scenario.id = ?1")
	List<Direction> getDirectionListByScenarioId(Long id);

}
