package com.masterteknoloji.trafficanalyzer.repository;

import com.masterteknoloji.trafficanalyzer.domain.CameraRecord;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the CameraRecord entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CameraRecordRepository extends JpaRepository<CameraRecord, Long> {

}
