package com.masterteknoloji.trafficanalyzer.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.masterteknoloji.trafficanalyzer.domain.RawRecord;


/**
 * Spring Data JPA repository for the RawRecord entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RawRecordRepository extends JpaRepository<RawRecord, Long> {
	@Query("select v from RawRecord v where v.sessionID = ?1 and v.moved= ?2")
	Page<RawRecord> findBySessionId(Pageable pageable,String sessionId,Boolean moved);
	
	@Query("select count(v) from RawRecord v where v.sessionID = ?1 and v.moved= ?2")
	Long getCountBySessionId(String sessionId,Boolean moved);
}
