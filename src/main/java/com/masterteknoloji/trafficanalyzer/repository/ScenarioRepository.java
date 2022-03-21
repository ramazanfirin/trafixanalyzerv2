package com.masterteknoloji.trafficanalyzer.repository;

import com.masterteknoloji.trafficanalyzer.domain.Scenario;
import com.masterteknoloji.trafficanalyzer.domain.Video;

import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Scenario entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ScenarioRepository extends JpaRepository<Scenario, Long> {
	@Query("select v from Scenario v where v.active=true")
	Page<Scenario> getActiveItem(Pageable pageable);
	
	List<Scenario> findByNameContaining(String name);
	
	@Query("select v from Scenario v where v.video.location.id= ?1")
	List<Scenario> findScenarioListByLocationId(Long locationId);
}
