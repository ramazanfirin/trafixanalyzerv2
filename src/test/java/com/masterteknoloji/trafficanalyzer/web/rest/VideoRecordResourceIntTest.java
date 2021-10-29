package com.masterteknoloji.trafficanalyzer.web.rest;

import com.masterteknoloji.trafficanalyzer.Trafficanalzyzerv2App;
import com.masterteknoloji.trafficanalyzer.config.ApplicationProperties;
import com.masterteknoloji.trafficanalyzer.domain.VideoRecord;
import com.masterteknoloji.trafficanalyzer.repository.AnalyzeOrderRepository;
import com.masterteknoloji.trafficanalyzer.repository.DirectionRepository;
import com.masterteknoloji.trafficanalyzer.repository.LineRepository;
import com.masterteknoloji.trafficanalyzer.repository.VideoRecordRepository;
import com.masterteknoloji.trafficanalyzer.service.UserService;
import com.masterteknoloji.trafficanalyzer.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.masterteknoloji.trafficanalyzer.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.masterteknoloji.trafficanalyzer.domain.enumeration.ProcessType;
/**
 * Test class for the VideoRecordResource REST controller.
 *
 * @see VideoRecordResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Trafficanalzyzerv2App.class)
public class VideoRecordResourceIntTest {

    private static final Instant DEFAULT_INSERT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_INSERT_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_VEHICLE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_VEHICLE_TYPE = "BBBBBBBBBB";

    private static final Long DEFAULT_DURATION = 1L;
    private static final Long UPDATED_DURATION = 2L;

    private static final Double DEFAULT_SPEED = 1D;
    private static final Double UPDATED_SPEED = 2D;

    private static final ProcessType DEFAULT_PROCESS_TYPE = ProcessType.COUNTING;
    private static final ProcessType UPDATED_PROCESS_TYPE = ProcessType.DIRECTION;

    @Autowired
    private VideoRecordRepository videoRecordRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restVideoRecordMockMvc;

    private VideoRecord videoRecord;
    
    @Autowired
    AnalyzeOrderRepository analyzeOrderRepository;
    
    @Autowired
    DirectionRepository directionRepository;
    
    @Autowired
    LineRepository lineRepository;
    
    @Autowired
    MessageSource messageSource;
    
    @Autowired
    UserService userService;
    
    @Autowired
    ApplicationProperties applicationProperties;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final VideoRecordResource videoRecordResource = new VideoRecordResource(videoRecordRepository, analyzeOrderRepository, lineRepository, messageSource, userService,directionRepository,applicationProperties);
        this.restVideoRecordMockMvc = MockMvcBuilders.standaloneSetup(videoRecordResource)
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
    public static VideoRecord createEntity(EntityManager em) {
        VideoRecord videoRecord = new VideoRecord()
            .insertDate(DEFAULT_INSERT_DATE)
            .vehicleType(DEFAULT_VEHICLE_TYPE)
            .duration(DEFAULT_DURATION)
            .speed(DEFAULT_SPEED)
            .processType(DEFAULT_PROCESS_TYPE);
        return videoRecord;
    }

    @Before
    public void initTest() {
        videoRecord = createEntity(em);
    }

    @Test
    @Transactional
    public void createVideoRecord() throws Exception {
        int databaseSizeBeforeCreate = videoRecordRepository.findAll().size();

        // Create the VideoRecord
        restVideoRecordMockMvc.perform(post("/api/video-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(videoRecord)))
            .andExpect(status().isCreated());

        // Validate the VideoRecord in the database
        List<VideoRecord> videoRecordList = videoRecordRepository.findAll();
        assertThat(videoRecordList).hasSize(databaseSizeBeforeCreate + 1);
        VideoRecord testVideoRecord = videoRecordList.get(videoRecordList.size() - 1);
        assertThat(testVideoRecord.getInsertDate()).isEqualTo(DEFAULT_INSERT_DATE);
        assertThat(testVideoRecord.getVehicleType()).isEqualTo(DEFAULT_VEHICLE_TYPE);
        assertThat(testVideoRecord.getDuration()).isEqualTo(DEFAULT_DURATION);
        assertThat(testVideoRecord.getSpeed()).isEqualTo(DEFAULT_SPEED);
        assertThat(testVideoRecord.getProcessType()).isEqualTo(DEFAULT_PROCESS_TYPE);
    }

    @Test
    @Transactional
    public void createVideoRecordWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = videoRecordRepository.findAll().size();

        // Create the VideoRecord with an existing ID
        videoRecord.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVideoRecordMockMvc.perform(post("/api/video-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(videoRecord)))
            .andExpect(status().isBadRequest());

        // Validate the VideoRecord in the database
        List<VideoRecord> videoRecordList = videoRecordRepository.findAll();
        assertThat(videoRecordList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkInsertDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = videoRecordRepository.findAll().size();
        // set the field null
        videoRecord.setInsertDate(null);

        // Create the VideoRecord, which fails.

        restVideoRecordMockMvc.perform(post("/api/video-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(videoRecord)))
            .andExpect(status().isBadRequest());

        List<VideoRecord> videoRecordList = videoRecordRepository.findAll();
        assertThat(videoRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkVehicleTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = videoRecordRepository.findAll().size();
        // set the field null
        videoRecord.setVehicleType(null);

        // Create the VideoRecord, which fails.

        restVideoRecordMockMvc.perform(post("/api/video-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(videoRecord)))
            .andExpect(status().isBadRequest());

        List<VideoRecord> videoRecordList = videoRecordRepository.findAll();
        assertThat(videoRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDurationIsRequired() throws Exception {
        int databaseSizeBeforeTest = videoRecordRepository.findAll().size();
        // set the field null
        videoRecord.setDuration(null);

        // Create the VideoRecord, which fails.

        restVideoRecordMockMvc.perform(post("/api/video-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(videoRecord)))
            .andExpect(status().isBadRequest());

        List<VideoRecord> videoRecordList = videoRecordRepository.findAll();
        assertThat(videoRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllVideoRecords() throws Exception {
        // Initialize the database
        videoRecordRepository.saveAndFlush(videoRecord);

        // Get all the videoRecordList
        restVideoRecordMockMvc.perform(get("/api/video-records?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(videoRecord.getId().intValue())))
            .andExpect(jsonPath("$.[*].insertDate").value(hasItem(DEFAULT_INSERT_DATE.toString())))
            .andExpect(jsonPath("$.[*].vehicleType").value(hasItem(DEFAULT_VEHICLE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION.intValue())))
            .andExpect(jsonPath("$.[*].speed").value(hasItem(DEFAULT_SPEED.doubleValue())))
            .andExpect(jsonPath("$.[*].processType").value(hasItem(DEFAULT_PROCESS_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getVideoRecord() throws Exception {
        // Initialize the database
        videoRecordRepository.saveAndFlush(videoRecord);

        // Get the videoRecord
        restVideoRecordMockMvc.perform(get("/api/video-records/{id}", videoRecord.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(videoRecord.getId().intValue()))
            .andExpect(jsonPath("$.insertDate").value(DEFAULT_INSERT_DATE.toString()))
            .andExpect(jsonPath("$.vehicleType").value(DEFAULT_VEHICLE_TYPE.toString()))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION.intValue()))
            .andExpect(jsonPath("$.speed").value(DEFAULT_SPEED.doubleValue()))
            .andExpect(jsonPath("$.processType").value(DEFAULT_PROCESS_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingVideoRecord() throws Exception {
        // Get the videoRecord
        restVideoRecordMockMvc.perform(get("/api/video-records/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVideoRecord() throws Exception {
        // Initialize the database
        videoRecordRepository.saveAndFlush(videoRecord);
        int databaseSizeBeforeUpdate = videoRecordRepository.findAll().size();

        // Update the videoRecord
        VideoRecord updatedVideoRecord = videoRecordRepository.findOne(videoRecord.getId());
        // Disconnect from session so that the updates on updatedVideoRecord are not directly saved in db
        em.detach(updatedVideoRecord);
        updatedVideoRecord
            .insertDate(UPDATED_INSERT_DATE)
            .vehicleType(UPDATED_VEHICLE_TYPE)
            .duration(UPDATED_DURATION)
            .speed(UPDATED_SPEED)
            .processType(UPDATED_PROCESS_TYPE);

        restVideoRecordMockMvc.perform(put("/api/video-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedVideoRecord)))
            .andExpect(status().isOk());

        // Validate the VideoRecord in the database
        List<VideoRecord> videoRecordList = videoRecordRepository.findAll();
        assertThat(videoRecordList).hasSize(databaseSizeBeforeUpdate);
        VideoRecord testVideoRecord = videoRecordList.get(videoRecordList.size() - 1);
        assertThat(testVideoRecord.getInsertDate()).isEqualTo(UPDATED_INSERT_DATE);
        assertThat(testVideoRecord.getVehicleType()).isEqualTo(UPDATED_VEHICLE_TYPE);
        assertThat(testVideoRecord.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testVideoRecord.getSpeed()).isEqualTo(UPDATED_SPEED);
        assertThat(testVideoRecord.getProcessType()).isEqualTo(UPDATED_PROCESS_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingVideoRecord() throws Exception {
        int databaseSizeBeforeUpdate = videoRecordRepository.findAll().size();

        // Create the VideoRecord

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restVideoRecordMockMvc.perform(put("/api/video-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(videoRecord)))
            .andExpect(status().isCreated());

        // Validate the VideoRecord in the database
        List<VideoRecord> videoRecordList = videoRecordRepository.findAll();
        assertThat(videoRecordList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteVideoRecord() throws Exception {
        // Initialize the database
        videoRecordRepository.saveAndFlush(videoRecord);
        int databaseSizeBeforeDelete = videoRecordRepository.findAll().size();

        // Get the videoRecord
        restVideoRecordMockMvc.perform(delete("/api/video-records/{id}", videoRecord.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<VideoRecord> videoRecordList = videoRecordRepository.findAll();
        assertThat(videoRecordList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VideoRecord.class);
        VideoRecord videoRecord1 = new VideoRecord();
        videoRecord1.setId(1L);
        VideoRecord videoRecord2 = new VideoRecord();
        videoRecord2.setId(videoRecord1.getId());
        assertThat(videoRecord1).isEqualTo(videoRecord2);
        videoRecord2.setId(2L);
        assertThat(videoRecord1).isNotEqualTo(videoRecord2);
        videoRecord1.setId(null);
        assertThat(videoRecord1).isNotEqualTo(videoRecord2);
    }
}
