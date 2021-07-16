package com.masterteknoloji.trafficanalyzer.repository;

import com.masterteknoloji.trafficanalyzer.domain.AnalyzeOrder;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the AnalyzeOrder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AnalyzeOrderRepository extends JpaRepository<AnalyzeOrder, Long> {

}
