package com.masterteknoloji.trafficanalyzer.repository;

import com.masterteknoloji.trafficanalyzer.domain.Camera;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Camera entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CameraRepository extends JpaRepository<Camera, Long> {

}
