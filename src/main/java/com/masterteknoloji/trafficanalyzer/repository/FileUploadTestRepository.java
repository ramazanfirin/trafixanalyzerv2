package com.masterteknoloji.trafficanalyzer.repository;

import com.masterteknoloji.trafficanalyzer.domain.FileUploadTest;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the FileUploadTest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FileUploadTestRepository extends JpaRepository<FileUploadTest, Long> {

}
