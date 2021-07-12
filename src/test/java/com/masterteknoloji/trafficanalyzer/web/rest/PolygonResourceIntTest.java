package com.masterteknoloji.trafficanalyzer.web.rest;

import com.masterteknoloji.trafficanalyzer.Trafficanalzyzerv2App;

import com.masterteknoloji.trafficanalyzer.domain.Polygon;
import com.masterteknoloji.trafficanalyzer.repository.PolygonRepository;
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

import com.masterteknoloji.trafficanalyzer.domain.enumeration.PolygonType;
/**
 * Test class for the PolygonResource REST controller.
 *
 * @see PolygonResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Trafficanalzyzerv2App.class)
public class PolygonResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_POINTS = "AAAAAAAAAA";
    private static final String UPDATED_POINTS = "BBBBBBBBBB";

    private static final PolygonType DEFAULT_TYPE = PolygonType.COUNTING;
    private static final PolygonType UPDATED_TYPE = PolygonType.SPEED;

    @Autowired
    private PolygonRepository polygonRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPolygonMockMvc;

    private Polygon polygon;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PolygonResource polygonResource = new PolygonResource(polygonRepository);
        this.restPolygonMockMvc = MockMvcBuilders.standaloneSetup(polygonResource)
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
    public static Polygon createEntity(EntityManager em) {
        Polygon polygon = new Polygon()
            .name(DEFAULT_NAME)
            .points(DEFAULT_POINTS)
            .type(DEFAULT_TYPE);
        return polygon;
    }

    @Before
    public void initTest() {
        polygon = createEntity(em);
    }

    @Test
    @Transactional
    public void createPolygon() throws Exception {
        int databaseSizeBeforeCreate = polygonRepository.findAll().size();

        // Create the Polygon
        restPolygonMockMvc.perform(post("/api/polygons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(polygon)))
            .andExpect(status().isCreated());

        // Validate the Polygon in the database
        List<Polygon> polygonList = polygonRepository.findAll();
        assertThat(polygonList).hasSize(databaseSizeBeforeCreate + 1);
        Polygon testPolygon = polygonList.get(polygonList.size() - 1);
        assertThat(testPolygon.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPolygon.getPoints()).isEqualTo(DEFAULT_POINTS);
        assertThat(testPolygon.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    public void createPolygonWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = polygonRepository.findAll().size();

        // Create the Polygon with an existing ID
        polygon.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPolygonMockMvc.perform(post("/api/polygons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(polygon)))
            .andExpect(status().isBadRequest());

        // Validate the Polygon in the database
        List<Polygon> polygonList = polygonRepository.findAll();
        assertThat(polygonList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPolygons() throws Exception {
        // Initialize the database
        polygonRepository.saveAndFlush(polygon);

        // Get all the polygonList
        restPolygonMockMvc.perform(get("/api/polygons?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(polygon.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getPolygon() throws Exception {
        // Initialize the database
        polygonRepository.saveAndFlush(polygon);

        // Get the polygon
        restPolygonMockMvc.perform(get("/api/polygons/{id}", polygon.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(polygon.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.points").value(DEFAULT_POINTS.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPolygon() throws Exception {
        // Get the polygon
        restPolygonMockMvc.perform(get("/api/polygons/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePolygon() throws Exception {
        // Initialize the database
        polygonRepository.saveAndFlush(polygon);
        int databaseSizeBeforeUpdate = polygonRepository.findAll().size();

        // Update the polygon
        Polygon updatedPolygon = polygonRepository.findOne(polygon.getId());
        // Disconnect from session so that the updates on updatedPolygon are not directly saved in db
        em.detach(updatedPolygon);
        updatedPolygon
            .name(UPDATED_NAME)
            .points(UPDATED_POINTS)
            .type(UPDATED_TYPE);

        restPolygonMockMvc.perform(put("/api/polygons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPolygon)))
            .andExpect(status().isOk());

        // Validate the Polygon in the database
        List<Polygon> polygonList = polygonRepository.findAll();
        assertThat(polygonList).hasSize(databaseSizeBeforeUpdate);
        Polygon testPolygon = polygonList.get(polygonList.size() - 1);
        assertThat(testPolygon.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPolygon.getPoints()).isEqualTo(UPDATED_POINTS);
        assertThat(testPolygon.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingPolygon() throws Exception {
        int databaseSizeBeforeUpdate = polygonRepository.findAll().size();

        // Create the Polygon

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPolygonMockMvc.perform(put("/api/polygons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(polygon)))
            .andExpect(status().isCreated());

        // Validate the Polygon in the database
        List<Polygon> polygonList = polygonRepository.findAll();
        assertThat(polygonList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePolygon() throws Exception {
        // Initialize the database
        polygonRepository.saveAndFlush(polygon);
        int databaseSizeBeforeDelete = polygonRepository.findAll().size();

        // Get the polygon
        restPolygonMockMvc.perform(delete("/api/polygons/{id}", polygon.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Polygon> polygonList = polygonRepository.findAll();
        assertThat(polygonList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Polygon.class);
        Polygon polygon1 = new Polygon();
        polygon1.setId(1L);
        Polygon polygon2 = new Polygon();
        polygon2.setId(polygon1.getId());
        assertThat(polygon1).isEqualTo(polygon2);
        polygon2.setId(2L);
        assertThat(polygon1).isNotEqualTo(polygon2);
        polygon1.setId(null);
        assertThat(polygon1).isNotEqualTo(polygon2);
    }
}
