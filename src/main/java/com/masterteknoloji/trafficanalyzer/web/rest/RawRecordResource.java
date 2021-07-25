package com.masterteknoloji.trafficanalyzer.web.rest;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.masterteknoloji.trafficanalyzer.domain.RawRecord;
import com.masterteknoloji.trafficanalyzer.repository.RawRecordRepository;
import com.masterteknoloji.trafficanalyzer.web.rest.errors.BadRequestAlertException;
import com.masterteknoloji.trafficanalyzer.web.rest.util.HeaderUtil;
import com.masterteknoloji.trafficanalyzer.web.rest.util.PaginationUtil;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing RawRecord.
 */
@RestController
@RequestMapping("/api")
public class RawRecordResource {

	private final Logger log = LoggerFactory.getLogger(RawRecordResource.class);

	private static final String ENTITY_NAME = "rawRecord";

	private final RawRecordRepository rawRecordRepository;

	DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

	public RawRecordResource(RawRecordRepository rawRecordRepository) {
		this.rawRecordRepository = rawRecordRepository;
	}

	/**
	 * POST /raw-records : Create a new rawRecord.
	 *
	 * @param rawRecord the rawRecord to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new
	 *         rawRecord, or with status 400 (Bad Request) if the rawRecord has
	 *         already an ID
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/raw-records")
	@Timed
	public ResponseEntity<RawRecord> createRawRecord(@RequestBody RawRecord rawRecord) throws URISyntaxException {
		log.debug("REST request to save RawRecord : {}", rawRecord);
		if (rawRecord.getId() != null) {
			throw new BadRequestAlertException("A new rawRecord cannot already have an ID", ENTITY_NAME, "idexists");
		}
		RawRecord result = rawRecordRepository.save(rawRecord);
		return ResponseEntity.created(new URI("/api/raw-records/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString())).body(result);
	}

	/**
	 * PUT /raw-records : Updates an existing rawRecord.
	 *
	 * @param rawRecord the rawRecord to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         rawRecord, or with status 400 (Bad Request) if the rawRecord is not
	 *         valid, or with status 500 (Internal Server Error) if the rawRecord
	 *         couldn't be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/raw-records")
	@Timed
	public ResponseEntity<RawRecord> updateRawRecord(@RequestBody RawRecord rawRecord) throws URISyntaxException {
		log.debug("REST request to update RawRecord : {}", rawRecord);
		if (rawRecord.getId() == null) {
			return createRawRecord(rawRecord);
		}
		RawRecord result = rawRecordRepository.save(rawRecord);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, rawRecord.getId().toString())).body(result);
	}

	/**
	 * GET /raw-records : get all the rawRecords.
	 *
	 * @param pageable the pagination information
	 * @return the ResponseEntity with status 200 (OK) and the list of rawRecords in
	 *         body
	 */
	@GetMapping("/raw-records")
	@Timed
	public ResponseEntity<List<RawRecord>> getAllRawRecords(Pageable pageable) {
		log.debug("REST request to get a page of RawRecords");
		Page<RawRecord> page = rawRecordRepository.findAll(pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/raw-records");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /raw-records/:id : get the "id" rawRecord.
	 *
	 * @param id the id of the rawRecord to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the rawRecord,
	 *         or with status 404 (Not Found)
	 */
	@GetMapping("/raw-records/{id}")
	@Timed
	public ResponseEntity<RawRecord> getRawRecord(@PathVariable Long id) {
		log.debug("REST request to get RawRecord : {}", id);
		RawRecord rawRecord = rawRecordRepository.findOne(id);
		return ResponseUtil.wrapOrNotFound(Optional.ofNullable(rawRecord));
	}

	/**
	 * DELETE /raw-records/:id : delete the "id" rawRecord.
	 *
	 * @param id the id of the rawRecord to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/raw-records/{id}")
	@Timed
	public ResponseEntity<Void> deleteRawRecord(@PathVariable Long id) {
		log.debug("REST request to delete RawRecord : {}", id);
		rawRecordRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
	}

	@GetMapping("/raw-records/parseData")
	@Timed
	public ResponseEntity<Void> parseData() throws FileNotFoundException, IOException, CsvException, ParseException {

//    	Video video = videoRepository.findOne(2l);
//		VideoLine gultepeKartal = videoLineRepository.findOne(15l);
//		VideoLine kartalGultepe = videoLineRepository.findOne(16l);

		List<RawRecord> tempList = new ArrayList<RawRecord>();

		int i = 0;
		try (CSVReader reader = new CSVReader(new FileReader("D:\\KBB\\gultepe\\gece.csv"))) {
			List<String[]> r = reader.readAll();
			for (String[] strings : r) {

				if (strings[2].equals("person"))
					continue;

				RawRecord videoRecord = new RawRecord();
				videoRecord.setTime(prepareDateValue(strings[0]));
				//videoRecord.setDuration(prepareDuration(strings[0]));
				videoRecord.setObjectType(strings[1]);

				String entry="";
				String exit="";
				
				String lineId = strings[2];
				if (lineId.equals("0")) {
					entry = "1";
					exit = "2";
				}else if (lineId.equals("1")) {
					entry = "3";
					exit = "4";
			
				}else
					throw new RuntimeException("line bulunamadı");

				videoRecord.setEntry(entry);
				videoRecord.setExit(exit);
				
				videoRecord.setSpeed(prepareSpeed(strings[3]));
				videoRecord.setSessionID("2");
				videoRecord.setMoved(false);
				rawRecordRepository.save(videoRecord);
				i++;
				System.out.println(i + " bitti");
				tempList.add(videoRecord);

				// break;
			}
			System.out.println("bitti");
		}

		return null;

	}

	private Instant prepareDateValue(String dateValue) throws ParseException {

		if (dateValue.length() == 14)
			dateValue = dateValue.substring(0, 11);
		else // if(dateValue.length()==7)
		{
			dateValue = dateValue + ".00";
		}

		Date date = sdf.parse("2000-01-01 0" + dateValue);
		return date.toInstant();

//    	return Instant.now();    	
	}

	private Double prepareSpeed(String value) {

		if (StringUtils.isEmpty(value) || value.equals("Unknown"))
			return 0d;

		Double result = 0d;
		try {
			result = Double.parseDouble(value);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;

	}

	private Long prepareDuration(String dateValue) throws ParseException {

		if (dateValue.length() == 14)
			dateValue = dateValue.substring(0, 11);
		else if (dateValue.length() == 7) {
			dateValue = dateValue + ".000";
		}

		Date date = sdf.parse("1970-01-01 0" + dateValue);
		return date.getTime();

	}
}
