package com.masterteknoloji.trafficanalyzer.repository;

import com.masterteknoloji.trafficanalyzer.domain.Polygon;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Polygon entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PolygonRepository extends JpaRepository<Polygon, Long> {

}
