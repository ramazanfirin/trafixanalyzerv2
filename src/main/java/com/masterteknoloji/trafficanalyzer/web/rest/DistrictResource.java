package com.masterteknoloji.trafficanalyzer.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.masterteknoloji.trafficanalyzer.domain.District;

import com.masterteknoloji.trafficanalyzer.repository.DistrictRepository;
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
 * REST controller for managing District.
 */
@RestController
@RequestMapping("/api")
public class DistrictResource {

    private final Logger log = LoggerFactory.getLogger(DistrictResource.class);

    private static final String ENTITY_NAME = "district";

    private final DistrictRepository districtRepository;

    public DistrictResource(DistrictRepository districtRepository) {
        this.districtRepository = districtRepository;
    }

    /**
     * POST  /districts : Create a new district.
     *
     * @param district the district to create
     * @return the ResponseEntity with status 201 (Created) and with body the new district, or with status 400 (Bad Request) if the district has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/districts")
    @Timed
    public ResponseEntity<District> createDistrict(@Valid @RequestBody District district) throws URISyntaxException {
        log.debug("REST request to save District : {}", district);
        if (district.getId() != null) {
            throw new BadRequestAlertException("A new district cannot already have an ID", ENTITY_NAME, "idexists");
        }
        District result = districtRepository.save(district);
        return ResponseEntity.created(new URI("/api/districts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /districts : Updates an existing district.
     *
     * @param district the district to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated district,
     * or with status 400 (Bad Request) if the district is not valid,
     * or with status 500 (Internal Server Error) if the district couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/districts")
    @Timed
    public ResponseEntity<District> updateDistrict(@Valid @RequestBody District district) throws URISyntaxException {
        log.debug("REST request to update District : {}", district);
        if (district.getId() == null) {
            return createDistrict(district);
        }
        District result = districtRepository.save(district);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, district.getId().toString()))
            .body(result);
    }

    /**
     * GET  /districts : get all the districts.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of districts in body
     */
    @GetMapping("/districts")
    @Timed
    public ResponseEntity<List<District>> getAllDistricts(Pageable pageable) {
        log.debug("REST request to get a page of Districts");
        Page<District> page = districtRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/districts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /districts/:id : get the "id" district.
     *
     * @param id the id of the district to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the district, or with status 404 (Not Found)
     */
    @GetMapping("/districts/{id}")
    @Timed
    public ResponseEntity<District> getDistrict(@PathVariable Long id) {
        log.debug("REST request to get District : {}", id);
        District district = districtRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(district));
    }

    /**
     * DELETE  /districts/:id : delete the "id" district.
     *
     * @param id the id of the district to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/districts/{id}")
    @Timed
    public ResponseEntity<Void> deleteDistrict(@PathVariable Long id) {
        log.debug("REST request to delete District : {}", id);
        districtRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
