package com.masterteknoloji.trafficanalyzer.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.masterteknoloji.trafficanalyzer.domain.AnalyzeOrder;
import com.masterteknoloji.trafficanalyzer.domain.Scenario;
import com.masterteknoloji.trafficanalyzer.domain.enumeration.AnalyzeState;


/**
 * Spring Data JPA repository for the AnalyzeOrder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AnalyzeOrderRepository extends JpaRepository<AnalyzeOrder, Long> {

	@Query("select v from AnalyzeOrder v where v.orderDetails.state = ?1")
	List<AnalyzeOrder> findByState(AnalyzeState analyzeState);
	
	@Query("select v from AnalyzeOrder v where v.active=true")
	Page<AnalyzeOrder> getActiveItem(Pageable pageable);
}
