package com.masterteknoloji.trafficanalyzer.repository;

import com.masterteknoloji.trafficanalyzer.domain.Scenario;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Scenario entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ScenarioRepository extends JpaRepository<Scenario, Long> {

}
