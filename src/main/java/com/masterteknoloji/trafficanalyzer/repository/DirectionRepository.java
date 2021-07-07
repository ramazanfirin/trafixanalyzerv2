package com.masterteknoloji.trafficanalyzer.repository;

import com.masterteknoloji.trafficanalyzer.domain.Direction;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Direction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DirectionRepository extends JpaRepository<Direction, Long> {

}
