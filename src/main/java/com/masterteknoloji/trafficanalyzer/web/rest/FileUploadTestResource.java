package com.masterteknoloji.trafficanalyzer.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.masterteknoloji.trafficanalyzer.domain.FileUploadTest;

import com.masterteknoloji.trafficanalyzer.repository.FileUploadTestRepository;
import com.masterteknoloji.trafficanalyzer.web.rest.errors.BadRequestAlertException;
import com.masterteknoloji.trafficanalyzer.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing FileUploadTest.
 */
@RestController
@RequestMapping("/api")
public class FileUploadTestResource {

    private final Logger log = LoggerFactory.getLogger(FileUploadTestResource.class);

    private static final String ENTITY_NAME = "fileUploadTest";

    private final FileUploadTestRepository fileUploadTestRepository;

    public FileUploadTestResource(FileUploadTestRepository fileUploadTestRepository) {
        this.fileUploadTestRepository = fileUploadTestRepository;
    }

    /**
     * POST  /file-upload-tests : Create a new fileUploadTest.
     *
     * @param fileUploadTest the fileUploadTest to create
     * @return the ResponseEntity with status 201 (Created) and with body the new fileUploadTest, or with status 400 (Bad Request) if the fileUploadTest has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/file-upload-tests")
    @Timed
    public ResponseEntity<FileUploadTest> createFileUploadTest(@RequestBody FileUploadTest fileUploadTest) throws URISyntaxException {
        log.debug("REST request to save FileUploadTest : {}", fileUploadTest);
        if (fileUploadTest.getId() != null) {
            throw new BadRequestAlertException("A new fileUploadTest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FileUploadTest result = fileUploadTestRepository.save(fileUploadTest);
        return ResponseEntity.created(new URI("/api/file-upload-tests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /file-upload-tests : Updates an existing fileUploadTest.
     *
     * @param fileUploadTest the fileUploadTest to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated fileUploadTest,
     * or with status 400 (Bad Request) if the fileUploadTest is not valid,
     * or with status 500 (Internal Server Error) if the fileUploadTest couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/file-upload-tests")
    @Timed
    public ResponseEntity<FileUploadTest> updateFileUploadTest(@RequestBody FileUploadTest fileUploadTest) throws URISyntaxException {
        log.debug("REST request to update FileUploadTest : {}", fileUploadTest);
        if (fileUploadTest.getId() == null) {
            return createFileUploadTest(fileUploadTest);
        }
        FileUploadTest result = fileUploadTestRepository.save(fileUploadTest);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, fileUploadTest.getId().toString()))
            .body(result);
    }

    /**
     * GET  /file-upload-tests : get all the fileUploadTests.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of fileUploadTests in body
     */
    @GetMapping("/file-upload-tests")
    @Timed
    public List<FileUploadTest> getAllFileUploadTests() {
        log.debug("REST request to get all FileUploadTests");
        return fileUploadTestRepository.findAll();
        }

    /**
     * GET  /file-upload-tests/:id : get the "id" fileUploadTest.
     *
     * @param id the id of the fileUploadTest to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the fileUploadTest, or with status 404 (Not Found)
     */
    @GetMapping("/file-upload-tests/{id}")
    @Timed
    public ResponseEntity<FileUploadTest> getFileUploadTest(@PathVariable Long id) {
        log.debug("REST request to get FileUploadTest : {}", id);
        FileUploadTest fileUploadTest = fileUploadTestRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(fileUploadTest));
    }

    /**
     * DELETE  /file-upload-tests/:id : delete the "id" fileUploadTest.
     *
     * @param id the id of the fileUploadTest to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/file-upload-tests/{id}")
    @Timed
    public ResponseEntity<Void> deleteFileUploadTest(@PathVariable Long id) {
        log.debug("REST request to delete FileUploadTest : {}", id);
        fileUploadTestRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
