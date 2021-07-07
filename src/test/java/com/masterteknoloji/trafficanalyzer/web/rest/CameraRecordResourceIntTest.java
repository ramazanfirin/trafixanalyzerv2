package com.masterteknoloji.trafficanalyzer.web.rest;

import com.masterteknoloji.trafficanalyzer.Trafficanalzyzerv2App;

import com.masterteknoloji.trafficanalyzer.domain.CameraRecord;
import com.masterteknoloji.trafficanalyzer.repository.CameraRecordRepository;
import com.masterteknoloji.trafficanalyzer.web.rest.errors.ExceptionTranslator;

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
 * Test class for the CameraRecordResource REST controller.
 *
 * @see CameraRecordResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Trafficanalzyzerv2App.class)
public class CameraRecordResourceIntTest {

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
    private CameraRecordRepository cameraRecordRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCameraRecordMockMvc;

    private CameraRecord cameraRecord;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CameraRecordResource cameraRecordResource = new CameraRecordResource(cameraRecordRepository);
        this.restCameraRecordMockMvc = MockMvcBuilders.standaloneSetup(cameraRecordResource)
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
    public static CameraRecord createEntity(EntityManager em) {
        CameraRecord cameraRecord = new CameraRecord()
            .insertDate(DEFAULT_INSERT_DATE)
            .vehicleType(DEFAULT_VEHICLE_TYPE)
            .duration(DEFAULT_DURATION)
            .speed(DEFAULT_SPEED)
            .processType(DEFAULT_PROCESS_TYPE);
        return cameraRecord;
    }

    @Before
    public void initTest() {
        cameraRecord = createEntity(em);
    }

    @Test
    @Transactional
    public void createCameraRecord() throws Exception {
        int databaseSizeBeforeCreate = cameraRecordRepository.findAll().size();

        // Create the CameraRecord
        restCameraRecordMockMvc.perform(post("/api/camera-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cameraRecord)))
            .andExpect(status().isCreated());

        // Validate the CameraRecord in the database
        List<CameraRecord> cameraRecordList = cameraRecordRepository.findAll();
        assertThat(cameraRecordList).hasSize(databaseSizeBeforeCreate + 1);
        CameraRecord testCameraRecord = cameraRecordList.get(cameraRecordList.size() - 1);
        assertThat(testCameraRecord.getInsertDate()).isEqualTo(DEFAULT_INSERT_DATE);
        assertThat(testCameraRecord.getVehicleType()).isEqualTo(DEFAULT_VEHICLE_TYPE);
        assertThat(testCameraRecord.getDuration()).isEqualTo(DEFAULT_DURATION);
        assertThat(testCameraRecord.getSpeed()).isEqualTo(DEFAULT_SPEED);
        assertThat(testCameraRecord.getProcessType()).isEqualTo(DEFAULT_PROCESS_TYPE);
    }

    @Test
    @Transactional
    public void createCameraRecordWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cameraRecordRepository.findAll().size();

        // Create the CameraRecord with an existing ID
        cameraRecord.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCameraRecordMockMvc.perform(post("/api/camera-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cameraRecord)))
            .andExpect(status().isBadRequest());

        // Validate the CameraRecord in the database
        List<CameraRecord> cameraRecordList = cameraRecordRepository.findAll();
        assertThat(cameraRecordList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkInsertDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = cameraRecordRepository.findAll().size();
        // set the field null
        cameraRecord.setInsertDate(null);

        // Create the CameraRecord, which fails.

        restCameraRecordMockMvc.perform(post("/api/camera-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cameraRecord)))
            .andExpect(status().isBadRequest());

        List<CameraRecord> cameraRecordList = cameraRecordRepository.findAll();
        assertThat(cameraRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkVehicleTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = cameraRecordRepository.findAll().size();
        // set the field null
        cameraRecord.setVehicleType(null);

        // Create the CameraRecord, which fails.

        restCameraRecordMockMvc.perform(post("/api/camera-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cameraRecord)))
            .andExpect(status().isBadRequest());

        List<CameraRecord> cameraRecordList = cameraRecordRepository.findAll();
        assertThat(cameraRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDurationIsRequired() throws Exception {
        int databaseSizeBeforeTest = cameraRecordRepository.findAll().size();
        // set the field null
        cameraRecord.setDuration(null);

        // Create the CameraRecord, which fails.

        restCameraRecordMockMvc.perform(post("/api/camera-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cameraRecord)))
            .andExpect(status().isBadRequest());

        List<CameraRecord> cameraRecordList = cameraRecordRepository.findAll();
        assertThat(cameraRecordList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCameraRecords() throws Exception {
        // Initialize the database
        cameraRecordRepository.saveAndFlush(cameraRecord);

        // Get all the cameraRecordList
        restCameraRecordMockMvc.perform(get("/api/camera-records?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cameraRecord.getId().intValue())))
            .andExpect(jsonPath("$.[*].insertDate").value(hasItem(DEFAULT_INSERT_DATE.toString())))
            .andExpect(jsonPath("$.[*].vehicleType").value(hasItem(DEFAULT_VEHICLE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION.intValue())))
            .andExpect(jsonPath("$.[*].speed").value(hasItem(DEFAULT_SPEED.doubleValue())))
            .andExpect(jsonPath("$.[*].processType").value(hasItem(DEFAULT_PROCESS_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getCameraRecord() throws Exception {
        // Initialize the database
        cameraRecordRepository.saveAndFlush(cameraRecord);

        // Get the cameraRecord
        restCameraRecordMockMvc.perform(get("/api/camera-records/{id}", cameraRecord.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cameraRecord.getId().intValue()))
            .andExpect(jsonPath("$.insertDate").value(DEFAULT_INSERT_DATE.toString()))
            .andExpect(jsonPath("$.vehicleType").value(DEFAULT_VEHICLE_TYPE.toString()))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION.intValue()))
            .andExpect(jsonPath("$.speed").value(DEFAULT_SPEED.doubleValue()))
            .andExpect(jsonPath("$.processType").value(DEFAULT_PROCESS_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCameraRecord() throws Exception {
        // Get the cameraRecord
        restCameraRecordMockMvc.perform(get("/api/camera-records/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCameraRecord() throws Exception {
        // Initialize the database
        cameraRecordRepository.saveAndFlush(cameraRecord);
        int databaseSizeBeforeUpdate = cameraRecordRepository.findAll().size();

        // Update the cameraRecord
        CameraRecord updatedCameraRecord = cameraRecordRepository.findOne(cameraRecord.getId());
        // Disconnect from session so that the updates on updatedCameraRecord are not directly saved in db
        em.detach(updatedCameraRecord);
        updatedCameraRecord
            .insertDate(UPDATED_INSERT_DATE)
            .vehicleType(UPDATED_VEHICLE_TYPE)
            .duration(UPDATED_DURATION)
            .speed(UPDATED_SPEED)
            .processType(UPDATED_PROCESS_TYPE);

        restCameraRecordMockMvc.perform(put("/api/camera-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCameraRecord)))
            .andExpect(status().isOk());

        // Validate the CameraRecord in the database
        List<CameraRecord> cameraRecordList = cameraRecordRepository.findAll();
        assertThat(cameraRecordList).hasSize(databaseSizeBeforeUpdate);
        CameraRecord testCameraRecord = cameraRecordList.get(cameraRecordList.size() - 1);
        assertThat(testCameraRecord.getInsertDate()).isEqualTo(UPDATED_INSERT_DATE);
        assertThat(testCameraRecord.getVehicleType()).isEqualTo(UPDATED_VEHICLE_TYPE);
        assertThat(testCameraRecord.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testCameraRecord.getSpeed()).isEqualTo(UPDATED_SPEED);
        assertThat(testCameraRecord.getProcessType()).isEqualTo(UPDATED_PROCESS_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingCameraRecord() throws Exception {
        int databaseSizeBeforeUpdate = cameraRecordRepository.findAll().size();

        // Create the CameraRecord

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCameraRecordMockMvc.perform(put("/api/camera-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cameraRecord)))
            .andExpect(status().isCreated());

        // Validate the CameraRecord in the database
        List<CameraRecord> cameraRecordList = cameraRecordRepository.findAll();
        assertThat(cameraRecordList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCameraRecord() throws Exception {
        // Initialize the database
        cameraRecordRepository.saveAndFlush(cameraRecord);
        int databaseSizeBeforeDelete = cameraRecordRepository.findAll().size();

        // Get the cameraRecord
        restCameraRecordMockMvc.perform(delete("/api/camera-records/{id}", cameraRecord.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CameraRecord> cameraRecordList = cameraRecordRepository.findAll();
        assertThat(cameraRecordList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CameraRecord.class);
        CameraRecord cameraRecord1 = new CameraRecord();
        cameraRecord1.setId(1L);
        CameraRecord cameraRecord2 = new CameraRecord();
        cameraRecord2.setId(cameraRecord1.getId());
        assertThat(cameraRecord1).isEqualTo(cameraRecord2);
        cameraRecord2.setId(2L);
        assertThat(cameraRecord1).isNotEqualTo(cameraRecord2);
        cameraRecord1.setId(null);
        assertThat(cameraRecord1).isNotEqualTo(cameraRecord2);
    }
}
