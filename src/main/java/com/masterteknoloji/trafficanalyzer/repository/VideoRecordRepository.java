package com.masterteknoloji.trafficanalyzer.repository;

import com.masterteknoloji.trafficanalyzer.domain.VideoRecord;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the VideoRecord entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VideoRecordRepository extends JpaRepository<VideoRecord, Long> {

}
