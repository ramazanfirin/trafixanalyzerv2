package com.masterteknoloji.trafficanalyzer.web.rest;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import com.masterteknoloji.trafficanalyzer.domain.AnalyzeOrder;
import com.masterteknoloji.trafficanalyzer.domain.Line;
import com.masterteknoloji.trafficanalyzer.domain.VideoRecord;
import com.masterteknoloji.trafficanalyzer.repository.AnalyzeOrderRepository;
import com.masterteknoloji.trafficanalyzer.repository.LineRepository;
import com.masterteknoloji.trafficanalyzer.repository.VideoRecordRepository;
import com.masterteknoloji.trafficanalyzer.web.rest.errors.BadRequestAlertException;
import com.masterteknoloji.trafficanalyzer.web.rest.util.ExcelExporter;
import com.masterteknoloji.trafficanalyzer.web.rest.util.HeaderUtil;
import com.masterteknoloji.trafficanalyzer.web.rest.util.PaginationUtil;
import com.masterteknoloji.trafficanalyzer.web.rest.vm.VideoRecordSummaryVM;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing VideoRecord.
 */
@RestController
@RequestMapping("/api")
public class VideoRecordResource {

    private final Logger log = LoggerFactory.getLogger(VideoRecordResource.class);

    private static final String ENTITY_NAME = "videoRecord";

    private final VideoRecordRepository videoRecordRepository;
    
    private final AnalyzeOrderRepository analyzeOrderRepository;
    
    private final LineRepository lineRepository;

    public VideoRecordResource(VideoRecordRepository videoRecordRepository,AnalyzeOrderRepository analyzeOrderRepository,LineRepository lineRepository) {
        this.videoRecordRepository = videoRecordRepository;
        this.analyzeOrderRepository = analyzeOrderRepository;
        this.lineRepository = lineRepository;
    }

    /**
     * POST  /video-records : Create a new videoRecord.
     *
     * @param videoRecord the videoRecord to create
     * @return the ResponseEntity with status 201 (Created) and with body the new videoRecord, or with status 400 (Bad Request) if the videoRecord has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/video-records")
    @Timed
    public ResponseEntity<VideoRecord> createVideoRecord(@Valid @RequestBody VideoRecord videoRecord) throws URISyntaxException {
        log.debug("REST request to save VideoRecord : {}", videoRecord);
        if (videoRecord.getId() != null) {
            throw new BadRequestAlertException("A new videoRecord cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VideoRecord result = videoRecordRepository.save(videoRecord);
        return ResponseEntity.created(new URI("/api/video-records/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /video-records : Updates an existing videoRecord.
     *
     * @param videoRecord the videoRecord to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated videoRecord,
     * or with status 400 (Bad Request) if the videoRecord is not valid,
     * or with status 500 (Internal Server Error) if the videoRecord couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/video-records")
    @Timed
    public ResponseEntity<VideoRecord> updateVideoRecord(@Valid @RequestBody VideoRecord videoRecord) throws URISyntaxException {
        log.debug("REST request to update VideoRecord : {}", videoRecord);
        if (videoRecord.getId() == null) {
            return createVideoRecord(videoRecord);
        }
        VideoRecord result = videoRecordRepository.save(videoRecord);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, videoRecord.getId().toString()))
            .body(result);
    }

    /**
     * GET  /video-records : get all the videoRecords.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of videoRecords in body
     */
    @GetMapping("/video-records")
    @Timed
    public ResponseEntity<List<VideoRecord>> getAllVideoRecords(Pageable pageable) {
        log.debug("REST request to get a page of VideoRecords");
        Page<VideoRecord> page = videoRecordRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/video-records");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /video-records/:id : get the "id" videoRecord.
     *
     * @param id the id of the videoRecord to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the videoRecord, or with status 404 (Not Found)
     */
    @GetMapping("/video-records/{id}")
    @Timed
    public ResponseEntity<VideoRecord> getVideoRecord(@PathVariable Long id) {
        log.debug("REST request to get VideoRecord : {}", id);
        VideoRecord videoRecord = videoRecordRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(videoRecord));
    }

    /**
     * DELETE  /video-records/:id : delete the "id" videoRecord.
     *
     * @param id the id of the videoRecord to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/video-records/{id}")
    @Timed
    public ResponseEntity<Void> deleteVideoRecord(@PathVariable Long id) {
        log.debug("REST request to delete VideoRecord : {}", id);
        videoRecordRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    
    @GetMapping("/video-records/getResultOfAnalyzeOrder/{id}")
    @Timed
    public List<VideoRecordSummaryVM> getResultOfAnalyzeOrder(@PathVariable Long id) {
    	
    	List<VideoRecordSummaryVM> result = new ArrayList<VideoRecordSummaryVM>();
    	
    	Iterable<Map<String,Object>> videoRecords = videoRecordRepository.getResultOfOrderReport(id);
    	for (Map<String, Object> map : videoRecords) {
    		VideoRecordSummaryVM videoRecordSummaryVM = new VideoRecordSummaryVM();
    		videoRecordSummaryVM.setCount((BigInteger)map.get("counts"));
    		videoRecordSummaryVM.setDate(map.get("grouptime"));
    		videoRecordSummaryVM.setDirectionName((String)map.get("line"));
    		videoRecordSummaryVM.setType((String)map.get("type"));
    		result.add(videoRecordSummaryVM);
		}
    	return result;
    }

    @GetMapping("/video-records/getResultOfAnalyzeOrderAndLineId/{id}/{lineId}")
    @Timed
    public List<VideoRecordSummaryVM> getResultOfAnalyzeOrder(@PathVariable Long id,@PathVariable Long lineId) {
    	
    	List<VideoRecordSummaryVM> result = new ArrayList<VideoRecordSummaryVM>();
    	
    	Iterable<Map<String,Object>> videoRecords = videoRecordRepository.getResultOfOrderReport(id,lineId);
    	for (Map<String, Object> map : videoRecords) {
    		VideoRecordSummaryVM videoRecordSummaryVM = new VideoRecordSummaryVM();
    		videoRecordSummaryVM.setCount((BigInteger)map.get("counts"));
    		videoRecordSummaryVM.setDate(map.get("grouptime"));
    		videoRecordSummaryVM.setDirectionName((String)map.get("line"));
    		videoRecordSummaryVM.setType((String)map.get("type"));
    		result.add(videoRecordSummaryVM);
		}
    	return result;
    }
    
    @GetMapping("/video-records/generateExcelFile/{id}")
    @Timed
    public void generateExcelFile(@PathVariable Long id,HttpServletResponse response) throws IOException {
    	
    	
    	response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
         
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=analyze_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);
         
        XSSFWorkbook workbook = new XSSFWorkbook();
        ExcelExporter excelExporter = new ExcelExporter();
        
        AnalyzeOrder analyzeOrder = analyzeOrderRepository.findOne(id);
        
        XSSFSheet scenario = excelExporter.createSheet(workbook, "Scenario");
        excelExporter.writeImage(workbook, scenario, analyzeOrder.getScreenShoot());
        
        
    	List<VideoRecordSummaryVM> result = getResultOfAnalyzeOrder(id);
        XSSFSheet allSheet = excelExporter.createSheet(workbook, "All");
        excelExporter.writeData(workbook, allSheet, result);
        
        List<Line> lines = lineRepository.getLineListByScenarioId(analyzeOrder.getScenario().getId());
        for (Iterator iterator = lines.iterator(); iterator.hasNext();) {
			Line line = (Line) iterator.next();
			List<VideoRecordSummaryVM> tempList = getResultOfAnalyzeOrder(id, line.getId());
			XSSFSheet sheetTemp = excelExporter.createSheet(workbook, line.getName());
			excelExporter.writeData(workbook, sheetTemp, tempList);
		}
        
        excelExporter.export(workbook,response);   
    	
    }
    
    public void calculateTotalForSheet() {
    	
    }
}
