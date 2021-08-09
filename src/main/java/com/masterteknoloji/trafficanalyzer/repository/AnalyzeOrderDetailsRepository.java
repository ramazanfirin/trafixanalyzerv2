package com.masterteknoloji.trafficanalyzer.repository;

import com.masterteknoloji.trafficanalyzer.domain.AnalyzeOrder;
import com.masterteknoloji.trafficanalyzer.domain.AnalyzeOrderDetails;
import com.masterteknoloji.trafficanalyzer.domain.enumeration.AnalyzeState;

import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the AnalyzeOrderDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AnalyzeOrderDetailsRepository extends JpaRepository<AnalyzeOrderDetails, Long> {

	@Query("select v from AnalyzeOrderDetails v where v.sessionId = ?1")
	List<AnalyzeOrderDetails> findBySessionId(String sessionId);
	
	@Query("select v from AnalyzeOrderDetails v where v.state = ?1")
	List<AnalyzeOrderDetails> findByState(AnalyzeState analyzeState);
}
