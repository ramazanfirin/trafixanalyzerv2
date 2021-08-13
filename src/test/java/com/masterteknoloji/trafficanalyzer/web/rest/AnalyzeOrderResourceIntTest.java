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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.masterteknoloji.trafficanalyzer.Trafficanalzyzerv2App;
import com.masterteknoloji.trafficanalyzer.config.ApplicationProperties;
import com.masterteknoloji.trafficanalyzer.domain.AnalyzeOrder;
import com.masterteknoloji.trafficanalyzer.domain.enumeration.AnalyzeState;
import com.masterteknoloji.trafficanalyzer.repository.AnalyzeOrderDetailsRepository;
import com.masterteknoloji.trafficanalyzer.repository.AnalyzeOrderRepository;
import com.masterteknoloji.trafficanalyzer.repository.LineRepository;
import com.masterteknoloji.trafficanalyzer.repository.PolygonRepository;
import com.masterteknoloji.trafficanalyzer.repository.RawRecordRepository;
import com.masterteknoloji.trafficanalyzer.repository.VideoRecordRepository;
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
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
//        final AnalyzeOrderResource analyzeOrderResource = new AnalyzeOrderResource(analyzeOrderRepository,objectMapper,lineRepository,polygonRepository,
//        		analyzeOrderDetailsRepository,analyzeOrderDetailsRepository,rawRepository, videoRecordRepository);
//       
        AnalyzeOrderResource analyzeOrderResource = new AnalyzeOrderResource(analyzeOrderRepository, objectMapper, lineRepository, polygonRepository, 
        		analyzeOrderDetailsRepository, rawRepository, videoRecordRepository,applicationProperties, linuxCommandService);
        
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

    @Test
    @Transactional
    public void createAnalyzeOrder() throws Exception {
        int databaseSizeBeforeCreate = analyzeOrderRepository.findAll().size();

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
