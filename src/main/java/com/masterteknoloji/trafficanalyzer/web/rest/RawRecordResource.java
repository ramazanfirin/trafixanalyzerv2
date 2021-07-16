package com.masterteknoloji.trafficanalyzer.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.masterteknoloji.trafficanalyzer.domain.RawRecord;

import com.masterteknoloji.trafficanalyzer.repository.RawRecordRepository;
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

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing RawRecord.
 */
@RestController
@RequestMapping("/api")
public class RawRecordResource {

    private final Logger log = LoggerFactory.getLogger(RawRecordResource.class);

    private static final String ENTITY_NAME = "rawRecord";

    private final RawRecordRepository rawRecordRepository;

    public RawRecordResource(RawRecordRepository rawRecordRepository) {
        this.rawRecordRepository = rawRecordRepository;
    }

    /**
     * POST  /raw-records : Create a new rawRecord.
     *
     * @param rawRecord the rawRecord to create
     * @return the ResponseEntity with status 201 (Created) and with body the new rawRecord, or with status 400 (Bad Request) if the rawRecord has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/raw-records")
    @Timed
    public ResponseEntity<RawRecord> createRawRecord(@RequestBody RawRecord rawRecord) throws URISyntaxException {
        log.debug("REST request to save RawRecord : {}", rawRecord);
        if (rawRecord.getId() != null) {
            throw new BadRequestAlertException("A new rawRecord cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RawRecord result = rawRecordRepository.save(rawRecord);
        return ResponseEntity.created(new URI("/api/raw-records/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /raw-records : Updates an existing rawRecord.
     *
     * @param rawRecord the rawRecord to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated rawRecord,
     * or with status 400 (Bad Request) if the rawRecord is not valid,
     * or with status 500 (Internal Server Error) if the rawRecord couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/raw-records")
    @Timed
    public ResponseEntity<RawRecord> updateRawRecord(@RequestBody RawRecord rawRecord) throws URISyntaxException {
        log.debug("REST request to update RawRecord : {}", rawRecord);
        if (rawRecord.getId() == null) {
            return createRawRecord(rawRecord);
        }
        RawRecord result = rawRecordRepository.save(rawRecord);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, rawRecord.getId().toString()))
            .body(result);
    }

    /**
     * GET  /raw-records : get all the rawRecords.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of rawRecords in body
     */
    @GetMapping("/raw-records")
    @Timed
    public ResponseEntity<List<RawRecord>> getAllRawRecords(Pageable pageable) {
        log.debug("REST request to get a page of RawRecords");
        Page<RawRecord> page = rawRecordRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/raw-records");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /raw-records/:id : get the "id" rawRecord.
     *
     * @param id the id of the rawRecord to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the rawRecord, or with status 404 (Not Found)
     */
    @GetMapping("/raw-records/{id}")
    @Timed
    public ResponseEntity<RawRecord> getRawRecord(@PathVariable Long id) {
        log.debug("REST request to get RawRecord : {}", id);
        RawRecord rawRecord = rawRecordRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(rawRecord));
    }

    /**
     * DELETE  /raw-records/:id : delete the "id" rawRecord.
     *
     * @param id the id of the rawRecord to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/raw-records/{id}")
    @Timed
    public ResponseEntity<Void> deleteRawRecord(@PathVariable Long id) {
        log.debug("REST request to delete RawRecord : {}", id);
        rawRecordRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
