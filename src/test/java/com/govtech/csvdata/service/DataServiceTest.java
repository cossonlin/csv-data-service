package com.govtech.csvdata.service;

import com.govtech.csvdata.dto.PagingBody;
import com.govtech.csvdata.dto.PagingHeader;
import com.govtech.csvdata.entity.Invoice;
import com.govtech.csvdata.repo.InvoiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class DataServiceTest {

	@Value("${page.size}")
	private int pageSize;
	private DataService dataService;
	private InvoiceRepository invoiceRepository = mock(InvoiceRepository.class);

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		this.dataService = new DataService(invoiceRepository);
	}

	@Test
	public void fetchBySpec_ShouldReturnPagingBody_WhenPageSizeAndSpecAreProvided() {
		Specification querySpec = mock(Specification.class);
		Sort sort = mock(Sort.class);
		HttpHeaders headers = new HttpHeaders();
		headers.add(PagingHeader.PAGE_NUMBER.getValue(), String.valueOf(0));
		headers.add(PagingHeader.PAGE_SIZE.getValue(), String.valueOf(5));

		PageRequest pageRequest = PageRequest.of(0, 5, sort);

		Invoice invoice1 = Invoice.builder().invoiceNo("1").build();
		Invoice invoice2 = Invoice.builder().invoiceNo("2").build();
		List<Invoice> invoiceList = new ArrayList<>();
		invoiceList.add(invoice1);
		invoiceList.add(invoice2);
		Page<Invoice> invoicePage = new PageImpl<>(invoiceList);

		when(invoiceRepository.findAll(any(Specification.class), (Pageable) any())).thenReturn(invoicePage);

		// When
		PagingBody pagingBody = dataService.fetchBySpec(querySpec, headers, sort);
		assertEquals(2L, pagingBody.getPageSize());
		assertEquals(0L, pagingBody.getPageNumber());
		assertEquals(1L, pagingBody.getPageTotal());
		verify(invoiceRepository, times(1)).findAll(querySpec, pageRequest);
	}
}
