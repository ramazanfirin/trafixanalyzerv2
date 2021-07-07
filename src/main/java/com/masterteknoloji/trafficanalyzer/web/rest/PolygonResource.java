package com.masterteknoloji.trafficanalyzer.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.masterteknoloji.trafficanalyzer.domain.Polygon;

import com.masterteknoloji.trafficanalyzer.repository.PolygonRepository;
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
 * REST controller for managing Polygon.
 */
@RestController
@RequestMapping("/api")
public class PolygonResource {

    private final Logger log = LoggerFactory.getLogger(PolygonResource.class);

    private static final String ENTITY_NAME = "polygon";

    private final PolygonRepository polygonRepository;

    public PolygonResource(PolygonRepository polygonRepository) {
        this.polygonRepository = polygonRepository;
    }

    /**
     * POST  /polygons : Create a new polygon.
     *
     * @param polygon the polygon to create
     * @return the ResponseEntity with status 201 (Created) and with body the new polygon, or with status 400 (Bad Request) if the polygon has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/polygons")
    @Timed
    public ResponseEntity<Polygon> createPolygon(@RequestBody Polygon polygon) throws URISyntaxException {
        log.debug("REST request to save Polygon : {}", polygon);
        if (polygon.getId() != null) {
            throw new BadRequestAlertException("A new polygon cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Polygon result = polygonRepository.save(polygon);
        return ResponseEntity.created(new URI("/api/polygons/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /polygons : Updates an existing polygon.
     *
     * @param polygon the polygon to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated polygon,
     * or with status 400 (Bad Request) if the polygon is not valid,
     * or with status 500 (Internal Server Error) if the polygon couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/polygons")
    @Timed
    public ResponseEntity<Polygon> updatePolygon(@RequestBody Polygon polygon) throws URISyntaxException {
        log.debug("REST request to update Polygon : {}", polygon);
        if (polygon.getId() == null) {
            return createPolygon(polygon);
        }
        Polygon result = polygonRepository.save(polygon);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, polygon.getId().toString()))
            .body(result);
    }

    /**
     * GET  /polygons : get all the polygons.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of polygons in body
     */
    @GetMapping("/polygons")
    @Timed
    public ResponseEntity<List<Polygon>> getAllPolygons(Pageable pageable) {
        log.debug("REST request to get a page of Polygons");
        Page<Polygon> page = polygonRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/polygons");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /polygons/:id : get the "id" polygon.
     *
     * @param id the id of the polygon to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the polygon, or with status 404 (Not Found)
     */
    @GetMapping("/polygons/{id}")
    @Timed
    public ResponseEntity<Polygon> getPolygon(@PathVariable Long id) {
        log.debug("REST request to get Polygon : {}", id);
        Polygon polygon = polygonRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(polygon));
    }

    /**
     * DELETE  /polygons/:id : delete the "id" polygon.
     *
     * @param id the id of the polygon to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/polygons/{id}")
    @Timed
    public ResponseEntity<Void> deletePolygon(@PathVariable Long id) {
        log.debug("REST request to delete Polygon : {}", id);
        polygonRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
