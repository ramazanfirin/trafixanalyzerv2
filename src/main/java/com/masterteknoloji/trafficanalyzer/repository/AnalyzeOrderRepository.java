package com.masterteknoloji.trafficanalyzer.repository;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
	
	@Query("select v from AnalyzeOrder v where v.active=true")
	Page<AnalyzeOrder> getActiveItem(Pageable pageable);
	
	@Query("select v from AnalyzeOrder v where v.active=true  and v.video.location.id=?1 and v.video.startDate>?2 and v.video.endDate<?3 order by v.id desc ")
	List<AnalyzeOrder> search(Long locationId, Instant startDate, Instant endDate);
}
