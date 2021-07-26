package com.masterteknoloji.trafficanalyzer.web.rest;

import java.awt.Point;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;

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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.masterteknoloji.trafficanalyzer.domain.Line;
import com.masterteknoloji.trafficanalyzer.domain.Polygon;
import com.masterteknoloji.trafficanalyzer.domain.Scenario;
import com.masterteknoloji.trafficanalyzer.repository.LineRepository;
import com.masterteknoloji.trafficanalyzer.repository.PolygonRepository;
import com.masterteknoloji.trafficanalyzer.repository.ScenarioRepository;
import com.masterteknoloji.trafficanalyzer.web.rest.errors.BadRequestAlertException;
import com.masterteknoloji.trafficanalyzer.web.rest.util.HeaderUtil;
import com.masterteknoloji.trafficanalyzer.web.rest.util.PaginationUtil;
import com.masterteknoloji.trafficanalyzer.web.rest.util.Util;
import com.masterteknoloji.trafficanalyzer.web.rest.vm.DistancePointVM;
import com.masterteknoloji.trafficanalyzer.web.rest.vm.LineSummaryVM;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Line.
 */
@RestController
@RequestMapping("/api")
public class LineResource {

    private final Logger log = LoggerFactory.getLogger(LineResource.class);

    private static final String ENTITY_NAME = "line";

    private final LineRepository lineRepository;

    private final ScenarioRepository scenarioRepository;
    
    private final PolygonRepository polygonRepository;
    
    public LineResource(LineRepository lineRepository, ScenarioRepository scenarioRepository, PolygonRepository polygonRepository ) {
        this.lineRepository = lineRepository;
        this.scenarioRepository = scenarioRepository;
        this.polygonRepository = polygonRepository;
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
    
    @PostMapping("/lines/createLineByPolygons")
    @Timed
    public ResponseEntity<Line> createLineByPolygons(@RequestBody String requestBody) throws JsonProcessingException, IOException {
        log.debug("REST request to get Line : {}", requestBody);
        
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode actualObj = objectMapper.readTree(requestBody);
        
        
        Line line = new Line();
        Scenario scenario = scenarioRepository.findOne(actualObj.get("scenarioId").asLong());
        Polygon startPolygon = polygonRepository.findOne(actualObj.get("startPolygonId").asLong());
        Polygon endPolygon = polygonRepository.findOne(actualObj.get("endPolygonId").asLong());
        String name = actualObj.get("name").asText();
        
        line.setScenario(scenario);
        line.setStartPolygon(startPolygon);
        line.setEndPolygon(endPolygon);
        line.setName(name);
        
        List<Point> calculatedPoints = calculateNeigberhood(startPolygon, endPolygon);
        if(calculatedPoints.size()==0)
        	throw new RuntimeException("komşu polygonlar bulunamadı");
                
        Polygon calculatedPolygon = new Polygon();
        calculatedPolygon.setPoints(Util.sortPoygonPoints(calculatedPoints));
        
        
        line.setCalculatedPolygon(calculatedPolygon);
        polygonRepository.save(calculatedPolygon);
        lineRepository.save(line);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(line));
    }
    
    @GetMapping("/lines/getLineListByScenarioId/{id}")
    @Timed
    public List<Line> getLineListByScenarioId(@PathVariable Long id) {
        log.debug("REST request to get Polygon : {}", id);
        List<Line> result = lineRepository.getLineListByScenarioId(id);
        return result;
    }
    
    public Point findCentroid(List<Point> points) {
	    int x = 0;
	    int y = 0;
	    for (Point p : points) {
	        x += p.x;
	        y += p.y;
	    }
	    Point center = new Point(0, 0);
	    center.x = x / points.size();
	    center.y = y / points.size();
	    return center;
	}
    
	public List<Point> calculateNeigberhood(Polygon p1, Polygon p2) {
		// p1.npoints

		TreeMap<Double,DistancePointVM> distanceMap = new TreeMap<Double,DistancePointVM>();
		
		List<Point> neigburPOints = new ArrayList<Point>();

		String[] p1Points = p1.getPoints().split(";");
		String[] p2Points = p2.getPoints().split(";");

		for (int i = 0; i < p1Points.length; i++) {
			String[] p1Point = p1Points[i].split(",");

			int x1 = Integer.parseInt(p1Point[0]);
			int y1 = Integer.parseInt(p1Point[1]);
			
			for (int j = 0; j < p2Points.length; j++) {
				String[] p2Point = p2Points[j].split(",");
				int x2 = Integer.parseInt(p2Point[0]);
				int y2 = Integer.parseInt(p2Point[1]);

				double dis = Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
				DistancePointVM distancePointVM= new DistancePointVM(); 
				distancePointVM.setDistance(dis);
				distancePointVM.setStartPoint(new Point(x1, y1));
				distancePointVM.setEndPoint(new Point(x2, y2));
				distanceMap.put(dis, distancePointVM);
				
			}
			
			
		}

		List<Double> employeeByKey = new ArrayList<>(distanceMap.keySet());
		Collections.sort(employeeByKey);
		DistancePointVM closerOne = distanceMap.get(employeeByKey.get(0));
		DistancePointVM closerTwo = distanceMap.get(employeeByKey.get(1));
		
		neigburPOints.add(closerOne.getStartPoint());
		neigburPOints.add(closerOne.getEndPoint());
		
		neigburPOints.add(closerTwo.getStartPoint());
		neigburPOints.add(closerTwo.getEndPoint());

		
		return neigburPOints;

	}
    
	@GetMapping("/lines/getLineSummaryListByScenarioId/{id}")
    @Timed
    public List<LineSummaryVM> getLineSummaryListByScenarioId(@PathVariable Long id) {
        log.debug("REST request to get Polygon : {}", id);
        List<LineSummaryVM> result = new ArrayList<LineSummaryVM>(); 
        List<Line> lines = lineRepository.getLineListByScenarioId(id);
        for (Iterator iterator = lines.iterator(); iterator.hasNext();) {
			Line line = (Line) iterator.next();
			LineSummaryVM item = new LineSummaryVM();
			item.setId(line.getId());
			item.setName(line.getName());
			item.setPoints(line.getCalculatedPolygon().getPoints());
			result.add(item);
		}
        
        return result;
    }
}
