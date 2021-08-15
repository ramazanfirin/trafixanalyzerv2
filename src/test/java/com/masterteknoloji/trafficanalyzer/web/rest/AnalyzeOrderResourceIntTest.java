package com.masterteknoloji.trafficanalyzer.web.rest;

import static com.masterteknoloji.trafficanalyzer.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.masterteknoloji.trafficanalyzer.Trafficanalzyzerv2App;
import com.masterteknoloji.trafficanalyzer.config.ApplicationProperties;
import com.masterteknoloji.trafficanalyzer.domain.AnalyzeOrder;
import com.masterteknoloji.trafficanalyzer.domain.AnalyzeOrderDetails;
import com.masterteknoloji.trafficanalyzer.domain.Direction;
import com.masterteknoloji.trafficanalyzer.domain.Line;
import com.masterteknoloji.trafficanalyzer.domain.Polygon;
import com.masterteknoloji.trafficanalyzer.domain.RawRecord;
import com.masterteknoloji.trafficanalyzer.domain.Scenario;
import com.masterteknoloji.trafficanalyzer.domain.Video;
import com.masterteknoloji.trafficanalyzer.domain.VideoRecord;
import com.masterteknoloji.trafficanalyzer.domain.enumeration.AnalyzeState;
import com.masterteknoloji.trafficanalyzer.domain.enumeration.PolygonType;
import com.masterteknoloji.trafficanalyzer.repository.AnalyzeOrderDetailsRepository;
import com.masterteknoloji.trafficanalyzer.repository.AnalyzeOrderRepository;
import com.masterteknoloji.trafficanalyzer.repository.DirectionRepository;
import com.masterteknoloji.trafficanalyzer.repository.LineRepository;
import com.masterteknoloji.trafficanalyzer.repository.PolygonRepository;
import com.masterteknoloji.trafficanalyzer.repository.RawRecordRepository;
import com.masterteknoloji.trafficanalyzer.repository.ScenarioRepository;
import com.masterteknoloji.trafficanalyzer.repository.VideoRecordRepository;
import com.masterteknoloji.trafficanalyzer.repository.VideoRepository;
import com.masterteknoloji.trafficanalyzer.service.LinuxCommandService;
import com.masterteknoloji.trafficanalyzer.web.rest.errors.ExceptionTranslator;
import com.masterteknoloji.trafficanalyzer.web.rest.vm.analyzeorderdetails.AnalyzeOrderDetailVM;
import com.masterteknoloji.trafficanalyzer.web.rest.vm.analyzeorderdetails.ConnectionVM;
import com.masterteknoloji.trafficanalyzer.web.rest.vm.analyzeorderdetails.DirectionVM;
import com.masterteknoloji.trafficanalyzer.web.rest.vm.analyzeorderdetails.PointsVM;
import com.masterteknoloji.trafficanalyzer.web.rest.vm.analyzeorderdetails.RegionVM;
import com.masterteknoloji.trafficanalyzer.web.rest.vm.analyzeorderdetails.SpeedVM;
import com.masterteknoloji.trafficanalyzer.web.rest.vm.analyzeorderdetails.VehicleTypeVM;
/**
 * Test class for the AnalyzeOrderResource REST controller.
 *
 * @see AnalyzeOrderResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Trafficanalzyzerv2App.class)
public class AnalyzeOrderResourceIntTest {

    private static final AnalyzeState DEFAULT_STATE = AnalyzeState.NOT_STARTED;
    private static final AnalyzeState UPDATED_STATE = AnalyzeState.STARTED;

    private static final byte[] DEFAULT_SCREEN_SHOOT = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_SCREEN_SHOOT = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_SCREEN_SHOOT_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_SCREEN_SHOOT_CONTENT_TYPE = "image/png";

    @Autowired
    private AnalyzeOrderRepository analyzeOrderRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAnalyzeOrderMockMvc;

    private AnalyzeOrder analyzeOrder;

    @Autowired
    ObjectMapper objectMapper; 
    
    @Autowired
    LineRepository lineRepository; 
	
    @Autowired
    PolygonRepository polygonRepository;
    
    @Autowired
    AnalyzeOrderDetailsRepository analyzeOrderDetailsRepository;
    
    @Autowired
    RawRecordRepository rawRepository;
    
    @Autowired
    VideoRecordRepository videoRecordRepository;
    
    @Autowired
	ApplicationProperties applicationProperties;

    @Autowired
	LinuxCommandService linuxCommandService;
    
    @Autowired
    DirectionRepository directionRepository;
    
    @Autowired
    ScenarioRepository scenarioRepository;
    
    @Autowired
    VideoRepository videoRepository;
    
    @Autowired
    RawRecordRepository rawRecordRepository;
    
    @Autowired
    VideoRecordRepository recordRepository; 
    
    Scenario scenario;
    
    Polygon startPolygon =new Polygon();
    Polygon endPolygon =new Polygon();
    Polygon startPolygon2 =new Polygon();
    Polygon endPolygon2 =new Polygon();
    Polygon startPolygon3 =new Polygon();
    Polygon endPolygon3 =new Polygon();
    
    Polygon speedPolygon =new Polygon();
    
    Line line1;
    Line line2;
    Line line3;
    
    Direction direction;
    
    Video video = new Video();
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
//        final AnalyzeOrderResource analyzeOrderResource = new AnalyzeOrderResource(analyzeOrderRepository,objectMapper,lineRepository,polygonRepository,
//        		analyzeOrderDetailsRepository,analyzeOrderDetailsRepository,rawRepository, videoRecordRepository);
//       
        AnalyzeOrderResource analyzeOrderResource = new AnalyzeOrderResource(analyzeOrderRepository, objectMapper, lineRepository, polygonRepository, 
        		analyzeOrderDetailsRepository, rawRepository, videoRecordRepository,applicationProperties, linuxCommandService,directionRepository);
        
        this.restAnalyzeOrderMockMvc = MockMvcBuilders.standaloneSetup(analyzeOrderResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AnalyzeOrder createEntity(EntityManager em) {
        AnalyzeOrder analyzeOrder = new AnalyzeOrder()
            .state(DEFAULT_STATE)
            .screenShoot(DEFAULT_SCREEN_SHOOT)
            .screenShootContentType(DEFAULT_SCREEN_SHOOT_CONTENT_TYPE);
        return analyzeOrder;
    }

    @Before
    public void initTest() {
        analyzeOrder = createEntity(em);
    }
    
    public void prepareData() {
    	 prepareScenarioList();
    	 prepareVideoList();
         preparePolygonList();
         prepareLineList();
         prepareDirectionList();
         prepareSpeedList();
    }
    
    public void prepareScenarioList() {
    	scenario = new Scenario();
        scenarioRepository.save(scenario);
    }
    
    public void prepareVideoList() {
    	video = new Video();
    	video.setName("name");
    	video.setPath("path");
    	videoRepository.save(video);
    }
    
    public void preparePolygonList() {
    	startPolygon =new Polygon();
    	startPolygon.setName("startPolygon");
    	startPolygon.setPoints("0,0;100,200");
    	startPolygon.setScenario(scenario);
    	polygonRepository.save(startPolygon);
    	
    	endPolygon.setName("endPolygon");
    	endPolygon.setPoints("0,0;300,400");
    	endPolygon.setScenario(scenario);
    	polygonRepository.save(endPolygon);
    	
    	startPolygon2 =new Polygon();
    	startPolygon2.setName("startstartPolygon2");
    	startPolygon2.setPoints("0,0;500,600");
    	startPolygon2.setScenario(scenario);
    	polygonRepository.save(startPolygon2);
    	
    	endPolygon2 =new Polygon();
    	endPolygon2.setName("endPolygon2");
    	endPolygon2.setPoints("0,0;700,800");
    	endPolygon2.setScenario(scenario);
    	polygonRepository.save(endPolygon2);
    	
    	startPolygon3 =new Polygon();
    	startPolygon3.setName("startstartPolygon3");
    	startPolygon3.setPoints("0,0;900,1000");
    	startPolygon3.setScenario(scenario);
    	polygonRepository.save(startPolygon3);
    	
    	endPolygon3 =new Polygon();
    	endPolygon3.setName("endPolygon3");
    	endPolygon3.setPoints("0,0;1000,1100");
    	endPolygon3.setScenario(scenario);
    	polygonRepository.save(endPolygon3);
    }
    
    public void prepareLineList() {
    	line1 = new Line();
    	line1.setStartPolygon(startPolygon);
    	line1.setEndPolygon(endPolygon);
    	line1.setName("line1");
    	line1.setScenario(scenario);
    	lineRepository.save(line1);
    	
    	line2 = new Line();
    	line2.setStartPolygon(startPolygon2);
    	line2.setEndPolygon(endPolygon2);
    	line2.setName("line1");
    	line2.setScenario(scenario);
    	lineRepository.save(line2);
    	
    	line3 = new Line();
    	line3.setStartPolygon(startPolygon3);
    	line3.setEndPolygon(endPolygon3);
    	line3.setName("line3");
    	line3.setScenario(scenario);
    	lineRepository.save(line3);
    }
    
    public void prepareDirectionList() {
    	direction = new Direction();
    	direction.setStartLine(line1);
    	direction.setEndLine(line3);
    	direction.setName("line-to-line3");
    	direction.setScenario(scenario);
    	directionRepository.save(direction);
    }
    
    public void prepareSpeedList() {
    	speedPolygon = new Polygon();
    	speedPolygon.setName("speed");
    	speedPolygon.setPoints("0,0;100,500");
    	speedPolygon.setWidth(20);
    	speedPolygon.setScenario(scenario);
    	speedPolygon.setType(PolygonType.SPEED);
    	polygonRepository.save(speedPolygon);
    }
    
    
    @Test
    @Transactional
    public void createAnalyzeOrder() throws Exception {
        int databaseSizeBeforeCreate = analyzeOrderRepository.findAll().size();

       
        prepareData();
        analyzeOrder.setVideo(video);
        analyzeOrder.setScenario(scenario);
        
        // Create the AnalyzeOrder
        restAnalyzeOrderMockMvc.perform(post("/api/analyze-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(analyzeOrder)))
            .andExpect(status().isCreated());

        // Validate the AnalyzeOrder in the database
        List<AnalyzeOrder> analyzeOrderList = analyzeOrderRepository.findAll();
        assertThat(analyzeOrderList).hasSize(databaseSizeBeforeCreate + 1);
        AnalyzeOrder testAnalyzeOrder = analyzeOrderList.get(analyzeOrderList.size() - 1);
        assertThat(testAnalyzeOrder.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testAnalyzeOrder.getScreenShoot()).isEqualTo(DEFAULT_SCREEN_SHOOT);
        assertThat(testAnalyzeOrder.getScreenShootContentType()).isEqualTo(DEFAULT_SCREEN_SHOOT_CONTENT_TYPE);
    
        AnalyzeOrderDetails analyzeOrderDetails = analyzeOrderList.get(0).getOrderDetails();
        assertThat(analyzeOrderDetails).isNotNull();
    
        DirectionVM directionVM = objectMapper.readValue(analyzeOrderDetails.getDirections(), DirectionVM.class);
        List<RegionVM> regionVMs =directionVM.getRegions();
        List<ConnectionVM> connectionVMs =directionVM.getConnections();
        
        assertThat(regionVMs.size()).isEqualTo(6);
        assertThat(connectionVMs.size()).isEqualTo(4);
        
        assertThat(regionVMs.get(0).getLabel()).isEqualTo(startPolygon.getId().toString());
        assertThat(regionVMs.get(1).getLabel()).isEqualTo(endPolygon.getId().toString());
        assertThat(regionVMs.get(2).getLabel()).isEqualTo(startPolygon2.getId().toString());
        assertThat(regionVMs.get(3).getLabel()).isEqualTo(endPolygon2.getId().toString());
        assertThat(regionVMs.get(4).getLabel()).isEqualTo(startPolygon3.getId().toString());
        assertThat(regionVMs.get(5).getLabel()).isEqualTo(endPolygon3.getId().toString());
        
        assertThat(connectionVMs.get(0).getEntry()).isEqualTo(startPolygon.getId().toString());
        assertThat(connectionVMs.get(0).getExit()).isEqualTo(endPolygon.getId().toString());
        
        assertThat(connectionVMs.get(1).getEntry()).isEqualTo(startPolygon2.getId().toString());
        assertThat(connectionVMs.get(1).getExit()).isEqualTo(endPolygon2.getId().toString());
        
        assertThat(connectionVMs.get(2).getEntry()).isEqualTo(startPolygon3.getId().toString());
        assertThat(connectionVMs.get(2).getExit()).isEqualTo(endPolygon3.getId().toString());
        
        //from directions
        
        assertThat(connectionVMs.get(3).getEntry()).isEqualTo(direction.getStartLine().getStartPolygon().getId().toString());
        assertThat(connectionVMs.get(3).getExit()).isEqualTo(direction.getEndLine().getEndPolygon().getId().toString());
        

        List<SpeedVM> speedVMs = objectMapper.readValue(analyzeOrderDetails.getSpeed(), new TypeReference<List<SpeedVM>>() { });
        assertThat(speedVMs.size()).isEqualTo(1);
        assertThat(speedVMs.get(0).getDistance()).isEqualTo(20);
        
        System.out.println("bitti");
    }

    
    @Test
    @Transactional
    public void checkUnprocessedDataForLine() throws Exception {
               
        prepareData();
        analyzeOrder.setVideo(video);
        analyzeOrder.setScenario(scenario);
        
     // Create the AnalyzeOrder
        restAnalyzeOrderMockMvc.perform(post("/api/analyze-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(analyzeOrder)))
            .andExpect(status().isCreated());
        
        analyzeOrder = analyzeOrderRepository.findAll().get(0);
        analyzeOrder.getOrderDetails().setState(AnalyzeState.ANALYZE_COMPLETED);
        analyzeOrderRepository.save(analyzeOrder);
        
        RawRecord rawRecord = new RawRecord();
        rawRecord.setEntry(startPolygon.getId().toString());
        rawRecord.setExit(endPolygon.getId().toString());
        rawRecord.setMoved(false);
        rawRecord.setObjectType("car");
        rawRecord.setSessionID(analyzeOrder.getOrderDetails().getSessionId());
        rawRecord.setSpeed(100d);
        rawRecord.setTime("00:00:59.333333");
        rawRecordRepository.save(rawRecord);
        
        Long unProcessedRecordCount = rawRepository.getCountBySessionId(analyzeOrder.getOrderDetails().getSessionId(), false);
        assertThat(unProcessedRecordCount).isEqualTo(1);
        
        restAnalyzeOrderMockMvc.perform(get("/api/analyze-orders/checUnprocessedRawRecords/"+analyzeOrder.getId()))
        .andExpect(status().isOk());

        unProcessedRecordCount = rawRepository.getCountBySessionId(analyzeOrder.getOrderDetails().getSessionId(), false);
        assertThat(unProcessedRecordCount).isEqualTo(0);
    
        analyzeOrder = analyzeOrderRepository.findOne(analyzeOrder.getId());
        assertThat(analyzeOrder.getOrderDetails().getState()).isEqualTo(AnalyzeState.PROCESS_COMPLETED);
        
        List<VideoRecord> videoRecords = recordRepository.findAll(); 
        assertThat(videoRecords.size()).isEqualTo(1);
        
        VideoRecord videoRecord = videoRecords.get(0);
        assertThat(videoRecord.getSpeed()).isEqualTo(100d);
        assertThat(videoRecord.getVehicleType()).isEqualTo("car");
        assertThat(videoRecord.getAnalyze().getId()).isEqualTo(analyzeOrder.getId());
        assertThat(videoRecord.getDuration()).isEqualTo(59333);
        assertThat(videoRecord.getProcessType()).isNull();
        assertThat(videoRecord.getLine().getId()).isEqualTo(line1.getId());
        assertThat(videoRecord.getDirection()).isNull();
    }
    

    @Test
    @Transactional
    public void checkUnprocessedDataForDirection() throws Exception {
               
        prepareData();
        analyzeOrder.setVideo(video);
        analyzeOrder.setScenario(scenario);
        
     // Create the AnalyzeOrder
        restAnalyzeOrderMockMvc.perform(post("/api/analyze-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(analyzeOrder)))
            .andExpect(status().isCreated());
        
        analyzeOrder = analyzeOrderRepository.findAll().get(0);
        analyzeOrder.getOrderDetails().setState(AnalyzeState.ANALYZE_COMPLETED);
        analyzeOrderRepository.save(analyzeOrder);
        
        RawRecord rawRecord = new RawRecord();
        rawRecord.setEntry(startPolygon.getId().toString());
        rawRecord.setExit(endPolygon3.getId().toString());
        rawRecord.setMoved(false);
        rawRecord.setObjectType("car");
        rawRecord.setSessionID(analyzeOrder.getOrderDetails().getSessionId());
        rawRecord.setSpeed(100d);
        rawRecord.setTime("00:00:59.333333");
        rawRecordRepository.save(rawRecord);
        
        
        Long unProcessedRecordCount = rawRepository.getCountBySessionId(analyzeOrder.getOrderDetails().getSessionId(), false);
        assertThat(unProcessedRecordCount).isEqualTo(1);
        
        restAnalyzeOrderMockMvc.perform(get("/api/analyze-orders/checUnprocessedRawRecords/"+analyzeOrder.getId()))
        .andExpect(status().isOk());

        unProcessedRecordCount = rawRepository.getCountBySessionId(analyzeOrder.getOrderDetails().getSessionId(), false);
        assertThat(unProcessedRecordCount).isEqualTo(0);
    
        analyzeOrder = analyzeOrderRepository.findOne(analyzeOrder.getId());
        assertThat(analyzeOrder.getOrderDetails().getState()).isEqualTo(AnalyzeState.PROCESS_COMPLETED);
        
        List<VideoRecord> videoRecords = recordRepository.findAll(); 
        assertThat(videoRecords.size()).isEqualTo(1);
        
        VideoRecord videoRecord = videoRecords.get(0);
        assertThat(videoRecord.getSpeed()).isEqualTo(100d);
        assertThat(videoRecord.getVehicleType()).isEqualTo("car");
        assertThat(videoRecord.getAnalyze().getId()).isEqualTo(analyzeOrder.getId());
        assertThat(videoRecord.getDuration()).isEqualTo(59333);
        assertThat(videoRecord.getProcessType()).isNull();
        assertThat(videoRecord.getDirection().getId()).isEqualTo(direction.getId());
        assertThat(videoRecord.getLine()).isNull();
    }

    
    @Test
    @Transactional
    public void createAnalyzeOrderWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = analyzeOrderRepository.findAll().size();

        // Create the AnalyzeOrder with an existing ID
        analyzeOrder.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAnalyzeOrderMockMvc.perform(post("/api/analyze-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(analyzeOrder)))
            .andExpect(status().isBadRequest());

        // Validate the AnalyzeOrder in the database
        List<AnalyzeOrder> analyzeOrderList = analyzeOrderRepository.findAll();
        assertThat(analyzeOrderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllAnalyzeOrders() throws Exception {
        // Initialize the database
        analyzeOrderRepository.saveAndFlush(analyzeOrder);

        // Get all the analyzeOrderList
        restAnalyzeOrderMockMvc.perform(get("/api/analyze-orders?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(analyzeOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].screenShootContentType").value(hasItem(DEFAULT_SCREEN_SHOOT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].screenShoot").value(hasItem(Base64Utils.encodeToString(DEFAULT_SCREEN_SHOOT))));
    }

    @Test
    @Transactional
    public void getAnalyzeOrder() throws Exception {
        // Initialize the database
        analyzeOrderRepository.saveAndFlush(analyzeOrder);

        // Get the analyzeOrder
        restAnalyzeOrderMockMvc.perform(get("/api/analyze-orders/{id}", analyzeOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(analyzeOrder.getId().intValue()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.screenShootContentType").value(DEFAULT_SCREEN_SHOOT_CONTENT_TYPE))
            .andExpect(jsonPath("$.screenShoot").value(Base64Utils.encodeToString(DEFAULT_SCREEN_SHOOT)));
    }

    @Test
    @Transactional
    public void getNonExistingAnalyzeOrder() throws Exception {
        // Get the analyzeOrder
        restAnalyzeOrderMockMvc.perform(get("/api/analyze-orders/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAnalyzeOrder() throws Exception {
        // Initialize the database
        analyzeOrderRepository.saveAndFlush(analyzeOrder);
        int databaseSizeBeforeUpdate = analyzeOrderRepository.findAll().size();

        // Update the analyzeOrder
        AnalyzeOrder updatedAnalyzeOrder = analyzeOrderRepository.findOne(analyzeOrder.getId());
        // Disconnect from session so that the updates on updatedAnalyzeOrder are not directly saved in db
        em.detach(updatedAnalyzeOrder);
        updatedAnalyzeOrder
            .state(UPDATED_STATE)
            .screenShoot(UPDATED_SCREEN_SHOOT)
            .screenShootContentType(UPDATED_SCREEN_SHOOT_CONTENT_TYPE);

        restAnalyzeOrderMockMvc.perform(put("/api/analyze-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAnalyzeOrder)))
            .andExpect(status().isOk());

        // Validate the AnalyzeOrder in the database
        List<AnalyzeOrder> analyzeOrderList = analyzeOrderRepository.findAll();
        assertThat(analyzeOrderList).hasSize(databaseSizeBeforeUpdate);
        AnalyzeOrder testAnalyzeOrder = analyzeOrderList.get(analyzeOrderList.size() - 1);
        assertThat(testAnalyzeOrder.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testAnalyzeOrder.getScreenShoot()).isEqualTo(UPDATED_SCREEN_SHOOT);
        assertThat(testAnalyzeOrder.getScreenShootContentType()).isEqualTo(UPDATED_SCREEN_SHOOT_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingAnalyzeOrder() throws Exception {
        int databaseSizeBeforeUpdate = analyzeOrderRepository.findAll().size();

        // Create the AnalyzeOrder

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAnalyzeOrderMockMvc.perform(put("/api/analyze-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(analyzeOrder)))
            .andExpect(status().isCreated());

        // Validate the AnalyzeOrder in the database
        List<AnalyzeOrder> analyzeOrderList = analyzeOrderRepository.findAll();
        assertThat(analyzeOrderList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAnalyzeOrder() throws Exception {
        // Initialize the database
        analyzeOrderRepository.saveAndFlush(analyzeOrder);
        int databaseSizeBeforeDelete = analyzeOrderRepository.findAll().size();

        // Get the analyzeOrder
        restAnalyzeOrderMockMvc.perform(delete("/api/analyze-orders/{id}", analyzeOrder.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<AnalyzeOrder> analyzeOrderList = analyzeOrderRepository.findAll();
        assertThat(analyzeOrderList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AnalyzeOrder.class);
        AnalyzeOrder analyzeOrder1 = new AnalyzeOrder();
        analyzeOrder1.setId(1L);
        AnalyzeOrder analyzeOrder2 = new AnalyzeOrder();
        analyzeOrder2.setId(analyzeOrder1.getId());
        assertThat(analyzeOrder1).isEqualTo(analyzeOrder2);
        analyzeOrder2.setId(2L);
        assertThat(analyzeOrder1).isNotEqualTo(analyzeOrder2);
        analyzeOrder1.setId(null);
        assertThat(analyzeOrder1).isNotEqualTo(analyzeOrder2);
    }
    
    @Test
    public void jsonTest_Classes() throws JsonProcessingException{
    	String result = objectMapper.writeValueAsString(new VehicleTypeVM());
    	System.out.println(result);
    }
    
    @Test
    public void jsonTest_Points() throws JsonProcessingException{
    	PointsVM pointsVM = preparePoints(100l, 100l);
    	String result = objectMapper.writeValueAsString(pointsVM);
    	System.out.println(result);
    }
    
    @Test
    public void jsonTest_Regions() throws JsonProcessingException{
    	
    	RegionVM regionVM = prepareRegions();
    	String result = objectMapper.writeValueAsString(regionVM);
    	System.out.println(result);
    }
    
    @Test
    public void jsonTest_Connection() throws JsonProcessingException{
    	ConnectionVM connectionVM = prepareConnection();
    	
    	String result = objectMapper.writeValueAsString(connectionVM);
    	System.out.println(result);
    }
    
    @Test
    public void jsonTest_Direction() throws JsonProcessingException{
    	DirectionVM direction = prepareDirection();
    	
    	String result = objectMapper.writeValueAsString(direction);
    	System.out.println(result);
    }
    
    @Test
    public void jsonTest_Speed() throws JsonProcessingException{
    	List<SpeedVM> speed = prepareSpeed();
    	
    	String result = objectMapper.writeValueAsString(speed);
    	System.out.println(result);
    }
    
    @Test
    public void jsonTest_All() throws JsonProcessingException{
    	AnalyzeOrderDetailVM analyzeOrderDetailVM = new AnalyzeOrderDetailVM();
    	analyzeOrderDetailVM.setCount(true);
    	analyzeOrderDetailVM.setDirections(prepareDirection());
    	analyzeOrderDetailVM.setPath("videoPath");
    	analyzeOrderDetailVM.setSessionId("sessiınID");
    	analyzeOrderDetailVM.setSpeed(prepareSpeed());
    	analyzeOrderDetailVM.setClasses(new VehicleTypeVM());
    	
    	String result = objectMapper.writeValueAsString(analyzeOrderDetailVM);
    	System.out.println(result);
    }
    public PointsVM preparePoints(Long x,Long y) {
    	PointsVM pointsVM2 = new PointsVM();
    	pointsVM2.setX(x);
    	pointsVM2.setY(y);
    	return pointsVM2;
    }
    
    public RegionVM prepareRegions() {
    	RegionVM regionVM = new RegionVM();
    	regionVM.setLabel("regions1");
    	
    	regionVM.getPoints().add(preparePoints(100l, 100l));
    	regionVM.getPoints().add(preparePoints(200l,200l));
    	
    	return regionVM;
    }
    
    public ConnectionVM prepareConnection() {
    	ConnectionVM connectionVM = new ConnectionVM();
    	connectionVM.setEntry("enrty");
    	connectionVM.setExit("exit");
    	
    	return connectionVM;
    }
    
    public DirectionVM prepareDirection() {
    	DirectionVM direction = new DirectionVM();
    	List<RegionVM> regionList = new ArrayList<RegionVM>();
    	regionList.add(prepareRegions());
    	regionList.add(prepareRegions());
    	direction.setRegions(regionList);
    	
    	List<ConnectionVM> connectionList = new ArrayList<ConnectionVM>();
    	connectionList.add(prepareConnection());
     	connectionList.add(prepareConnection()); 	
    	direction.setConnections(connectionList);
    	
    	return direction;
    }
    
    public List<SpeedVM> prepareSpeed() {
    	List<SpeedVM> result = new ArrayList<SpeedVM>();
    	
    	SpeedVM speedVM = new SpeedVM();
    	
    	speedVM.setLabel("label");
    	speedVM.setDistance(100l);
    	
    	speedVM.getPoints().add(preparePoints(100l, 100l));
    	speedVM.getPoints().add(preparePoints(200l, 200l));
    	
    	result.add(speedVM);
    	return result;
    }
}
