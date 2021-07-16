package com.masterteknoloji.trafficanalyzer.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.masterteknoloji.trafficanalyzer.domain.AnalyzeOrderDetails;

import com.masterteknoloji.trafficanalyzer.repository.AnalyzeOrderDetailsRepository;
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
 * REST controller for managing AnalyzeOrderDetails.
 */
@RestController
@RequestMapping("/api")
public class AnalyzeOrderDetailsResource {

    private final Logger log = LoggerFactory.getLogger(AnalyzeOrderDetailsResource.class);

    private static final String ENTITY_NAME = "analyzeOrderDetails";

    private final AnalyzeOrderDetailsRepository analyzeOrderDetailsRepository;

    public AnalyzeOrderDetailsResource(AnalyzeOrderDetailsRepository analyzeOrderDetailsRepository) {
        this.analyzeOrderDetailsRepository = analyzeOrderDetailsRepository;
    }

    /**
     * POST  /analyze-order-details : Create a new analyzeOrderDetails.
     *
     * @param analyzeOrderDetails the analyzeOrderDetails to create
     * @return the ResponseEntity with status 201 (Created) and with body the new analyzeOrderDetails, or with status 400 (Bad Request) if the analyzeOrderDetails has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/analyze-order-details")
    @Timed
    public ResponseEntity<AnalyzeOrderDetails> createAnalyzeOrderDetails(@RequestBody AnalyzeOrderDetails analyzeOrderDetails) throws URISyntaxException {
        log.debug("REST request to save AnalyzeOrderDetails : {}", analyzeOrderDetails);
        if (analyzeOrderDetails.getId() != null) {
            throw new BadRequestAlertException("A new analyzeOrderDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AnalyzeOrderDetails result = analyzeOrderDetailsRepository.save(analyzeOrderDetails);
        return ResponseEntity.created(new URI("/api/analyze-order-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /analyze-order-details : Updates an existing analyzeOrderDetails.
     *
     * @param analyzeOrderDetails the analyzeOrderDetails to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated analyzeOrderDetails,
     * or with status 400 (Bad Request) if the analyzeOrderDetails is not valid,
     * or with status 500 (Internal Server Error) if the analyzeOrderDetails couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/analyze-order-details")
    @Timed
    public ResponseEntity<AnalyzeOrderDetails> updateAnalyzeOrderDetails(@RequestBody AnalyzeOrderDetails analyzeOrderDetails) throws URISyntaxException {
        log.debug("REST request to update AnalyzeOrderDetails : {}", analyzeOrderDetails);
        if (analyzeOrderDetails.getId() == null) {
            return createAnalyzeOrderDetails(analyzeOrderDetails);
        }
        AnalyzeOrderDetails result = analyzeOrderDetailsRepository.save(analyzeOrderDetails);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, analyzeOrderDetails.getId().toString()))
            .body(result);
    }

    /**
     * GET  /analyze-order-details : get all the analyzeOrderDetails.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of analyzeOrderDetails in body
     */
    @GetMapping("/analyze-order-details")
    @Timed
    public ResponseEntity<List<AnalyzeOrderDetails>> getAllAnalyzeOrderDetails(Pageable pageable) {
        log.debug("REST request to get a page of AnalyzeOrderDetails");
        Page<AnalyzeOrderDetails> page = analyzeOrderDetailsRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/analyze-order-details");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /analyze-order-details/:id : get the "id" analyzeOrderDetails.
     *
     * @param id the id of the analyzeOrderDetails to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the analyzeOrderDetails, or with status 404 (Not Found)
     */
    @GetMapping("/analyze-order-details/{id}")
    @Timed
    public ResponseEntity<AnalyzeOrderDetails> getAnalyzeOrderDetails(@PathVariable Long id) {
        log.debug("REST request to get AnalyzeOrderDetails : {}", id);
        AnalyzeOrderDetails analyzeOrderDetails = analyzeOrderDetailsRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(analyzeOrderDetails));
    }

    /**
     * DELETE  /analyze-order-details/:id : delete the "id" analyzeOrderDetails.
     *
     * @param id the id of the analyzeOrderDetails to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/analyze-order-details/{id}")
    @Timed
    public ResponseEntity<Void> deleteAnalyzeOrderDetails(@PathVariable Long id) {
        log.debug("REST request to delete AnalyzeOrderDetails : {}", id);
        analyzeOrderDetailsRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
