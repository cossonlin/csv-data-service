package com.govtech.csvdata.controller;

import com.govtech.csvdata.dto.PagingBody;
import com.govtech.csvdata.dto.PagingHeader;
import com.govtech.csvdata.entity.Invoice;
import com.govtech.csvdata.service.CsvService;
import com.govtech.csvdata.service.DataService;
import lombok.extern.slf4j.Slf4j;
import net.kaczmarzyk.spring.data.jpa.domain.*;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/csvdata")
public class CsvDataController {

	private final CsvService csvService;
	private final DataService dataService;

	@Autowired
	public CsvDataController(CsvService csvService, DataService dataService) {
		this.csvService = csvService;
		this.dataService = dataService;
	}

	@PostMapping("/file")
	public ResponseEntity<Long> uploadCsvFile(@RequestPart("file") MultipartFile file) {
		long taskId = csvService.saveCsvFile(file);
		return ResponseEntity.ok(taskId);
	}

	@GetMapping("/data/search")
	public ResponseEntity<List<Invoice>> searchData(
			@And({
					@Spec(path = "invoiceNo", params = "invoiceNo", spec = Like.class),
					@Spec(path = "stockCode", params = "stockCode", spec = Like.class),
					@Spec(path = "description", params = "description", spec = Like.class),
					@Spec(path = "quantity", params = {"quantityMin", "quantityMax"}, spec = Between.class),
					@Spec(path = "invoiceDate", params = {"invoiceDateMin", "invoiceDateMax"}, spec = Between.class),
					@Spec(path = "unitPrice", params = {"unitPriceLow", "unitPriceHigh"}, spec = Between.class),
					@Spec(path = "unitPrice", params = {"unitPriceMin"}, spec = GreaterThanOrEqual.class),
					@Spec(path = "unitPrice", params = {"unitPriceMax"}, spec = LessThanOrEqual.class),
					@Spec(path = "customerID", params = "customerID", spec = Equal.class),
					@Spec(path = "country", params = "country", spec = Like.class)
			}) Specification<Invoice> spec,
			Sort sort,
			@RequestHeader HttpHeaders headers) {
		final PagingBody response = dataService.fetchBySpec(spec, headers, sort);
		return new ResponseEntity<>(response.getElements(), createHttpHeaders(response), HttpStatus.OK);
	}

	private HttpHeaders createHttpHeaders(PagingBody response) {
		HttpHeaders headers = new HttpHeaders();
		headers.set(PagingHeader.COUNT.getValue(), String.valueOf(response.getCount()));
		headers.set(PagingHeader.PAGE_SIZE.getValue(), String.valueOf(response.getPageSize()));
		headers.set(PagingHeader.PAGE_OFFSET.getValue(), String.valueOf(response.getPageOffset()));
		headers.set(PagingHeader.PAGE_NUMBER.getValue(), String.valueOf(response.getPageNumber()));
		headers.set(PagingHeader.PAGE_TOTAL.getValue(), String.valueOf(response.getPageTotal()));
		return headers;
	}
}
