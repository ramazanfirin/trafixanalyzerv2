package com.masterteknoloji.trafficanalyzer.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.masterteknoloji.trafficanalyzer.domain.Line;

import com.masterteknoloji.trafficanalyzer.repository.LineRepository;
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
 * REST controller for managing Line.
 */
@RestController
@RequestMapping("/api")
public class LineResource {

    private final Logger log = LoggerFactory.getLogger(LineResource.class);

    private static final String ENTITY_NAME = "line";

    private final LineRepository lineRepository;

    public LineResource(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    /**
     * POST  /lines : Create a new line.
     *
     * @param line the line to create
     * @return the ResponseEntity with status 201 (Created) and with body the new line, or with status 400 (Bad Request) if the line has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/lines")
    @Timed
    public ResponseEntity<Line> createLine(@RequestBody Line line) throws URISyntaxException {
        log.debug("REST request to save Line : {}", line);
        if (line.getId() != null) {
            throw new BadRequestAlertException("A new line cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Line result = lineRepository.save(line);
        return ResponseEntity.created(new URI("/api/lines/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /lines : Updates an existing line.
     *
     * @param line the line to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated line,
     * or with status 400 (Bad Request) if the line is not valid,
     * or with status 500 (Internal Server Error) if the line couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/lines")
    @Timed
    public ResponseEntity<Line> updateLine(@RequestBody Line line) throws URISyntaxException {
        log.debug("REST request to update Line : {}", line);
        if (line.getId() == null) {
            return createLine(line);
        }
        Line result = lineRepository.save(line);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, line.getId().toString()))
            .body(result);
    }

    /**
     * GET  /lines : get all the lines.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of lines in body
     */
    @GetMapping("/lines")
    @Timed
    public ResponseEntity<List<Line>> getAllLines(Pageable pageable) {
        log.debug("REST request to get a page of Lines");
        Page<Line> page = lineRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/lines");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /lines/:id : get the "id" line.
     *
     * @param id the id of the line to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the line, or with status 404 (Not Found)
     */
    @GetMapping("/lines/{id}")
    @Timed
    public ResponseEntity<Line> getLine(@PathVariable Long id) {
        log.debug("REST request to get Line : {}", id);
        Line line = lineRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(line));
    }

    /**
     * DELETE  /lines/:id : delete the "id" line.
     *
     * @param id the id of the line to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/lines/{id}")
    @Timed
    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
        log.debug("REST request to delete Line : {}", id);
        lineRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
