package com.masterteknoloji.trafficanalyzer.web.rest;

import com.masterteknoloji.trafficanalyzer.Trafficanalzyzerv2App;

import com.masterteknoloji.trafficanalyzer.domain.FileUploadTest;
import com.masterteknoloji.trafficanalyzer.repository.FileUploadTestRepository;
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
 * Test class for the FileUploadTestResource REST controller.
 *
 * @see FileUploadTestResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Trafficanalzyzerv2App.class)
public class FileUploadTestResourceIntTest {

    private static final byte[] DEFAULT_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FILE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FILE_CONTENT_TYPE = "image/png";

    @Autowired
    private FileUploadTestRepository fileUploadTestRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restFileUploadTestMockMvc;

    private FileUploadTest fileUploadTest;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FileUploadTestResource fileUploadTestResource = new FileUploadTestResource(fileUploadTestRepository);
        this.restFileUploadTestMockMvc = MockMvcBuilders.standaloneSetup(fileUploadTestResource)
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
    public static FileUploadTest createEntity(EntityManager em) {
        FileUploadTest fileUploadTest = new FileUploadTest()
            .file(DEFAULT_FILE)
            .fileContentType(DEFAULT_FILE_CONTENT_TYPE);
        return fileUploadTest;
    }

    @Before
    public void initTest() {
        fileUploadTest = createEntity(em);
    }

    @Test
    @Transactional
    public void createFileUploadTest() throws Exception {
        int databaseSizeBeforeCreate = fileUploadTestRepository.findAll().size();

        // Create the FileUploadTest
        restFileUploadTestMockMvc.perform(post("/api/file-upload-tests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fileUploadTest)))
            .andExpect(status().isCreated());

        // Validate the FileUploadTest in the database
        List<FileUploadTest> fileUploadTestList = fileUploadTestRepository.findAll();
        assertThat(fileUploadTestList).hasSize(databaseSizeBeforeCreate + 1);
        FileUploadTest testFileUploadTest = fileUploadTestList.get(fileUploadTestList.size() - 1);
        assertThat(testFileUploadTest.getFile()).isEqualTo(DEFAULT_FILE);
        assertThat(testFileUploadTest.getFileContentType()).isEqualTo(DEFAULT_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createFileUploadTestWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = fileUploadTestRepository.findAll().size();

        // Create the FileUploadTest with an existing ID
        fileUploadTest.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFileUploadTestMockMvc.perform(post("/api/file-upload-tests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fileUploadTest)))
            .andExpect(status().isBadRequest());

        // Validate the FileUploadTest in the database
        List<FileUploadTest> fileUploadTestList = fileUploadTestRepository.findAll();
        assertThat(fileUploadTestList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllFileUploadTests() throws Exception {
        // Initialize the database
        fileUploadTestRepository.saveAndFlush(fileUploadTest);

        // Get all the fileUploadTestList
        restFileUploadTestMockMvc.perform(get("/api/file-upload-tests?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fileUploadTest.getId().intValue())))
            .andExpect(jsonPath("$.[*].fileContentType").value(hasItem(DEFAULT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].file").value(hasItem(Base64Utils.encodeToString(DEFAULT_FILE))));
    }

    @Test
    @Transactional
    public void getFileUploadTest() throws Exception {
        // Initialize the database
        fileUploadTestRepository.saveAndFlush(fileUploadTest);

        // Get the fileUploadTest
        restFileUploadTestMockMvc.perform(get("/api/file-upload-tests/{id}", fileUploadTest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(fileUploadTest.getId().intValue()))
            .andExpect(jsonPath("$.fileContentType").value(DEFAULT_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.file").value(Base64Utils.encodeToString(DEFAULT_FILE)));
    }

    @Test
    @Transactional
    public void getNonExistingFileUploadTest() throws Exception {
        // Get the fileUploadTest
        restFileUploadTestMockMvc.perform(get("/api/file-upload-tests/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFileUploadTest() throws Exception {
        // Initialize the database
        fileUploadTestRepository.saveAndFlush(fileUploadTest);
        int databaseSizeBeforeUpdate = fileUploadTestRepository.findAll().size();

        // Update the fileUploadTest
        FileUploadTest updatedFileUploadTest = fileUploadTestRepository.findOne(fileUploadTest.getId());
        // Disconnect from session so that the updates on updatedFileUploadTest are not directly saved in db
        em.detach(updatedFileUploadTest);
        updatedFileUploadTest
            .file(UPDATED_FILE)
            .fileContentType(UPDATED_FILE_CONTENT_TYPE);

        restFileUploadTestMockMvc.perform(put("/api/file-upload-tests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFileUploadTest)))
            .andExpect(status().isOk());

        // Validate the FileUploadTest in the database
        List<FileUploadTest> fileUploadTestList = fileUploadTestRepository.findAll();
        assertThat(fileUploadTestList).hasSize(databaseSizeBeforeUpdate);
        FileUploadTest testFileUploadTest = fileUploadTestList.get(fileUploadTestList.size() - 1);
        assertThat(testFileUploadTest.getFile()).isEqualTo(UPDATED_FILE);
        assertThat(testFileUploadTest.getFileContentType()).isEqualTo(UPDATED_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingFileUploadTest() throws Exception {
        int databaseSizeBeforeUpdate = fileUploadTestRepository.findAll().size();

        // Create the FileUploadTest

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restFileUploadTestMockMvc.perform(put("/api/file-upload-tests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(fileUploadTest)))
            .andExpect(status().isCreated());

        // Validate the FileUploadTest in the database
        List<FileUploadTest> fileUploadTestList = fileUploadTestRepository.findAll();
        assertThat(fileUploadTestList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteFileUploadTest() throws Exception {
        // Initialize the database
        fileUploadTestRepository.saveAndFlush(fileUploadTest);
        int databaseSizeBeforeDelete = fileUploadTestRepository.findAll().size();

        // Get the fileUploadTest
        restFileUploadTestMockMvc.perform(delete("/api/file-upload-tests/{id}", fileUploadTest.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<FileUploadTest> fileUploadTestList = fileUploadTestRepository.findAll();
        assertThat(fileUploadTestList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FileUploadTest.class);
        FileUploadTest fileUploadTest1 = new FileUploadTest();
        fileUploadTest1.setId(1L);
        FileUploadTest fileUploadTest2 = new FileUploadTest();
        fileUploadTest2.setId(fileUploadTest1.getId());
        assertThat(fileUploadTest1).isEqualTo(fileUploadTest2);
        fileUploadTest2.setId(2L);
        assertThat(fileUploadTest1).isNotEqualTo(fileUploadTest2);
        fileUploadTest1.setId(null);
        assertThat(fileUploadTest1).isNotEqualTo(fileUploadTest2);
    }
}
