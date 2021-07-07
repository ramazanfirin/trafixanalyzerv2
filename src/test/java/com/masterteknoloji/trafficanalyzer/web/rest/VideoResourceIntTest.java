package com.masterteknoloji.trafficanalyzer.web.rest;

import com.masterteknoloji.trafficanalyzer.Trafficanalzyzerv2App;
import com.masterteknoloji.trafficanalyzer.config.ApplicationProperties;
import com.masterteknoloji.trafficanalyzer.domain.Video;
import com.masterteknoloji.trafficanalyzer.repository.VideoRepository;
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

import com.masterteknoloji.trafficanalyzer.domain.enumeration.VideoState;
/**
 * Test class for the VideoResource REST controller.
 *
 * @see VideoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Trafficanalzyzerv2App.class)
public class VideoResourceIntTest {

    private static final String DEFAULT_PATH = "AAAAAAAAAA";
    private static final String UPDATED_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_PROCESS_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PROCESS_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final VideoState DEFAULT_STATE = VideoState.NOT_PROCESSED;
    private static final VideoState UPDATED_STATE = VideoState.STARTED;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private ApplicationProperties applicationProperties;
    
    private MockMvc restVideoMockMvc;

    private Video video;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final VideoResource videoResource = new VideoResource(videoRepository,applicationProperties);
        this.restVideoMockMvc = MockMvcBuilders.standaloneSetup(videoResource)
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
    public static Video createEntity(EntityManager em) {
        Video video = new Video()
            .path(DEFAULT_PATH)
            .name(DEFAULT_NAME)
            .processDate(DEFAULT_PROCESS_DATE)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .state(DEFAULT_STATE);
        return video;
    }

    @Before
    public void initTest() {
        video = createEntity(em);
    }

    @Test
    @Transactional
    public void createVideo() throws Exception {
        int databaseSizeBeforeCreate = videoRepository.findAll().size();

        // Create the Video
        restVideoMockMvc.perform(post("/api/videos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(video)))
            .andExpect(status().isCreated());

        // Validate the Video in the database
        List<Video> videoList = videoRepository.findAll();
        assertThat(videoList).hasSize(databaseSizeBeforeCreate + 1);
        Video testVideo = videoList.get(videoList.size() - 1);
        assertThat(testVideo.getPath()).isEqualTo(DEFAULT_PATH);
        assertThat(testVideo.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testVideo.getProcessDate()).isEqualTo(DEFAULT_PROCESS_DATE);
        assertThat(testVideo.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testVideo.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testVideo.getState()).isEqualTo(DEFAULT_STATE);
    }

    @Test
    @Transactional
    public void createVideoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = videoRepository.findAll().size();

        // Create the Video with an existing ID
        video.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVideoMockMvc.perform(post("/api/videos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(video)))
            .andExpect(status().isBadRequest());

        // Validate the Video in the database
        List<Video> videoList = videoRepository.findAll();
        assertThat(videoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkPathIsRequired() throws Exception {
        int databaseSizeBeforeTest = videoRepository.findAll().size();
        // set the field null
        video.setPath(null);

        // Create the Video, which fails.

        restVideoMockMvc.perform(post("/api/videos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(video)))
            .andExpect(status().isBadRequest());

        List<Video> videoList = videoRepository.findAll();
        assertThat(videoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = videoRepository.findAll().size();
        // set the field null
        video.setName(null);

        // Create the Video, which fails.

        restVideoMockMvc.perform(post("/api/videos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(video)))
            .andExpect(status().isBadRequest());

        List<Video> videoList = videoRepository.findAll();
        assertThat(videoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllVideos() throws Exception {
        // Initialize the database
        videoRepository.saveAndFlush(video);

        // Get all the videoList
        restVideoMockMvc.perform(get("/api/videos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(video.getId().intValue())))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].processDate").value(hasItem(DEFAULT_PROCESS_DATE.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())));
    }

    @Test
    @Transactional
    public void getVideo() throws Exception {
        // Initialize the database
        videoRepository.saveAndFlush(video);

        // Get the video
        restVideoMockMvc.perform(get("/api/videos/{id}", video.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(video.getId().intValue()))
            .andExpect(jsonPath("$.path").value(DEFAULT_PATH.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.processDate").value(DEFAULT_PROCESS_DATE.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingVideo() throws Exception {
        // Get the video
        restVideoMockMvc.perform(get("/api/videos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVideo() throws Exception {
        // Initialize the database
        videoRepository.saveAndFlush(video);
        int databaseSizeBeforeUpdate = videoRepository.findAll().size();

        // Update the video
        Video updatedVideo = videoRepository.findOne(video.getId());
        // Disconnect from session so that the updates on updatedVideo are not directly saved in db
        em.detach(updatedVideo);
        updatedVideo
            .path(UPDATED_PATH)
            .name(UPDATED_NAME)
            .processDate(UPDATED_PROCESS_DATE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .state(UPDATED_STATE);

        restVideoMockMvc.perform(put("/api/videos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedVideo)))
            .andExpect(status().isOk());

        // Validate the Video in the database
        List<Video> videoList = videoRepository.findAll();
        assertThat(videoList).hasSize(databaseSizeBeforeUpdate);
        Video testVideo = videoList.get(videoList.size() - 1);
        assertThat(testVideo.getPath()).isEqualTo(UPDATED_PATH);
        assertThat(testVideo.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testVideo.getProcessDate()).isEqualTo(UPDATED_PROCESS_DATE);
        assertThat(testVideo.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testVideo.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testVideo.getState()).isEqualTo(UPDATED_STATE);
    }

    @Test
    @Transactional
    public void updateNonExistingVideo() throws Exception {
        int databaseSizeBeforeUpdate = videoRepository.findAll().size();

        // Create the Video

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restVideoMockMvc.perform(put("/api/videos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(video)))
            .andExpect(status().isCreated());

        // Validate the Video in the database
        List<Video> videoList = videoRepository.findAll();
        assertThat(videoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteVideo() throws Exception {
        // Initialize the database
        videoRepository.saveAndFlush(video);
        int databaseSizeBeforeDelete = videoRepository.findAll().size();

        // Get the video
        restVideoMockMvc.perform(delete("/api/videos/{id}", video.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Video> videoList = videoRepository.findAll();
        assertThat(videoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Video.class);
        Video video1 = new Video();
        video1.setId(1L);
        Video video2 = new Video();
        video2.setId(video1.getId());
        assertThat(video1).isEqualTo(video2);
        video2.setId(2L);
        assertThat(video1).isNotEqualTo(video2);
        video1.setId(null);
        assertThat(video1).isNotEqualTo(video2);
    }
}
