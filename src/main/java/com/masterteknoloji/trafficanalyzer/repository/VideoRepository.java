package com.masterteknoloji.trafficanalyzer.repository;

import com.masterteknoloji.trafficanalyzer.domain.Video;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Video entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {

}
