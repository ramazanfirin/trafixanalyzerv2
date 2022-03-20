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
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
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
import com.masterteknoloji.trafficanalyzer.config.ApplicationProperties;
import com.masterteknoloji.trafficanalyzer.domain.AnalyzeOrder;
import com.masterteknoloji.trafficanalyzer.domain.Direction;
import com.masterteknoloji.trafficanalyzer.domain.Line;
import com.masterteknoloji.trafficanalyzer.domain.Scenario;
import com.masterteknoloji.trafficanalyzer.domain.VideoRecord;
import com.masterteknoloji.trafficanalyzer.domain.enumeration.VideoType;
import com.masterteknoloji.trafficanalyzer.repository.AnalyzeOrderRepository;
import com.masterteknoloji.trafficanalyzer.repository.DirectionRepository;
import com.masterteknoloji.trafficanalyzer.repository.LineRepository;
import com.masterteknoloji.trafficanalyzer.repository.ScenarioRepository;
import com.masterteknoloji.trafficanalyzer.repository.VideoRecordRepository;
import com.masterteknoloji.trafficanalyzer.service.UserService;
import com.masterteknoloji.trafficanalyzer.web.rest.errors.BadRequestAlertException;
import com.masterteknoloji.trafficanalyzer.web.rest.util.ExcelExporter;
import com.masterteknoloji.trafficanalyzer.web.rest.util.HeaderUtil;
import com.masterteknoloji.trafficanalyzer.web.rest.util.PaginationUtil;
import com.masterteknoloji.trafficanalyzer.web.rest.util.Util;
import com.masterteknoloji.trafficanalyzer.web.rest.vm.ClassificationResultDetailsVM;
import com.masterteknoloji.trafficanalyzer.web.rest.vm.ClassificationResultVM;
import com.masterteknoloji.trafficanalyzer.web.rest.vm.DirectionReportDetail;
import com.masterteknoloji.trafficanalyzer.web.rest.vm.DirectionReportSummary;
import com.masterteknoloji.trafficanalyzer.web.rest.vm.DirectionReportVM;
import com.masterteknoloji.trafficanalyzer.web.rest.vm.LineCrossedVM;
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
    
    private final DirectionRepository directionRepository;
    
    private final ScenarioRepository scenarioRepository;
    
    private final LineRepository lineRepository;
    
    private final MessageSource messageSource;
    
    private final UserService userService;
    
    private final ApplicationProperties applicationProperties;

    public VideoRecordResource(VideoRecordRepository videoRecordRepository,AnalyzeOrderRepository analyzeOrderRepository,LineRepository lineRepository,
    		MessageSource messageSource,UserService userService, DirectionRepository directionRepository,ApplicationProperties applicationProperties,ScenarioRepository scenarioRepository) {
        this.videoRecordRepository = videoRecordRepository;
        this.analyzeOrderRepository = analyzeOrderRepository;
        this.lineRepository = lineRepository;
        this.messageSource = messageSource;
        this.userService = userService;
        this.directionRepository = directionRepository;
        this.applicationProperties = applicationProperties;
        this.scenarioRepository = scenarioRepository;
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
    	AnalyzeOrder analyzeOrder = analyzeOrderRepository.findOne(id);
    	Iterable<Map<String,Object>> videoRecords = null;
    	
    	if(analyzeOrder.getVideo().getType()==VideoType.STRAIGHT_ROAD) {
    		videoRecords = videoRecordRepository.getResultOfOrderReport(id,applicationProperties.getReportInterval());
    	}
    	else {
    		videoRecords = videoRecordRepository.getResultOfOrderReportForDirection(id,applicationProperties.getReportInterval());
     	}
    	
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
    
    @GetMapping("/video-records/getResultOfDirectionReport/{id}")
    @Timed
    public List<DirectionReportSummary> getResultOfDirectionReport(@PathVariable Long id) {
    	
    	AnalyzeOrder analyzeOrder = analyzeOrderRepository.findOne(id);
    	if(analyzeOrder.getVideo().getType()==VideoType.STRAIGHT_ROAD) {
    		return getResultOfDirectionReportForStraigthRoad(id);
    	}
    	else {
     		return getResultOfDirectionReportForIntersection(id);
     	   
    	}
    	
    }

    public List<DirectionReportSummary> getResultOfDirectionReportForStraigthRoad(@PathVariable Long id){
    	List<DirectionReportSummary> result = new ArrayList<DirectionReportSummary>();
    	
    	Iterable<Map<String,Object>> videoRecords = videoRecordRepository.getDirectionReportData(id);
    	for (Map<String, Object> map : videoRecords) {
    		DirectionReportSummary item = new DirectionReportSummary();
    		Long count = ((BigInteger)map.get("count")).longValue();
    		item.setCount(count);
    		item.setDirectionName((String)map.get("directionname"));
    		item.setStartLineName((String)map.get("startlinename"));
    		item.setEndLineName((String)map.get("endlinename"));
    		
    		Long startLineCount = ((BigInteger)map.get("startlinecount")).longValue();
    		Long endLineCount = ((BigInteger)map.get("endlinecount")).longValue();
    		item.setStartLineCount(startLineCount);
    		item.setEndLineCount(endLineCount);
    		
    		item.setStartLineRate(Util.calculatePertencile(startLineCount, count));
    		item.setEndLineRate(Util.calculatePertencile(endLineCount, count));
    		result.add(item);
		}
    	return result;
    }
    
    public List<DirectionReportSummary> getResultOfDirectionReportForIntersection(@PathVariable Long id){
    	List<DirectionReportSummary> result = new ArrayList<DirectionReportSummary>();
    	
    	Iterable<Map<String,Object>> startLinesCrossCount = videoRecordRepository.getStartLineCrossCountForDirection(id);
    	Iterable<Map<String,Object>> endLinesCrossCount= videoRecordRepository.getEndLineCrossCountForDirection(id);
    	Iterable<Map<String,Object>> directionCrossCount=videoRecordRepository.getDirectionCrossCountForDirection(id);
    	
    	for (Map<String, Object> map : directionCrossCount) {
    		DirectionReportSummary item = new DirectionReportSummary();
    		Long count = ((BigInteger)map.get("count")).longValue();
    		item.setCount(count);
    		item.setDirectionName((String)map.get("name"));
    		item.setStartLineName((String)map.get("startlinename"));
    		item.setEndLineName((String)map.get("endlinename"));
    		
    		Long startLineCount = getCrossingCountValue(startLinesCrossCount,map.get("startlinename").toString()).longValue();
    		Long endLineCount = getCrossingCountValue(endLinesCrossCount,map.get("endlinename").toString()).longValue();
    		item.setStartLineCount(startLineCount);
    		item.setEndLineCount(endLineCount);
    		
    		item.setStartLineRate(Util.calculatePertencile(startLineCount, count));
    		item.setEndLineRate(Util.calculatePertencile(endLineCount, count));
    		
    		result.add(item);
		}
    	
    	return result;
    }
    
    public BigInteger getCrossingCountValue(Iterable<Map<String,Object>> crossCountMap,String lineName) {
    	for (Map<String, Object> map : crossCountMap) {
    		if(map.get("linename").toString().equals(lineName)) {
    			return (BigInteger) map.get("count");
    		}
    	}
    	return new BigInteger("0");
    }
    
    @GetMapping("/video-records/getResultOfAnalyzeOrderAndLineId/{id}/{lineId}")
    @Timed
    public List<VideoRecordSummaryVM> getResultOfAnalyzeOrder(@PathVariable Long id,@PathVariable Long lineId) {
    	
    	List<VideoRecordSummaryVM> result = new ArrayList<VideoRecordSummaryVM>();
    	
    	Iterable<Map<String,Object>> videoRecords = videoRecordRepository.getResultOfOrderReport(id,applicationProperties.getReportInterval(),lineId);
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
    
    public List<VideoRecordSummaryVM> getResultOfAnalyzeOrderForDirection(@PathVariable Long id,@PathVariable Long direcitonId) {
    	
    	List<VideoRecordSummaryVM> result = new ArrayList<VideoRecordSummaryVM>();
    	
    	Iterable<Map<String,Object>> videoRecords = videoRecordRepository.getResultOfOrderReportForDirection(id,applicationProperties.getReportInterval(), direcitonId);
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
    
    @GetMapping("/video-records/generateExcelFile/{id}/{language}")
    @Timed
    public void generateExcelFile(@PathVariable Long id,@PathVariable String language,HttpServletResponse response,HttpServletRequest request) throws IOException {
    	
    	
    	response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
         
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=analyze_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);
        
        if(StringUtils.isEmpty(language))
        	language = "tr";
        
        Locale locale = Locale.forLanguageTag(language);
        XSSFWorkbook workbook = new XSSFWorkbook();
        ExcelExporter excelExporter = new ExcelExporter(messageSource,language);
        
        AnalyzeOrder analyzeOrder = analyzeOrderRepository.findOne(id);
        
        XSSFSheet scenario = excelExporter.createSheet(workbook, messageSource.getMessage("excel.scenario", null,locale));
        excelExporter.writeImage(workbook, scenario, analyzeOrder.getScreenShoot());
        
        
    	List<VideoRecordSummaryVM> result = getResultOfAnalyzeOrder(id);
        XSSFSheet allSheet = excelExporter.createSheet(workbook, messageSource.getMessage("excel.all", null,locale));
        excelExporter.writeData(workbook, allSheet, result);
        
        if(analyzeOrder.getVideo().getType()==VideoType.STRAIGHT_ROAD) {
    		List<Line> lines = lineRepository.getLineListByScenarioId(analyzeOrder.getScenario().getId());
            for (Iterator iterator = lines.iterator(); iterator.hasNext();) {
    			Line line = (Line) iterator.next();
    			List<VideoRecordSummaryVM> tempList = getResultOfAnalyzeOrder(id, line.getId());
    			XSSFSheet sheetTemp = excelExporter.createSheet(workbook, line.getName());
    			excelExporter.writeData(workbook, sheetTemp, tempList);
    		}
    	}
    	else {
    		List<Direction> directions = directionRepository.getDirectionListByScenarioId(analyzeOrder.getScenario().getId());
            for (Iterator iterator = directions.iterator(); iterator.hasNext();) {
            	Direction direction = (Direction) iterator.next();
    			List<VideoRecordSummaryVM> tempList = getResultOfAnalyzeOrderForDirection(id, direction.getId());
    			XSSFSheet sheetTemp = excelExporter.createSheet(workbook, direction.getName());
    			excelExporter.writeData(workbook, sheetTemp, tempList);
    		}
     	   
    	}
        
        XSSFSheet yon = excelExporter.createSheetDirectionReport(workbook, messageSource.getMessage("excel.direction", null,locale));
        List<DirectionReportSummary> directionReportSummaries = getResultOfDirectionReport(id);
        excelExporter.writeDataForDirectionReport(workbook, yon, directionReportSummaries);
        
        excelExporter.export(workbook,response);   
    	
    }
    
    @GetMapping("/video-records/getClassificationData/{id}")
    @Timed
    public List<ClassificationResultVM> getClassificationData(@PathVariable Long id) {
    	
    	List<ClassificationResultVM> result = new ArrayList<ClassificationResultVM>();
    	
    	AnalyzeOrder analyzeOrder = analyzeOrderRepository.findOne(id);
    	Iterable<Map<String,Object>> videoRecords = null;
    	
    	if(analyzeOrder.getVideo().getType()==VideoType.STRAIGHT_ROAD) {
    		videoRecords = videoRecordRepository.getVehicleTypeGroups(id);
    	}
    	else {
    		videoRecords = videoRecordRepository.getVehicleTypeGroupsForDirection(id);
    	}
    	
    	for (Map<String, Object> map : videoRecords) {
    		
    		ClassificationResultVM classificationResultVM = getclassificationResultByName(result, (String)map.get("name"));
    		
    		ClassificationResultDetailsVM itemVM = new ClassificationResultDetailsVM();
    		itemVM.setType((String)map.get("vehicle_type"));
    		itemVM.setCount(((BigInteger)map.get("count")).longValue());
    		
    		classificationResultVM.getDatas().add(itemVM);
		}
    	return result;
    }
    
    private ClassificationResultVM getclassificationResultByName(List<ClassificationResultVM> list,String name) {
    	
    	for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			ClassificationResultVM classificationResultVM = (ClassificationResultVM) iterator.next();
			if(classificationResultVM.getLineName().equals(name))
				return classificationResultVM;
		}
    	
    	ClassificationResultVM result = new ClassificationResultVM();
    	result.setLineName(name);
    	list.add(result);
    	return result;
    }
    
    @GetMapping("/video-records/getAverageSpeedData/{id}")
    @Timed
    public List<ClassificationResultVM> getAverageSpeedData(@PathVariable Long id) {
    	
    	List<ClassificationResultVM> result = new ArrayList<ClassificationResultVM>();
    	
    	Iterable<Map<String,Object>> videoRecords = videoRecordRepository.getAverageSpeed(id);
    	for (Map<String, Object> map : videoRecords) {
    		
    		ClassificationResultVM classificationResultVM = getclassificationResultByName(result, (String)map.get("linename"));
    		Double speed = (Double)map.get("speed");
    		classificationResultVM.setAverageSpeed(speed.longValue());
    	}
    	
    	return result;
    }
    
    @GetMapping("/video-records/getVisulationData/{id}")
    @Timed
    public List<LineCrossedVM> getVisulationData(@PathVariable Long id) {
    	
    	List<LineCrossedVM> result = new ArrayList<LineCrossedVM>();
    	
    	Iterable<Map<String,Object>> videoRecords = null;
    	
    	AnalyzeOrder analyzeOrder = analyzeOrderRepository.findOne(id);
    	if(analyzeOrder.getVideo().getType()==VideoType.STRAIGHT_ROAD) {
    		videoRecords = videoRecordRepository.getVisulationData(id);
    	}
    	else {
    		videoRecords = videoRecordRepository.getVisulationDataForDirection(id);
     	   
    	}
    	
    	for (Map<String, Object> map : videoRecords) {
    		LineCrossedVM lineCrossedVM = new LineCrossedVM();
    		lineCrossedVM.setType((String)map.get("type"));
    		lineCrossedVM.setDuration(((BigInteger)map.get("duration")).longValue());
    		lineCrossedVM.setLineId(((BigInteger)map.get("line")).longValue());
    		lineCrossedVM.setSpeed((Double)map.get("speed"));
    		lineCrossedVM.setDirectionName((String)map.get("directionname"));    		
			result.add(lineCrossedVM);
		}
    	return result;
    }
    
    @GetMapping("/video-records/getDirectionReportByScnario/{id}")
    @Timed
    public List<DirectionReportVM> getDirectionReportByScnario(@PathVariable Long id) {
    	
    	List<DirectionReportVM> result = new ArrayList<DirectionReportVM>();
    	Scenario scenario = scenarioRepository.findOne(id);
    	
    	Iterable<Map<String,Object>> videoRecords;
    	
    	if(scenario.getVideo().getType()==VideoType.STRAIGHT_ROAD)
    		videoRecords = getReportOfLineScenario(id);
    	else
    		videoRecords = getReportOfDirectionScenario(id);
    	
    		
    	for (Map<String, Object> map : videoRecords) {
    		
    		BigInteger analyzeId = (BigInteger)map.get("analyze_id");
    		DirectionReportVM directionReportVM = getDirectionReportVMById(result, analyzeId);
    		
    		if(directionReportVM==null) {
	    		directionReportVM = new DirectionReportVM();
	    		directionReportVM.setAnalyzeId((BigInteger)map.get("analyze_id"));
	    		directionReportVM.setScenarioName((String)map.get("scenarioname"));
	    		directionReportVM.setVideoEndDate((Date)map.get("enddate"));
	    		directionReportVM.setVideoName((String)map.get("videoname"));
	    		directionReportVM.setVideoStartDate((Date)map.get("startdate"));
	    		result.add(directionReportVM);
    		}
    		
    		DirectionReportDetail directionReportDetail = new DirectionReportDetail();
    		directionReportDetail.setCount((BigInteger)map.get("count"));
    		directionReportDetail.setDirectionName((String)map.get("directionname"));
    		directionReportVM.getDetails().add(directionReportDetail);
    		
    	}
    	
    	return result;
    }
    
    public Iterable<Map<String,Object>> getReportOfLineScenario(Long id){
    	Iterable<Map<String,Object>> videoRecords = videoRecordRepository.getLineReportByScnario(id);
    	return videoRecords;
    }
    
    public Iterable<Map<String,Object>> getReportOfDirectionScenario(Long id){
    	Iterable<Map<String,Object>> videoRecords = videoRecordRepository.getDirectionReportByScnario(id);
    	return videoRecords;
    }
    
    public DirectionReportVM getDirectionReportVMById(List<DirectionReportVM> result, BigInteger analyzeId) {
    	for (Iterator iterator = result.iterator(); iterator.hasNext();) {
			DirectionReportVM directionReportVM = (DirectionReportVM) iterator.next();
			if(directionReportVM.getAnalyzeId().longValue() == analyzeId.longValue())
				return directionReportVM;
		}
    	return null;
    }
}
