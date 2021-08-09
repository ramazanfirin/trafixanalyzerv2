package com.masterteknoloji.trafficanalyzer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.masterteknoloji.trafficanalyzer.domain.AnalyzeOrder;
import com.masterteknoloji.trafficanalyzer.domain.enumeration.AnalyzeState;


/**
 * Spring Data JPA repository for the AnalyzeOrder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AnalyzeOrderRepository extends JpaRepository<AnalyzeOrder, Long> {

	@Query("select v from AnalyzeOrder v where v.orderDetails.state = ?1")
	List<AnalyzeOrder> findByState(AnalyzeState analyzeState);
}
