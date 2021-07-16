package com.masterteknoloji.trafficanalyzer.repository;

import com.masterteknoloji.trafficanalyzer.domain.AnalyzeOrderDetails;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the AnalyzeOrderDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AnalyzeOrderDetailsRepository extends JpaRepository<AnalyzeOrderDetails, Long> {

}
