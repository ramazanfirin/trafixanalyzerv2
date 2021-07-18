package com.masterteknoloji.trafficanalyzer.web.rest;

import com.masterteknoloji.trafficanalyzer.Trafficanalzyzerv2App;

import com.masterteknoloji.trafficanalyzer.domain.AnalyzeOrder;
import com.masterteknoloji.trafficanalyzer.repository.AnalyzeOrderRepository;
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
import java.util.List;

import static com.masterteknoloji.trafficanalyzer.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.masterteknoloji.trafficanalyzer.domain.enumeration.AnalyzeState;
/**
 * Test class for the AnalyzeOrderResource REST controller.
 *
 * @see AnalyzeOrderResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Trafficanalzyzerv2App.class)
public class AnalyzeOrderResourceIntTest {

    private static final AnalyzeState DEFAULT_STATE = AnalyzeState.NOT_PROCESSED;
    private static final AnalyzeState UPDATED_STATE = AnalyzeState.STARTED;

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

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AnalyzeOrderResource analyzeOrderResource = new AnalyzeOrderResource(analyzeOrderRepository);
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
            .state(DEFAULT_STATE);
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
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())));
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
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()));
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
            .state(UPDATED_STATE);

        restAnalyzeOrderMockMvc.perform(put("/api/analyze-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAnalyzeOrder)))
            .andExpect(status().isOk());

        // Validate the AnalyzeOrder in the database
        List<AnalyzeOrder> analyzeOrderList = analyzeOrderRepository.findAll();
        assertThat(analyzeOrderList).hasSize(databaseSizeBeforeUpdate);
        AnalyzeOrder testAnalyzeOrder = analyzeOrderList.get(analyzeOrderList.size() - 1);
        assertThat(testAnalyzeOrder.getState()).isEqualTo(UPDATED_STATE);
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
}
