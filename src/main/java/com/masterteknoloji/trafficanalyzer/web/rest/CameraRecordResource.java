package com.masterteknoloji.trafficanalyzer.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.masterteknoloji.trafficanalyzer.domain.CameraRecord;

import com.masterteknoloji.trafficanalyzer.repository.CameraRecordRepository;
import com.masterteknoloji.trafficanalyzer.web.rest.errors.BadRequestAlertException;
import com.masterteknoloji.trafficanalyzer.web.rest.util.HeaderUtil;
import com.masterteknoloji.trafficanalyzer.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing CameraRecord.
 */
@RestController
@RequestMapping("/api")
public class CameraRecordResource {

    private final Logger log = LoggerFactory.getLogger(CameraRecordResource.class);

    private static final String ENTITY_NAME = "cameraRecord";

    private final CameraRecordRepository cameraRecordRepository;

    public CameraRecordResource(CameraRecordRepository cameraRecordRepository) {
        this.cameraRecordRepository = cameraRecordRepository;
    }

    /**
     * POST  /camera-records : Create a new cameraRecord.
     *
     * @param cameraRecord the cameraRecord to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cameraRecord, or with status 400 (Bad Request) if the cameraRecord has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/camera-records")
    @Timed
    public ResponseEntity<CameraRecord> createCameraRecord(@Valid @RequestBody CameraRecord cameraRecord) throws URISyntaxException {
        log.debug("REST request to save CameraRecord : {}", cameraRecord);
        if (cameraRecord.getId() != null) {
            throw new BadRequestAlertException("A new cameraRecord cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CameraRecord result = cameraRecordRepository.save(cameraRecord);
        return ResponseEntity.created(new URI("/api/camera-records/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /camera-records : Updates an existing cameraRecord.
     *
     * @param cameraRecord the cameraRecord to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cameraRecord,
     * or with status 400 (Bad Request) if the cameraRecord is not valid,
     * or with status 500 (Internal Server Error) if the cameraRecord couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/camera-records")
    @Timed
    public ResponseEntity<CameraRecord> updateCameraRecord(@Valid @RequestBody CameraRecord cameraRecord) throws URISyntaxException {
        log.debug("REST request to update CameraRecord : {}", cameraRecord);
        if (cameraRecord.getId() == null) {
            return createCameraRecord(cameraRecord);
        }
        CameraRecord result = cameraRecordRepository.save(cameraRecord);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cameraRecord.getId().toString()))
            .body(result);
    }

    /**
     * GET  /camera-records : get all the cameraRecords.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of cameraRecords in body
     */
    @GetMapping("/camera-records")
    @Timed
    public ResponseEntity<List<CameraRecord>> getAllCameraRecords(Pageable pageable) {
        log.debug("REST request to get a page of CameraRecords");
        Page<CameraRecord> page = cameraRecordRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/camera-records");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /camera-records/:id : get the "id" cameraRecord.
     *
     * @param id the id of the cameraRecord to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cameraRecord, or with status 404 (Not Found)
     */
    @GetMapping("/camera-records/{id}")
    @Timed
    public ResponseEntity<CameraRecord> getCameraRecord(@PathVariable Long id) {
        log.debug("REST request to get CameraRecord : {}", id);
        CameraRecord cameraRecord = cameraRecordRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cameraRecord));
    }

    /**
     * DELETE  /camera-records/:id : delete the "id" cameraRecord.
     *
     * @param id the id of the cameraRecord to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/camera-records/{id}")
    @Timed
    public ResponseEntity<Void> deleteCameraRecord(@PathVariable Long id) {
        log.debug("REST request to delete CameraRecord : {}", id);
        cameraRecordRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
