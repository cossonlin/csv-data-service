package com.govtech.csvdata.service;

import com.govtech.csvdata.entity.Invoice;
import com.govtech.csvdata.entity.Task;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CsvService {

	private final TaskService taskService;
	private final String dateTimeFormat;

	@Autowired
	public CsvService(@Value("${datetime.format}") String dateTimeFormat, TaskService taskService) {
		this.dateTimeFormat = dateTimeFormat;
		this.taskService = taskService;
	}

	public long saveCsvFile(MultipartFile file) {
		log.info("start saveCsvFile()");
		try (InputStream inputStream = file.getInputStream();
			 BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream))) {
			Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(fileReader);
			List<Invoice> invoiceList = new ArrayList<>();
			DateTimeFormatter format = DateTimeFormatter.ofPattern(dateTimeFormat);
			log.info("after parsed CSV");
			for (CSVRecord record : records) {
				Invoice invoice = Invoice.builder()
						.invoiceNo(record.get(0))
						.stockCode(record.get(1))
						.description(record.get(2))
						.quantity(Integer.parseInt(record.get(3)))
						.invoiceDate(LocalDateTime.parse(record.get(4), format))
						.unitPrice(new BigDecimal(record.get(5)))
						.customerID(record.get(6))
						.country(record.get(7))
						.build();
				invoiceList.add(invoice);
			}
			log.info("done populating list");
			taskService.saveList(invoiceList);
			int size = invoiceList.size();
			Task task = taskService.createTask(size);
			return task.getId();
		} catch (IOException e) {
			log.error("Encounter error while loading CSV file: {} ", e.getMessage());
			return 0;
		}
	}
}
