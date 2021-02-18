package com.govtech.csvdata.service;

import com.govtech.csvdata.dto.PagingBody;
import com.govtech.csvdata.dto.PagingHeader;
import com.govtech.csvdata.entity.Invoice;
import com.govtech.csvdata.repo.InvoiceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class DataService {

	private final int defaultPageSize;
	private final InvoiceRepository invoiceRepository;

	@Autowired
	public DataService(@Value("${page.size}") int defaultPageSize, InvoiceRepository invoiceRepository) {
		this.defaultPageSize = defaultPageSize;
		this.invoiceRepository = invoiceRepository;
	}

	public PagingBody fetchBySpec(Specification<Invoice> spec, HttpHeaders headers, Sort sort) {
		Pageable pageable = buildPageRequest(headers, sort);
		Page<Invoice> page = invoiceRepository.findAll(spec, pageable);
		List<Invoice> content = page.getContent();
		return PagingBody.builder()
				.count(page.getTotalElements())
				.pageNumber((long) page.getNumber())
				.pageSize((long) page.getNumberOfElements())
				.pageOffset(pageable.getOffset())
				.pageTotal((long) page.getTotalPages())
				.elements(content)
				.build();
	}

	private Pageable buildPageRequest(HttpHeaders headers, Sort sort) {
		// Page number can't be negative
		int page = 0;
		if (headers.containsKey(PagingHeader.PAGE_NUMBER.getValue())) {
			String pageNumber = Objects.requireNonNull(headers.get(PagingHeader.PAGE_NUMBER.getValue()).get(0));
			try {
				page = Math.max(0, Integer.parseInt(pageNumber));
			} catch (NumberFormatException exception) {
				throw new ResponseStatusException(HttpStatus.REQUEST_HEADER_FIELDS_TOO_LARGE, String.format("The input Page-Number %s is an invalid Integer", pageNumber));
			}
		}
		// Page size is capped at 100
		int size = defaultPageSize;
		if (headers.containsKey(PagingHeader.PAGE_SIZE.getValue())) {
			String pageSize = Objects.requireNonNull(headers.get(PagingHeader.PAGE_SIZE.getValue()).get(0));
			try {
				size = Math.min(size, Integer.parseInt(pageSize));
			} catch (NumberFormatException exception) {
				throw new ResponseStatusException(HttpStatus.REQUEST_HEADER_FIELDS_TOO_LARGE, String.format("The input Page-Size %s is an invalid Integer", pageSize));
			}
		}
		return PageRequest.of(page, size, sort);
	}
}
