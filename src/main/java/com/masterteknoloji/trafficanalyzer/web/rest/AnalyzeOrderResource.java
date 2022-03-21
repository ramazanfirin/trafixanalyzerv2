package com.masterteknoloji.trafficanalyzer.web.rest;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.masterteknoloji.trafficanalyzer.config.ApplicationProperties;
import com.masterteknoloji.trafficanalyzer.domain.AnalyzeOrder;
import com.masterteknoloji.trafficanalyzer.domain.AnalyzeOrderDetails;
import com.masterteknoloji.trafficanalyzer.domain.Direction;
import com.masterteknoloji.trafficanalyzer.domain.Line;
import com.masterteknoloji.trafficanalyzer.domain.Polygon;
import com.masterteknoloji.trafficanalyzer.domain.RawRecord;
import com.masterteknoloji.trafficanalyzer.domain.Scenario;
import com.masterteknoloji.trafficanalyzer.domain.VideoRecord;
import com.masterteknoloji.trafficanalyzer.domain.enumeration.AnalyzeState;
import com.masterteknoloji.trafficanalyzer.domain.enumeration.PolygonType;
import com.masterteknoloji.trafficanalyzer.repository.AnalyzeOrderDetailsRepository;
import com.masterteknoloji.trafficanalyzer.repository.AnalyzeOrderRepository;
import com.masterteknoloji.trafficanalyzer.repository.DirectionRepository;
import com.masterteknoloji.trafficanalyzer.repository.LineRepository;
import com.masterteknoloji.trafficanalyzer.repository.PolygonRepository;
import com.masterteknoloji.trafficanalyzer.repository.RawRecordRepository;
import com.masterteknoloji.trafficanalyzer.repository.VideoRecordRepository;
import com.masterteknoloji.trafficanalyzer.service.LinuxCommandService;
import com.masterteknoloji.trafficanalyzer.web.rest.errors.AnalyzeOrderNotStartedException;
import com.masterteknoloji.trafficanalyzer.web.rest.errors.BadRequestAlertException;
import com.masterteknoloji.trafficanalyzer.web.rest.util.HeaderUtil;
import com.masterteknoloji.trafficanalyzer.web.rest.util.PaginationUtil;
import com.masterteknoloji.trafficanalyzer.web.rest.util.Util;
import com.masterteknoloji.trafficanalyzer.web.rest.vm.AnalyzeOrderSummaryVM;
import com.masterteknoloji.trafficanalyzer.web.rest.vm.ParameterVM;

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

	private final RawRecordRepository rawRepository;

	private final VideoRecordRepository videoRecordRepository;

	private final AnalyzeOrderDetailsRepository analyzeOrderDetailsRepository;

	private final ApplicationProperties applicationProperties;

	private final LinuxCommandService linuxCommandService;
	
	private final DirectionRepository directionRepository;

	DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

	DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
			.withZone(ZoneId.systemDefault());

	public AnalyzeOrderResource(AnalyzeOrderRepository analyzeOrderRepository, ObjectMapper objectMapper,
			LineRepository lineRepository, PolygonRepository polygonRepository,
			AnalyzeOrderDetailsRepository analyzeOrderDetailsRepository, RawRecordRepository rawRepository,
			VideoRecordRepository videoRecordRepository, ApplicationProperties applicationProperties,
			LinuxCommandService linuxCommandService,DirectionRepository directionRepository) {
		this.analyzeOrderRepository = analyzeOrderRepository;
		this.objectMapper = objectMapper;
		this.lineRepository = lineRepository;
		this.polygonRepository = polygonRepository;
		this.analyzeOrderDetailsRepository = analyzeOrderDetailsRepository;
		this.rawRepository = rawRepository;
		this.videoRecordRepository = videoRecordRepository;
		this.applicationProperties = applicationProperties;
		this.linuxCommandService = linuxCommandService;
		this.directionRepository = directionRepository;
	}

	/**
	 * POST /analyze-orders : Create a new analyzeOrder.
	 *
	 * @param analyzeOrder the analyzeOrder to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         analyzeOrder, or with status 400 (Bad Request) if the analyzeOrder
	 *         has already an ID
	 * @throws Exception
	 */
	@PostMapping("/analyze-orders")
	@Timed
	public ResponseEntity<AnalyzeOrder> createAnalyzeOrder(@RequestBody AnalyzeOrder result) throws Exception {
		log.debug("REST request to save AnalyzeOrder : {}", result);
		if (result.getId() != null) {
			throw new BadRequestAlertException("A new analyzeOrder cannot already have an ID", ENTITY_NAME, "idexists");
		}
		result.setState(AnalyzeState.NOT_STARTED);
		analyzeOrderRepository.save(result);
		AnalyzeOrderDetails analyzeOrderDetails = null;
		try {
			List<Line> lines = lineRepository.getLineListByScenarioId(result.getScenario().getId());
			List<Direction> directions = directionRepository.getDirectionListByScenarioId(result.getScenario().getId());
			List<Polygon> speedPolygons = polygonRepository.getPolygonListByScenarioId(result.getScenario().getId(),PolygonType.SPEED);
			
			analyzeOrderDetails = Util.prepareAnalyzeOrderDetails(objectMapper,result.getId().toString(), result.getVideo().getPath(), lines,directions, speedPolygons,
					result.getShowVisulationWindow(),result.getVideo().getType().toString(),result.getAnalyzePerson());
			
			if(result.getAddToQuene()) {
				analyzeOrderDetails.setState(AnalyzeState.WAITING_ON_QUEBE);
				result.setState(AnalyzeState.WAITING_ON_QUEBE);
			}else {
				analyzeOrderDetails.setState(AnalyzeState.NOT_STARTED);
				result.setState(AnalyzeState.NOT_STARTED);
			}
			
			analyzeOrderDetailsRepository.save(analyzeOrderDetails);
			result.setOrderDetails(analyzeOrderDetails);
			result = analyzeOrderRepository.save(result);
			
			if(!result.getAddToQuene())
				linuxCommandService.startScriptByHttp(analyzeOrderDetails.getSessionId());
			
			
			return ResponseEntity.created(new URI("/api/analyze-orders/" + result.getId()))
					.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
		} catch (Exception e) {
			e.printStackTrace();
			analyzeOrderDetails.setState(AnalyzeState.ERROR);
			analyzeOrderDetailsRepository.save(analyzeOrderDetails);
			
			result.setState(AnalyzeState.NOT_STARTED);
			analyzeOrderRepository.save(result);
			
			throw new AnalyzeOrderNotStartedException(e.getMessage());
		}

		
	}

	/**
	 * PUT /analyze-orders : Updates an existing analyzeOrder.
	 *
	 * @param analyzeOrder the analyzeOrder to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         analyzeOrder, or with status 400 (Bad Request) if the analyzeOrder is
	 *         not valid, or with status 500 (Internal Server Error) if the
	 *         analyzeOrder couldn't be updated
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
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, analyzeOrder.getId().toString())).body(result);
	}

	/**
	 * GET /analyze-orders : get all the analyzeOrders.
	 *
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of analyzeOrders
	 *         in body
	 */
	@GetMapping("/analyze-orders")
	@Timed
	public ResponseEntity<List<AnalyzeOrder>> getAllAnalyzeOrders(Pageable pageable) {
		log.debug("REST request to get a page of AnalyzeOrders");
		Page<AnalyzeOrder> page = analyzeOrderRepository.getActiveItem(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/analyze-orders");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /analyze-orders/:id : get the "id" analyzeOrder.
	 *
	 * @param id the id of the analyzeOrder to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         analyzeOrder, or with status 404 (Not Found)
	 */
	@GetMapping("/analyze-orders/{id}")
	@Timed
	public ResponseEntity<AnalyzeOrder> getAnalyzeOrder(@PathVariable Long id) {
		log.debug("REST request to get AnalyzeOrder : {}", id);
		AnalyzeOrder analyzeOrder = analyzeOrderRepository.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(analyzeOrder));
	}

	/**
	 * DELETE /analyze-orders/:id : delete the "id" analyzeOrder.
	 *
	 * @param id the id of the analyzeOrder to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/analyze-orders/{id}")
	@Timed
	public ResponseEntity<Void> deleteAnalyzeOrder(@PathVariable Long id) {
		log.debug("REST request to delete AnalyzeOrder : {}", id);
		AnalyzeOrder analyzeOrder = analyzeOrderRepository.findOne(id);
		analyzeOrder.setActive(false);
        analyzeOrderRepository.save(analyzeOrder);
		//analyzeOrderRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	@GetMapping("/analyze-orders/checkUnprocessedOrders")
	@Timed
	public ResponseEntity<Void> checkUnprocessedOrdersServlet() throws ParseException {
		checkUnprocessedOrders();
		return ResponseEntity.ok().build();
	}

	@GetMapping("/analyze-orders/checUnprocessedRawRecords/{id}")
	@Timed
	public ResponseEntity<Void> checUnprocessedRawRecords(@PathVariable Long id) throws ParseException {
		checkUnprocessedOrders();
		return ResponseEntity.ok().build();
	}

//    @GetMapping("/analyze-orders/scriptTest")
//    @Timed
//    public ResponseEntity<Void> scriptTest() {
//    	linuxCommandService.startAIScript("30");
//    	return ResponseEntity.ok().build();
//    }

	@GetMapping("/analyze-orders/getAllSummary")
	@Timed
	public List<AnalyzeOrderSummaryVM> getAllSummary(Pageable pageable) {

		List<AnalyzeOrderSummaryVM> result = new ArrayList<AnalyzeOrderSummaryVM>();
		;

		Page<AnalyzeOrder> page = analyzeOrderRepository.findAll(pageable);
		for (Iterator iterator = page.getContent().iterator(); iterator.hasNext();) {
			AnalyzeOrder analyzeOrder = (AnalyzeOrder) iterator.next();

			AnalyzeOrderSummaryVM item = new AnalyzeOrderSummaryVM();
			item.setId(analyzeOrder.getId());
			item.setScenarioPath(analyzeOrder.getScenario().getName());
			item.setState(analyzeOrder.getOrderDetails().getState().toString());
			item.setVideoName(analyzeOrder.getVideo().getName());
			item.setVideoPath(analyzeOrder.getVideo().getPath());
			item.setScenarioId(analyzeOrder.getScenario().getId());
			if (analyzeOrder.getOrderDetails().getStartDate() != null)
				item.setStartDate(DATE_TIME_FORMATTER.format(analyzeOrder.getOrderDetails().getStartDate()));

			if (analyzeOrder.getOrderDetails().getEndDate() != null)
				item.setEndDate(DATE_TIME_FORMATTER.format(analyzeOrder.getOrderDetails().getEndDate()));
			result.add(item);
		}

		return result;
	}

	@GetMapping("/analyze-orders/search")
	@Timed
	public List<AnalyzeOrder> search(@RequestParam(value = "locationId") Long locationId,@RequestParam(value = "startDate") Instant startDate,
			@RequestParam(value = "endDate") Instant endDate) {
		log.debug("REST request to get a page of AnalyzeOrders");
		List<AnalyzeOrder> page = analyzeOrderRepository.search(locationId,startDate,endDate);
		
		return page;
	}
	
	@GetMapping("/analyze-orders/getLogs/{id}")
	@Timed
	public ParameterVM getLogs(@PathVariable Long id) {

		String value ="";
		AnalyzeOrder analyzeOrder = analyzeOrderRepository.findOne(id);
		if(analyzeOrder == null) {
			value = "order bulunamadi";
		}else {
			value = getFileContent(analyzeOrder.getOrderDetails().getSessionId());
			
		}
		
		ParameterVM result = new ParameterVM();
		result.setValue(value);
		return result;
	}
	

	@GetMapping("/analyze-orders/play/{id}")
	@Timed
	public void play(@PathVariable Long id) {

		System.out.println(id);
		String[] cmd = { "java -jar", 
    			"/home/ramazan/Desktop/trafix/trafixviewer-0.0.1-SNAPSHOT.jar "+ id, 
				};
		try {
			Process p = Runtime.getRuntime().exec("java -jar "+ applicationProperties.getTrafixViewerJarLocation()+" "+ id);
			//getOutput(p);
			log.info("java"+ " script called");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error("java"+ " script called but there is errror",e);
		}
	}
	
	private List<String> getOutput(Process p) throws IOException {
		
		 List<String> result = new ArrayList<String>();
		
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

		BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

		String s = null;
		// read the output from the command
		System.out.println("Here is the standard output of the command:\n");
		while ((s = stdInput.readLine()) != null) {
			log.info(s);
			result.add(s);
		}

		// read any errors from the attempted command
		System.out.println("Here is the standard error of the command (if any):\n");
		while ((s = stdError.readLine()) != null) {
			System.out.println(s);
		}
		
		return result;
	}
	
	public String getFileContent(String sessionId){
		String path = applicationProperties.getLogDirectory()+"/"+"sessionId_"+sessionId+".log";
		File logFile = new File(path);
		boolean exists = logFile.exists();
		if(!exists) {
			return "Log Dosyası bulunamadı";
		}
		
		String result= "";
		try
        {
			result = new String ( Files.readAllBytes( Paths.get(path) ) );
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
            result = e.getMessage();
        }

		return result;
	}
	
	
	@Scheduled(fixedRate = 60000)
	public void checkUnprocessedOrders() throws ParseException {

		log.info("checkUnprocessedOrders" + " started");
		tranfserFromRawToVideoRecordTable(AnalyzeState.ANALYZE_COMPLETED);

		log.info("checkUnprocessedOrders" + " ended");

	}
	
	@Scheduled(fixedRate = 180000)
	public void processFromQuene() throws Exception {

		log.info("processFromQuene" + " started");
		
		List<AnalyzeOrder> analyzeOrderFromQueneList = analyzeOrderRepository.findFirstFromQuene(AnalyzeState.WAITING_ON_QUEBE);
		if(analyzeOrderFromQueneList.size()>0) {
			AnalyzeOrder analyzeOrderFromQuene = analyzeOrderFromQueneList.get(0);
			URL url = new URL(applicationProperties.getAiScriptEndpoint());
			
			Boolean isServerAvaible = Util.checkPortInUse(url.getHost(), url.getPort());
			if(isServerAvaible) {
				linuxCommandService.startScriptByHttp(analyzeOrderFromQuene.getOrderDetails().getSessionId());
				
				analyzeOrderFromQuene.setState(AnalyzeState.NOT_STARTED);
				analyzeOrderRepository.save(analyzeOrderFromQuene);
				
				analyzeOrderFromQuene.getOrderDetails().setState(AnalyzeState.NOT_STARTED);
				analyzeOrderDetailsRepository.save(analyzeOrderFromQuene.getOrderDetails());
				
				log.info("processFromQuene kuyruktan iş başlatıldı");
			}else {
				log.info("processFromQuene sunucu mevcut degil. analiz devam ediyor olabilir");
			}
		}else {
			log.info("processFromQuene kuyrukta kayit bulunmadi");
		}
		
		log.info("processFromQuene" + " ended");

	}
	
	
	@Scheduled(fixedRate = 60000)
	public void checkUnprocessedOrdersForLongProcess() throws ParseException {

		log.info("checkUnprocessedOrdersForLongProcess" + " started");
		tranfserFromRawToVideoRecordTable(AnalyzeState.TRANSFER_STARTED);

		log.info("checkUnprocessedOrdersForLongProcess" + " ended");

	}
	

	public void tranfserFromRawToVideoRecordTable(AnalyzeState analyzeState) throws ParseException {

		log.info("tranfserFromRawToVideoRecordTable" + " started");
		List<AnalyzeOrder> list = analyzeOrderRepository.findByState(analyzeState);
		Pageable pageRequest = new PageRequest(0, 5000);
		Map<String, Line> lines = prepareLineList();
		Map<String, Direction> directions = prepareDirectionList();
		
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			AnalyzeOrder analyzeOrder = (AnalyzeOrder) iterator.next();
			AnalyzeOrderDetails analyzeOrderDetails = analyzeOrder.getOrderDetails();
			String sessionId = analyzeOrder.getOrderDetails().getSessionId().toString();
			updateAnalyzeOrderDetails(analyzeOrderDetails, AnalyzeState.TRANSFER_STARTED);
			
			if(analyzeOrderDetails.getStartDate()==null) {
				System.out.println("startdate = null");
			}

			if(analyzeOrderDetails.getEndDate()==null) {
				System.out.println("enddate = null");
			}

			
			Long unProcessedRecordCount = rawRepository.getCountBySessionId(sessionId, false);
//			while (unProcessedRecordCount>0) {
//				Page<RawRecord> rawRecords = rawRepository.findBySessionId(pageRequest,sessionId,false);
//				transferData(rawRecords, analyzeOrder, lines);
//				unProcessedRecordCount = rawRepository.getCountBySessionId(sessionId, false);
//			}
//			
//			updateAnalyzeOrderDetails(analyzeOrderDetails, AnalyzeState.PROCESS_COMPLETED);
			
			// FOR 
			Page<RawRecord> rawRecords = rawRepository.findBySessionId(pageRequest,sessionId,false);
			transferData(rawRecords, analyzeOrder, lines,directions);
			unProcessedRecordCount = rawRepository.getCountBySessionId(sessionId, false);
			if(unProcessedRecordCount ==0)
				updateAnalyzeOrderDetails(analyzeOrderDetails, AnalyzeState.PROCESS_COMPLETED);

		}

		log.info("checkUnprocessedOrders" + " ended");
	}
	
	private String getRegionIdFromRowRecord(String value) {
		if(value.contains("-")) {
			String[] items = value.split("-");
			return items[0];		
		}else {
			return value;
		}
	}

	private void transferData(Page<RawRecord> rawRecords, AnalyzeOrder analyzeOrder, Map<String, Line> lines,Map<String, Direction> directions)
			throws ParseException {

		List<VideoRecord> videoRecords = new ArrayList<VideoRecord>();
		List<RawRecord> rawRecordList = new ArrayList<RawRecord>();

		for (Iterator iterator2 = rawRecords.iterator(); iterator2.hasNext();) {
			RawRecord rawRecord = (RawRecord) iterator2.next();
			String entry = getRegionIdFromRowRecord(rawRecord.getEntry());
			String exit = getRegionIdFromRowRecord(rawRecord.getExit());
			
			
			try {
				Line line = lines.get(entry + "-" + exit);
				Direction direction = directions.get(entry + "-" + exit);
				VideoRecord videoRecord = insertVideoRecord(rawRecord, analyzeOrder, line,direction);
				videoRecords.add(videoRecord);

				rawRecord.setMoved(true);
				rawRecordList.add(rawRecord);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.error("checkUnprocessedOrders : transferData" + videoRecords.size() + "error",e);
			}
		}

		if (videoRecords.size() > 0)
			videoRecordRepository.save(videoRecords);
		if (rawRecordList.size() > 0)
			rawRepository.save(rawRecordList);

		log.info("checkUnprocessedOrders : transferData" + videoRecords.size() + " inserted to video_record");
		log.info("checkUnprocessedOrders : transferData" + rawRecordList.size() + " updated to raw_record");

	}

	private void updateAnalyzeOrderDetails(AnalyzeOrderDetails analyzeOrderDetails, AnalyzeState analyzeState) {
		analyzeOrderDetails.setState(analyzeState);
		if(analyzeOrderDetails.getStartDate()!=null && analyzeOrderDetails.getEndDate()!=null) {
			Duration res = Duration.between(analyzeOrderDetails.getStartDate(), analyzeOrderDetails.getEndDate());
			analyzeOrderDetails.setProcessDuration(res.getSeconds() / 60);
		}
		analyzeOrderDetailsRepository.save(analyzeOrderDetails);
	}

	private Map<String, Line> prepareLineList() {
		Map<String, Line> result = new HashMap<String, Line>();
		List<Line> lines = lineRepository.findAll();
		for (Iterator iterator = lines.iterator(); iterator.hasNext();) {
			Line line = (Line) iterator.next();
			result.put(line.getStartPolygon().getId() + "-" + line.getEndPolygon().getId(), line);
		}

		return result;
	}
	
	private Map<String, Direction> prepareDirectionList() {
		Map<String, Direction> result = new HashMap<String, Direction>();
		List<Direction> directions = directionRepository.findAll();
		for (Iterator iterator = directions.iterator(); iterator.hasNext();) {
			Direction direction = (Direction) iterator.next();
			result.put(direction.getStartLine().getEndPolygon().getId() + "-" + direction.getEndLine().getStartPolygon().getId(), direction);
		}

		return result;
	}

	private VideoRecord insertVideoRecord(RawRecord rawRecord, AnalyzeOrder analyzeOrder, Line line, Direction direction)
			throws ParseException {
		VideoRecord videoRecord = new VideoRecord();
		videoRecord.setAnalyze(analyzeOrder);
		
		videoRecord.setInsertDate(prepareInsertDate(rawRecord.getTime()));
		
		videoRecord.setSpeed(rawRecord.getSpeed());
		videoRecord.setVehicleType(rawRecord.getObjectType());
		videoRecord.setLine(line);
		videoRecord.setDirection(direction);
		videoRecord.setDuration(prepareDuration(rawRecord.getTime()));
		return videoRecord;

	}

	private Instant prepareInsertDate(String dateValue) throws ParseException {
		try {
			if(dateValue.length()<15)
				dateValue = dateValue+".000";
			String temp = dateValue.substring(0, 12);
			Date date = sdf.parse("2000-01-01 " + temp);
			return date.toInstant();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Instant.now();
		}
	}

	private Long prepareDuration(String dateValue) {

		Long result = -1l;
//    	if(dateValue.length()==14)
//    	 	dateValue = dateValue.substring(0,11);
//    	 else if(dateValue.length()==7) {
//    		dateValue = dateValue+".000";
//    	 }

		// Date date = sdf.parse("1970-01-01 0"+dateValue);
		// return date.getTime();

		if (dateValue.length() == 8)
			dateValue = dateValue + ".000";
		else if (dateValue.length() == 15)
			dateValue = dateValue.substring(0, 12);

		try {
			Date start = sdf.parse("2000-01-01 00:00:00.000");
			Date end = sdf.parse("2000-01-01 " + dateValue);

			Long sa = end.getTime() - start.getTime();

			return sa;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}
	
	@GetMapping("/analyze-orders/getScreenShoot/{id}")
    public @ResponseBody void getScreenShoot(@PathVariable Long id,HttpServletResponse response) throws IOException {
    	AnalyzeOrder analyzeOrder = analyzeOrderRepository.findOne(id);
    	InputStream targetStream = new ByteArrayInputStream(analyzeOrder.getScreenShoot());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        IOUtils.copy(targetStream, response.getOutputStream());
    }

}
