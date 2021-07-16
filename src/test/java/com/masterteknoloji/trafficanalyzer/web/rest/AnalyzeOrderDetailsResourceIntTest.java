package com.masterteknoloji.trafficanalyzer.web.rest;

import com.masterteknoloji.trafficanalyzer.Trafficanalzyzerv2App;

import com.masterteknoloji.trafficanalyzer.domain.AnalyzeOrderDetails;
import com.masterteknoloji.trafficanalyzer.repository.AnalyzeOrderDetailsRepository;
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
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.util.List;

import static com.masterteknoloji.trafficanalyzer.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AnalyzeOrderDetailsResource REST controller.
 *
 * @see AnalyzeOrderDetailsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Trafficanalzyzerv2App.class)
public class AnalyzeOrderDetailsResourceIntTest {

    private static final String DEFAULT_SESSION_ID = "AAAAAAAAAA";
    private static final String UPDATED_SESSION_ID = "BBBBBBBBBB";

    private static final String DEFAULT_VIDEO_PATH = "AAAAAAAAAA";
    private static final String UPDATED_VIDEO_PATH = "BBBBBBBBBB";

    private static final Boolean DEFAULT_COUNT = false;
    private static final Boolean UPDATED_COUNT = true;

    private static final String DEFAULT_CLASSES = "AAAAAAAAAA";
    private static final String UPDATED_CLASSES = "BBBBBBBBBB";

    private static final String DEFAULT_DIRECTIONS = "AAAAAAAAAA";
    private static final String UPDATED_DIRECTIONS = "BBBBBBBBBB";

    private static final String DEFAULT_SPEED = "AAAAAAAAAA";
    private static final String UPDATED_SPEED = "BBBBBBBBBB";

    @Autowired
    private AnalyzeOrderDetailsRepository analyzeOrderDetailsRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAnalyzeOrderDetailsMockMvc;

    private AnalyzeOrderDetails analyzeOrderDetails;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AnalyzeOrderDetailsResource analyzeOrderDetailsResource = new AnalyzeOrderDetailsResource(analyzeOrderDetailsRepository);
        this.restAnalyzeOrderDetailsMockMvc = MockMvcBuilders.standaloneSetup(analyzeOrderDetailsResource)
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
    public static AnalyzeOrderDetails createEntity(EntityManager em) {
        AnalyzeOrderDetails analyzeOrderDetails = new AnalyzeOrderDetails()
            .sessionId(DEFAULT_SESSION_ID)
            .videoPath(DEFAULT_VIDEO_PATH)
            .count(DEFAULT_COUNT)
            .classes(DEFAULT_CLASSES)
            .directions(DEFAULT_DIRECTIONS)
            .speed(DEFAULT_SPEED);
        return analyzeOrderDetails;
    }

    @Before
    public void initTest() {
        analyzeOrderDetails = createEntity(em);
    }

    @Test
    @Transactional
    public void createAnalyzeOrderDetails() throws Exception {
        int databaseSizeBeforeCreate = analyzeOrderDetailsRepository.findAll().size();

        // Create the AnalyzeOrderDetails
        restAnalyzeOrderDetailsMockMvc.perform(post("/api/analyze-order-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(analyzeOrderDetails)))
            .andExpect(status().isCreated());

        // Validate the AnalyzeOrderDetails in the database
        List<AnalyzeOrderDetails> analyzeOrderDetailsList = analyzeOrderDetailsRepository.findAll();
        assertThat(analyzeOrderDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        AnalyzeOrderDetails testAnalyzeOrderDetails = analyzeOrderDetailsList.get(analyzeOrderDetailsList.size() - 1);
        assertThat(testAnalyzeOrderDetails.getSessionId()).isEqualTo(DEFAULT_SESSION_ID);
        assertThat(testAnalyzeOrderDetails.getVideoPath()).isEqualTo(DEFAULT_VIDEO_PATH);
        assertThat(testAnalyzeOrderDetails.isCount()).isEqualTo(DEFAULT_COUNT);
        assertThat(testAnalyzeOrderDetails.getClasses()).isEqualTo(DEFAULT_CLASSES);
        assertThat(testAnalyzeOrderDetails.getDirections()).isEqualTo(DEFAULT_DIRECTIONS);
        assertThat(testAnalyzeOrderDetails.getSpeed()).isEqualTo(DEFAULT_SPEED);
    }

    @Test
    @Transactional
    public void createAnalyzeOrderDetailsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = analyzeOrderDetailsRepository.findAll().size();

        // Create the AnalyzeOrderDetails with an existing ID
        analyzeOrderDetails.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAnalyzeOrderDetailsMockMvc.perform(post("/api/analyze-order-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(analyzeOrderDetails)))
            .andExpect(status().isBadRequest());

        // Validate the AnalyzeOrderDetails in the database
        List<AnalyzeOrderDetails> analyzeOrderDetailsList = analyzeOrderDetailsRepository.findAll();
        assertThat(analyzeOrderDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllAnalyzeOrderDetails() throws Exception {
        // Initialize the database
        analyzeOrderDetailsRepository.saveAndFlush(analyzeOrderDetails);

        // Get all the analyzeOrderDetailsList
        restAnalyzeOrderDetailsMockMvc.perform(get("/api/analyze-order-details?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(analyzeOrderDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].sessionId").value(hasItem(DEFAULT_SESSION_ID.toString())))
            .andExpect(jsonPath("$.[*].videoPath").value(hasItem(DEFAULT_VIDEO_PATH.toString())))
            .andExpect(jsonPath("$.[*].count").value(hasItem(DEFAULT_COUNT.booleanValue())))
            .andExpect(jsonPath("$.[*].classes").value(hasItem(DEFAULT_CLASSES.toString())))
            .andExpect(jsonPath("$.[*].directions").value(hasItem(DEFAULT_DIRECTIONS.toString())))
            .andExpect(jsonPath("$.[*].speed").value(hasItem(DEFAULT_SPEED.toString())));
    }

    @Test
    @Transactional
    public void getAnalyzeOrderDetails() throws Exception {
        // Initialize the database
        analyzeOrderDetailsRepository.saveAndFlush(analyzeOrderDetails);

        // Get the analyzeOrderDetails
        restAnalyzeOrderDetailsMockMvc.perform(get("/api/analyze-order-details/{id}", analyzeOrderDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(analyzeOrderDetails.getId().intValue()))
            .andExpect(jsonPath("$.sessionId").value(DEFAULT_SESSION_ID.toString()))
            .andExpect(jsonPath("$.videoPath").value(DEFAULT_VIDEO_PATH.toString()))
            .andExpect(jsonPath("$.count").value(DEFAULT_COUNT.booleanValue()))
            .andExpect(jsonPath("$.classes").value(DEFAULT_CLASSES.toString()))
            .andExpect(jsonPath("$.directions").value(DEFAULT_DIRECTIONS.toString()))
            .andExpect(jsonPath("$.speed").value(DEFAULT_SPEED.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAnalyzeOrderDetails() throws Exception {
        // Get the analyzeOrderDetails
        restAnalyzeOrderDetailsMockMvc.perform(get("/api/analyze-order-details/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAnalyzeOrderDetails() throws Exception {
        // Initialize the database
        analyzeOrderDetailsRepository.saveAndFlush(analyzeOrderDetails);
        int databaseSizeBeforeUpdate = analyzeOrderDetailsRepository.findAll().size();

        // Update the analyzeOrderDetails
        AnalyzeOrderDetails updatedAnalyzeOrderDetails = analyzeOrderDetailsRepository.findOne(analyzeOrderDetails.getId());
        // Disconnect from session so that the updates on updatedAnalyzeOrderDetails are not directly saved in db
        em.detach(updatedAnalyzeOrderDetails);
        updatedAnalyzeOrderDetails
            .sessionId(UPDATED_SESSION_ID)
            .videoPath(UPDATED_VIDEO_PATH)
            .count(UPDATED_COUNT)
            .classes(UPDATED_CLASSES)
            .directions(UPDATED_DIRECTIONS)
            .speed(UPDATED_SPEED);

        restAnalyzeOrderDetailsMockMvc.perform(put("/api/analyze-order-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAnalyzeOrderDetails)))
            .andExpect(status().isOk());

        // Validate the AnalyzeOrderDetails in the database
        List<AnalyzeOrderDetails> analyzeOrderDetailsList = analyzeOrderDetailsRepository.findAll();
        assertThat(analyzeOrderDetailsList).hasSize(databaseSizeBeforeUpdate);
        AnalyzeOrderDetails testAnalyzeOrderDetails = analyzeOrderDetailsList.get(analyzeOrderDetailsList.size() - 1);
        assertThat(testAnalyzeOrderDetails.getSessionId()).isEqualTo(UPDATED_SESSION_ID);
        assertThat(testAnalyzeOrderDetails.getVideoPath()).isEqualTo(UPDATED_VIDEO_PATH);
        assertThat(testAnalyzeOrderDetails.isCount()).isEqualTo(UPDATED_COUNT);
        assertThat(testAnalyzeOrderDetails.getClasses()).isEqualTo(UPDATED_CLASSES);
        assertThat(testAnalyzeOrderDetails.getDirections()).isEqualTo(UPDATED_DIRECTIONS);
        assertThat(testAnalyzeOrderDetails.getSpeed()).isEqualTo(UPDATED_SPEED);
    }

    @Test
    @Transactional
    public void updateNonExistingAnalyzeOrderDetails() throws Exception {
        int databaseSizeBeforeUpdate = analyzeOrderDetailsRepository.findAll().size();

        // Create the AnalyzeOrderDetails

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAnalyzeOrderDetailsMockMvc.perform(put("/api/analyze-order-details")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(analyzeOrderDetails)))
            .andExpect(status().isCreated());

        // Validate the AnalyzeOrderDetails in the database
        List<AnalyzeOrderDetails> analyzeOrderDetailsList = analyzeOrderDetailsRepository.findAll();
        assertThat(analyzeOrderDetailsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAnalyzeOrderDetails() throws Exception {
        // Initialize the database
        analyzeOrderDetailsRepository.saveAndFlush(analyzeOrderDetails);
        int databaseSizeBeforeDelete = analyzeOrderDetailsRepository.findAll().size();

        // Get the analyzeOrderDetails
        restAnalyzeOrderDetailsMockMvc.perform(delete("/api/analyze-order-details/{id}", analyzeOrderDetails.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<AnalyzeOrderDetails> analyzeOrderDetailsList = analyzeOrderDetailsRepository.findAll();
        assertThat(analyzeOrderDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AnalyzeOrderDetails.class);
        AnalyzeOrderDetails analyzeOrderDetails1 = new AnalyzeOrderDetails();
        analyzeOrderDetails1.setId(1L);
        AnalyzeOrderDetails analyzeOrderDetails2 = new AnalyzeOrderDetails();
        analyzeOrderDetails2.setId(analyzeOrderDetails1.getId());
        assertThat(analyzeOrderDetails1).isEqualTo(analyzeOrderDetails2);
        analyzeOrderDetails2.setId(2L);
        assertThat(analyzeOrderDetails1).isNotEqualTo(analyzeOrderDetails2);
        analyzeOrderDetails1.setId(null);
        assertThat(analyzeOrderDetails1).isNotEqualTo(analyzeOrderDetails2);
    }
}
