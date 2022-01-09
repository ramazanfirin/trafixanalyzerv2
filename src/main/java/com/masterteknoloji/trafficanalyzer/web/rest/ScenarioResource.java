package com.masterteknoloji.trafficanalyzer.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.masterteknoloji.trafficanalyzer.domain.Scenario;
import com.masterteknoloji.trafficanalyzer.domain.Video;
import com.masterteknoloji.trafficanalyzer.repository.ScenarioRepository;
import com.masterteknoloji.trafficanalyzer.web.rest.errors.BadRequestAlertException;
import com.masterteknoloji.trafficanalyzer.web.rest.util.HeaderUtil;
import com.masterteknoloji.trafficanalyzer.web.rest.util.PaginationUtil;
import com.masterteknoloji.trafficanalyzer.web.rest.util.Util;

import io.github.jhipster.web.util.ResponseUtil;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

/**
 * REST controller for managing Scenario.
 */
@RestController
@RequestMapping("/api")
public class ScenarioResource {

    private final Logger log = LoggerFactory.getLogger(ScenarioResource.class);

    private static final String ENTITY_NAME = "scenario";

    private final ScenarioRepository scenarioRepository;

    public ScenarioResource(ScenarioRepository scenarioRepository) {
        this.scenarioRepository = scenarioRepository;
    }

    /**
     * POST  /scenarios : Create a new scenario.
     *
     * @param scenario the scenario to create
     * @return the ResponseEntity with status 201 (Created) and with body the new scenario, or with status 400 (Bad Request) if the scenario has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     * @throws IOException 
     */
    @PostMapping("/scenarios")
    @Timed
    public ResponseEntity<Scenario> createScenario(@RequestBody Scenario scenario) throws URISyntaxException, IOException {
        log.debug("REST request to save Scenario : {}", scenario);
        if (scenario.getId() != null) {
            throw new BadRequestAlertException("A new scenario cannot already have an ID", ENTITY_NAME, "idexists");
        }
        
        if(scenario.getVideo()!=null) {
        	ByteArrayOutputStream os = Util.getScreenshotOfVideo(scenario.getVideo().getPath());
        	scenario.setScreenShot(os.toByteArray());
        }
        
        Scenario result = scenarioRepository.save(scenario);
        return ResponseEntity.created(new URI("/api/scenarios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
    
    @PostMapping("/scenarios/insertScreenshot")
    @Timed
    public ResponseEntity<Scenario> insertScreenshot(@RequestBody Scenario scenario) throws URISyntaxException, IOException {
        log.debug("REST request to save Scenario : {}", scenario);
        
        if(scenario.getVideo()!=null) {
        	ByteArrayOutputStream os = Util.getScreenshotOfVideo(scenario.getVideo().getPath());
        	scenario.setScreenShot(os.toByteArray());
        }
        
        Scenario result = scenarioRepository.save(scenario);
        return ResponseEntity.created(new URI("/api/scenarios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /scenarios : Updates an existing scenario.
     *
     * @param scenario the scenario to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated scenario,
     * or with status 400 (Bad Request) if the scenario is not valid,
     * or with status 500 (Internal Server Error) if the scenario couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     * @throws IOException 
     */
    @PutMapping("/scenarios")
    @Timed
    public ResponseEntity<Scenario> updateScenario(@RequestBody Scenario scenario) throws URISyntaxException, IOException {
        log.debug("REST request to update Scenario : {}", scenario);
        if (scenario.getId() == null) {
            return createScenario(scenario);
        }
        Scenario result = scenarioRepository.save(scenario);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, scenario.getId().toString()))
            .body(result);
    }

    /**
     * GET  /scenarios : get all the scenarios.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of scenarios in body
     */
    @GetMapping("/scenarios")
    @Timed
    public ResponseEntity<List<Scenario>> getAllScenarios(Pageable pageable) {
        log.debug("REST request to get a page of Scenarios");
        Page<Scenario> page = scenarioRepository.getActiveItem(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/scenarios");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/scenarios/getAll")
    @Timed
    public ResponseEntity<List<Scenario>> getAll(Pageable pageable) {
        log.debug("REST request to get a page of Scenarios");
        Page<Scenario> page = scenarioRepository.getActiveItem(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/scenarios");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    /**
     * GET  /scenarios/:id : get the "id" scenario.
     *
     * @param id the id of the scenario to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the scenario, or with status 404 (Not Found)
     */
    @GetMapping("/scenarios/{id}")
    @Timed
    public ResponseEntity<Scenario> getScenario(@PathVariable Long id) {
        log.debug("REST request to get Scenario : {}", id);
        Scenario scenario = scenarioRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(scenario));
    }

    /**
     * DELETE  /scenarios/:id : delete the "id" scenario.
     *
     * @param id the id of the scenario to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/scenarios/{id}")
    @Timed
    public ResponseEntity<Void> deleteScenario(@PathVariable Long id) {
        log.debug("REST request to delete Scenario : {}", id);
        Scenario scenario = scenarioRepository.findOne(id);
        scenario.setActive(false);
        scenarioRepository.save(scenario);
        //scenarioRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    
    @GetMapping("/scenarios/getScreenShoot/{id}")
    public @ResponseBody void getScreenShoot(@PathVariable Long id,HttpServletResponse response) throws IOException {
    	Scenario scenario = scenarioRepository.findOne(id);
    	if(scenario.getScreenShot()==null) {
    		if(scenario.getVideo()!=null) {
            	ByteArrayOutputStream os = Util.getScreenshotOfVideo(scenario.getVideo().getPath());
            	scenario.setScreenShot(os.toByteArray());
            	scenarioRepository.save(scenario);
            }
    	}
    	InputStream targetStream = new ByteArrayInputStream(scenario.getScreenShot());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        IOUtils.copy(targetStream, response.getOutputStream());
    }
    
    @GetMapping(value = "/scenarios/search")
    public List<Scenario> search(@RequestParam String name) throws IOException {
        
    	List<Scenario> result = scenarioRepository.findByNameContaining(name);
    	
    	return result;
    }
}
