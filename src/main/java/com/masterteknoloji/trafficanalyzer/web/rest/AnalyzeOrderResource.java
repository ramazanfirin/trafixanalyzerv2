package com.masterteknoloji.trafficanalyzer.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.masterteknoloji.trafficanalyzer.domain.AnalyzeOrder;
import com.masterteknoloji.trafficanalyzer.domain.AnalyzeOrderDetails;
import com.masterteknoloji.trafficanalyzer.domain.Line;
import com.masterteknoloji.trafficanalyzer.domain.Polygon;
import com.masterteknoloji.trafficanalyzer.domain.enumeration.AnalyzeState;
import com.masterteknoloji.trafficanalyzer.domain.enumeration.PolygonType;
import com.masterteknoloji.trafficanalyzer.repository.AnalyzeOrderDetailsRepository;
import com.masterteknoloji.trafficanalyzer.repository.AnalyzeOrderRepository;
import com.masterteknoloji.trafficanalyzer.repository.LineRepository;
import com.masterteknoloji.trafficanalyzer.repository.PolygonRepository;
import com.masterteknoloji.trafficanalyzer.web.rest.errors.BadRequestAlertException;
import com.masterteknoloji.trafficanalyzer.web.rest.util.HeaderUtil;
import com.masterteknoloji.trafficanalyzer.web.rest.util.PaginationUtil;
import com.masterteknoloji.trafficanalyzer.web.rest.util.Util;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing AnalyzeOrder.
 */
@RestController
@RequestMapping("/api")
public class AnalyzeOrderResource {

    private final Logger log = LoggerFactory.getLogger(AnalyzeOrderResource.class);

    private static final String ENTITY_NAME = "analyzeOrder";

    private final AnalyzeOrderRepository analyzeOrderRepository;
    
    private final ObjectMapper objectMapper;
    
    private final LineRepository lineRepository;
    
    private final PolygonRepository polygonRepository;
    
    private final AnalyzeOrderDetailsRepository analyzeOrderDetailsRepository;

    public AnalyzeOrderResource(AnalyzeOrderRepository analyzeOrderRepository,  ObjectMapper objectMapper, LineRepository lineRepository, PolygonRepository polygonRepository, AnalyzeOrderDetailsRepository analyzeOrderDetailsRepository) {
        this.analyzeOrderRepository = analyzeOrderRepository;
        this.objectMapper = objectMapper;
        this.lineRepository = lineRepository;
        this.polygonRepository = polygonRepository;
        this.analyzeOrderDetailsRepository = analyzeOrderDetailsRepository;
    }

    /**
     * POST  /analyze-orders : Create a new analyzeOrder.
     *
     * @param analyzeOrder the analyzeOrder to create
     * @return the ResponseEntity with status 201 (Created) and with body the new analyzeOrder, or with status 400 (Bad Request) if the analyzeOrder has already an ID
     * @throws Exception 
     */
    @PostMapping("/analyze-orders")
    @Timed
    public ResponseEntity<AnalyzeOrder> createAnalyzeOrder(@RequestBody AnalyzeOrder analyzeOrder) throws Exception {
        log.debug("REST request to save AnalyzeOrder : {}", analyzeOrder);
        if (analyzeOrder.getId() != null) {
            throw new BadRequestAlertException("A new analyzeOrder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        
        AnalyzeOrder result = analyzeOrderRepository.save(analyzeOrder);
        
        List<Line> lines = lineRepository.getLineListByScenarioId(result.getScenario().getId());
        List<Polygon> speedPolygons = polygonRepository.getPolygonListByScenarioId(result.getScenario().getId(), PolygonType.SPEED);
        AnalyzeOrderDetails analyzeOrderDetails = Util.prepareAnalyzeOrderDetails(objectMapper, result.getId().toString(), analyzeOrder.getVideo().getPath(), lines, speedPolygons);
        analyzeOrderDetailsRepository.save(analyzeOrderDetails);
        
        result.setOrderDetails(analyzeOrderDetails);
        result = analyzeOrderRepository.save(analyzeOrder);
        
        return ResponseEntity.created(new URI("/api/analyze-orders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /analyze-orders : Updates an existing analyzeOrder.
     *
     * @param analyzeOrder the analyzeOrder to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated analyzeOrder,
     * or with status 400 (Bad Request) if the analyzeOrder is not valid,
     * or with status 500 (Internal Server Error) if the analyzeOrder couldn't be updated
     * @throws Exception 
     */
    @PutMapping("/analyze-orders")
    @Timed
    public ResponseEntity<AnalyzeOrder> updateAnalyzeOrder(@RequestBody AnalyzeOrder analyzeOrder) throws Exception {
        log.debug("REST request to update AnalyzeOrder : {}", analyzeOrder);
        if (analyzeOrder.getId() == null) {
            return createAnalyzeOrder(analyzeOrder);
        }
        AnalyzeOrder result = analyzeOrderRepository.save(analyzeOrder);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, analyzeOrder.getId().toString()))
            .body(result);
    }

    /**
     * GET  /analyze-orders : get all the analyzeOrders.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of analyzeOrders in body
     */
    @GetMapping("/analyze-orders")
    @Timed
    public ResponseEntity<List<AnalyzeOrder>> getAllAnalyzeOrders(Pageable pageable) {
        log.debug("REST request to get a page of AnalyzeOrders");
        Page<AnalyzeOrder> page = analyzeOrderRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/analyze-orders");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /analyze-orders/:id : get the "id" analyzeOrder.
     *
     * @param id the id of the analyzeOrder to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the analyzeOrder, or with status 404 (Not Found)
     */
    @GetMapping("/analyze-orders/{id}")
    @Timed
    public ResponseEntity<AnalyzeOrder> getAnalyzeOrder(@PathVariable Long id) {
        log.debug("REST request to get AnalyzeOrder : {}", id);
        AnalyzeOrder analyzeOrder = analyzeOrderRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(analyzeOrder));
    }

    /**
     * DELETE  /analyze-orders/:id : delete the "id" analyzeOrder.
     *
     * @param id the id of the analyzeOrder to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/analyze-orders/{id}")
    @Timed
    public ResponseEntity<Void> deleteAnalyzeOrder(@PathVariable Long id) {
        log.debug("REST request to delete AnalyzeOrder : {}", id);
        analyzeOrderRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
