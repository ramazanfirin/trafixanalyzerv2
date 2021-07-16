package com.masterteknoloji.trafficanalyzer.web.rest;

import com.masterteknoloji.trafficanalyzer.Trafficanalzyzerv2App;

import com.masterteknoloji.trafficanalyzer.domain.RawRecord;
import com.masterteknoloji.trafficanalyzer.repository.RawRecordRepository;
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

/**
 * Test class for the RawRecordResource REST controller.
 *
 * @see RawRecordResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Trafficanalzyzerv2App.class)
public class RawRecordResourceIntTest {

    private static final String DEFAULT_SESSION_ID = "AAAAAAAAAA";
    private static final String UPDATED_SESSION_ID = "BBBBBBBBBB";

    private static final Instant DEFAULT_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_OBJECT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_OBJECT_TYPE = "BBBBBBBBBB";

    private static final Double DEFAULT_SPEED = 1D;
    private static final Double UPDATED_SPEED = 2D;

    private static final String DEFAULT_ENTRY = "AAAAAAAAAA";
    private static final String UPDATED_ENTRY = "BBBBBBBBBB";

    private static final String DEFAULT_EXIT = "AAAAAAAAAA";
    private static final String UPDATED_EXIT = "BBBBBBBBBB";

    @Autowired
    private RawRecordRepository rawRecordRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRawRecordMockMvc;

    private RawRecord rawRecord;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RawRecordResource rawRecordResource = new RawRecordResource(rawRecordRepository);
        this.restRawRecordMockMvc = MockMvcBuilders.standaloneSetup(rawRecordResource)
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
    public static RawRecord createEntity(EntityManager em) {
        RawRecord rawRecord = new RawRecord()
            .sessionID(DEFAULT_SESSION_ID)
            .time(DEFAULT_TIME)
            .objectType(DEFAULT_OBJECT_TYPE)
            .speed(DEFAULT_SPEED)
            .entry(DEFAULT_ENTRY)
            .exit(DEFAULT_EXIT);
        return rawRecord;
    }

    @Before
    public void initTest() {
        rawRecord = createEntity(em);
    }

    @Test
    @Transactional
    public void createRawRecord() throws Exception {
        int databaseSizeBeforeCreate = rawRecordRepository.findAll().size();

        // Create the RawRecord
        restRawRecordMockMvc.perform(post("/api/raw-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rawRecord)))
            .andExpect(status().isCreated());

        // Validate the RawRecord in the database
        List<RawRecord> rawRecordList = rawRecordRepository.findAll();
        assertThat(rawRecordList).hasSize(databaseSizeBeforeCreate + 1);
        RawRecord testRawRecord = rawRecordList.get(rawRecordList.size() - 1);
        assertThat(testRawRecord.getSessionID()).isEqualTo(DEFAULT_SESSION_ID);
        assertThat(testRawRecord.getTime()).isEqualTo(DEFAULT_TIME);
        assertThat(testRawRecord.getObjectType()).isEqualTo(DEFAULT_OBJECT_TYPE);
        assertThat(testRawRecord.getSpeed()).isEqualTo(DEFAULT_SPEED);
        assertThat(testRawRecord.getEntry()).isEqualTo(DEFAULT_ENTRY);
        assertThat(testRawRecord.getExit()).isEqualTo(DEFAULT_EXIT);
    }

    @Test
    @Transactional
    public void createRawRecordWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = rawRecordRepository.findAll().size();

        // Create the RawRecord with an existing ID
        rawRecord.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRawRecordMockMvc.perform(post("/api/raw-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rawRecord)))
            .andExpect(status().isBadRequest());

        // Validate the RawRecord in the database
        List<RawRecord> rawRecordList = rawRecordRepository.findAll();
        assertThat(rawRecordList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllRawRecords() throws Exception {
        // Initialize the database
        rawRecordRepository.saveAndFlush(rawRecord);

        // Get all the rawRecordList
        restRawRecordMockMvc.perform(get("/api/raw-records?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rawRecord.getId().intValue())))
            .andExpect(jsonPath("$.[*].sessionID").value(hasItem(DEFAULT_SESSION_ID.toString())))
            .andExpect(jsonPath("$.[*].time").value(hasItem(DEFAULT_TIME.toString())))
            .andExpect(jsonPath("$.[*].objectType").value(hasItem(DEFAULT_OBJECT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].speed").value(hasItem(DEFAULT_SPEED.doubleValue())))
            .andExpect(jsonPath("$.[*].entry").value(hasItem(DEFAULT_ENTRY.toString())))
            .andExpect(jsonPath("$.[*].exit").value(hasItem(DEFAULT_EXIT.toString())));
    }

    @Test
    @Transactional
    public void getRawRecord() throws Exception {
        // Initialize the database
        rawRecordRepository.saveAndFlush(rawRecord);

        // Get the rawRecord
        restRawRecordMockMvc.perform(get("/api/raw-records/{id}", rawRecord.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(rawRecord.getId().intValue()))
            .andExpect(jsonPath("$.sessionID").value(DEFAULT_SESSION_ID.toString()))
            .andExpect(jsonPath("$.time").value(DEFAULT_TIME.toString()))
            .andExpect(jsonPath("$.objectType").value(DEFAULT_OBJECT_TYPE.toString()))
            .andExpect(jsonPath("$.speed").value(DEFAULT_SPEED.doubleValue()))
            .andExpect(jsonPath("$.entry").value(DEFAULT_ENTRY.toString()))
            .andExpect(jsonPath("$.exit").value(DEFAULT_EXIT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRawRecord() throws Exception {
        // Get the rawRecord
        restRawRecordMockMvc.perform(get("/api/raw-records/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRawRecord() throws Exception {
        // Initialize the database
        rawRecordRepository.saveAndFlush(rawRecord);
        int databaseSizeBeforeUpdate = rawRecordRepository.findAll().size();

        // Update the rawRecord
        RawRecord updatedRawRecord = rawRecordRepository.findOne(rawRecord.getId());
        // Disconnect from session so that the updates on updatedRawRecord are not directly saved in db
        em.detach(updatedRawRecord);
        updatedRawRecord
            .sessionID(UPDATED_SESSION_ID)
            .time(UPDATED_TIME)
            .objectType(UPDATED_OBJECT_TYPE)
            .speed(UPDATED_SPEED)
            .entry(UPDATED_ENTRY)
            .exit(UPDATED_EXIT);

        restRawRecordMockMvc.perform(put("/api/raw-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRawRecord)))
            .andExpect(status().isOk());

        // Validate the RawRecord in the database
        List<RawRecord> rawRecordList = rawRecordRepository.findAll();
        assertThat(rawRecordList).hasSize(databaseSizeBeforeUpdate);
        RawRecord testRawRecord = rawRecordList.get(rawRecordList.size() - 1);
        assertThat(testRawRecord.getSessionID()).isEqualTo(UPDATED_SESSION_ID);
        assertThat(testRawRecord.getTime()).isEqualTo(UPDATED_TIME);
        assertThat(testRawRecord.getObjectType()).isEqualTo(UPDATED_OBJECT_TYPE);
        assertThat(testRawRecord.getSpeed()).isEqualTo(UPDATED_SPEED);
        assertThat(testRawRecord.getEntry()).isEqualTo(UPDATED_ENTRY);
        assertThat(testRawRecord.getExit()).isEqualTo(UPDATED_EXIT);
    }

    @Test
    @Transactional
    public void updateNonExistingRawRecord() throws Exception {
        int databaseSizeBeforeUpdate = rawRecordRepository.findAll().size();

        // Create the RawRecord

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restRawRecordMockMvc.perform(put("/api/raw-records")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rawRecord)))
            .andExpect(status().isCreated());

        // Validate the RawRecord in the database
        List<RawRecord> rawRecordList = rawRecordRepository.findAll();
        assertThat(rawRecordList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteRawRecord() throws Exception {
        // Initialize the database
        rawRecordRepository.saveAndFlush(rawRecord);
        int databaseSizeBeforeDelete = rawRecordRepository.findAll().size();

        // Get the rawRecord
        restRawRecordMockMvc.perform(delete("/api/raw-records/{id}", rawRecord.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<RawRecord> rawRecordList = rawRecordRepository.findAll();
        assertThat(rawRecordList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RawRecord.class);
        RawRecord rawRecord1 = new RawRecord();
        rawRecord1.setId(1L);
        RawRecord rawRecord2 = new RawRecord();
        rawRecord2.setId(rawRecord1.getId());
        assertThat(rawRecord1).isEqualTo(rawRecord2);
        rawRecord2.setId(2L);
        assertThat(rawRecord1).isNotEqualTo(rawRecord2);
        rawRecord1.setId(null);
        assertThat(rawRecord1).isNotEqualTo(rawRecord2);
    }
}
