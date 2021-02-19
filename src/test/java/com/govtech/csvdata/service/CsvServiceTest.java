package com.govtech.csvdata.service;

import com.govtech.csvdata.entity.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CsvServiceTest {

	@Value("${datetime.format}")
	String dateTimeFormat;
	private CsvService csvService;
	private TaskService taskService = mock(TaskService.class);

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		this.csvService = new CsvService(taskService);
	}

	@Test
	public void saveCsvFile_ShouldReturnTaskId_WhenStartSavingRecordsFromCsv() throws IOException {
		long expectedTaskId = 1;
		Task task = Task.builder().id(expectedTaskId).build();
		MultipartFile file = new MockMultipartFile("test-data.csv", new FileInputStream("src/test/resources/test-data.csv"));

		when(taskService.createTask(anyInt())).thenReturn(task);

		long returnTaskId = csvService.saveCsvFile(file);

		assertEquals(expectedTaskId, returnTaskId);
	}
}
