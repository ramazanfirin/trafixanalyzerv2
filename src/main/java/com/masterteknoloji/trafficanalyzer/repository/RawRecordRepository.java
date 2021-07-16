package com.masterteknoloji.trafficanalyzer.repository;

import com.masterteknoloji.trafficanalyzer.domain.RawRecord;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the RawRecord entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RawRecordRepository extends JpaRepository<RawRecord, Long> {

}
