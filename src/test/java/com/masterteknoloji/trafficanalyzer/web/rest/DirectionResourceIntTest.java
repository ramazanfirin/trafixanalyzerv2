package com.masterteknoloji.trafficanalyzer.web.rest;

import com.masterteknoloji.trafficanalyzer.Trafficanalzyzerv2App;

import com.masterteknoloji.trafficanalyzer.domain.Direction;
import com.masterteknoloji.trafficanalyzer.repository.DirectionRepository;
import com.masterteknoloji.trafficanalyzer.repository.LineRepository;
import com.masterteknoloji.trafficanalyzer.repository.ScenarioRepository;
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

/**
 * Test class for the DirectionResource REST controller.
 *
 * @see DirectionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Trafficanalzyzerv2App.class)
public class DirectionResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PATH_COORDINATES = "AAAAAAAAAA";
    private static final String UPDATED_PATH_COORDINATES = "BBBBBBBBBB";

    @Autowired
    private DirectionRepository directionRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;
    
    @Autowired
    private ScenarioRepository scenarioRepository;
    
    @Autowired
    private LineRepository lineRepository;

    private MockMvc restDirectionMockMvc;

    private Direction direction;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DirectionResource directionResource = new DirectionResource(directionRepository,scenarioRepository,lineRepository);
        this.restDirectionMockMvc = MockMvcBuilders.standaloneSetup(directionResource)
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
    public static Direction createEntity(EntityManager em) {
        Direction direction = new Direction()
            .name(DEFAULT_NAME)
            .pathCoordinates(DEFAULT_PATH_COORDINATES);
        return direction;
    }

    @Before
    public void initTest() {
        direction = createEntity(em);
    }

    @Test
    @Transactional
    public void createDirection() throws Exception {
        int databaseSizeBeforeCreate = directionRepository.findAll().size();

        // Create the Direction
        restDirectionMockMvc.perform(post("/api/directions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(direction)))
            .andExpect(status().isCreated());

        // Validate the Direction in the database
        List<Direction> directionList = directionRepository.findAll();
        assertThat(directionList).hasSize(databaseSizeBeforeCreate + 1);
        Direction testDirection = directionList.get(directionList.size() - 1);
        assertThat(testDirection.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDirection.getPathCoordinates()).isEqualTo(DEFAULT_PATH_COORDINATES);
    }

    @Test
    @Transactional
    public void createDirectionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = directionRepository.findAll().size();

        // Create the Direction with an existing ID
        direction.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDirectionMockMvc.perform(post("/api/directions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(direction)))
            .andExpect(status().isBadRequest());

        // Validate the Direction in the database
        List<Direction> directionList = directionRepository.findAll();
        assertThat(directionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = directionRepository.findAll().size();
        // set the field null
        direction.setName(null);

        // Create the Direction, which fails.

        restDirectionMockMvc.perform(post("/api/directions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(direction)))
            .andExpect(status().isBadRequest());

        List<Direction> directionList = directionRepository.findAll();
        assertThat(directionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDirections() throws Exception {
        // Initialize the database
        directionRepository.saveAndFlush(direction);

        // Get all the directionList
        restDirectionMockMvc.perform(get("/api/directions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(direction.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].pathCoordinates").value(hasItem(DEFAULT_PATH_COORDINATES.toString())));
    }

    @Test
    @Transactional
    public void getDirection() throws Exception {
        // Initialize the database
        directionRepository.saveAndFlush(direction);

        // Get the direction
        restDirectionMockMvc.perform(get("/api/directions/{id}", direction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(direction.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.pathCoordinates").value(DEFAULT_PATH_COORDINATES.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDirection() throws Exception {
        // Get the direction
        restDirectionMockMvc.perform(get("/api/directions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDirection() throws Exception {
        // Initialize the database
        directionRepository.saveAndFlush(direction);
        int databaseSizeBeforeUpdate = directionRepository.findAll().size();

        // Update the direction
        Direction updatedDirection = directionRepository.findOne(direction.getId());
        // Disconnect from session so that the updates on updatedDirection are not directly saved in db
        em.detach(updatedDirection);
        updatedDirection
            .name(UPDATED_NAME)
            .pathCoordinates(UPDATED_PATH_COORDINATES);

        restDirectionMockMvc.perform(put("/api/directions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDirection)))
            .andExpect(status().isOk());

        // Validate the Direction in the database
        List<Direction> directionList = directionRepository.findAll();
        assertThat(directionList).hasSize(databaseSizeBeforeUpdate);
        Direction testDirection = directionList.get(directionList.size() - 1);
        assertThat(testDirection.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDirection.getPathCoordinates()).isEqualTo(UPDATED_PATH_COORDINATES);
    }

    @Test
    @Transactional
    public void updateNonExistingDirection() throws Exception {
        int databaseSizeBeforeUpdate = directionRepository.findAll().size();

        // Create the Direction

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDirectionMockMvc.perform(put("/api/directions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(direction)))
            .andExpect(status().isCreated());

        // Validate the Direction in the database
        List<Direction> directionList = directionRepository.findAll();
        assertThat(directionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDirection() throws Exception {
        // Initialize the database
        directionRepository.saveAndFlush(direction);
        int databaseSizeBeforeDelete = directionRepository.findAll().size();

        // Get the direction
        restDirectionMockMvc.perform(delete("/api/directions/{id}", direction.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Direction> directionList = directionRepository.findAll();
        assertThat(directionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Direction.class);
        Direction direction1 = new Direction();
        direction1.setId(1L);
        Direction direction2 = new Direction();
        direction2.setId(direction1.getId());
        assertThat(direction1).isEqualTo(direction2);
        direction2.setId(2L);
        assertThat(direction1).isNotEqualTo(direction2);
        direction1.setId(null);
        assertThat(direction1).isNotEqualTo(direction2);
    }
}
