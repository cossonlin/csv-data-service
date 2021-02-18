package com.govtech.csvdata.service;

import com.govtech.csvdata.entity.Invoice;
import com.govtech.csvdata.entity.Task;
import com.govtech.csvdata.repo.InvoiceRepository;
import com.govtech.csvdata.repo.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TaskServiceTest {

	private TaskService taskService;
	private InvoiceRepository invoiceRepository = mock(InvoiceRepository.class);
	private TaskRepository taskRepository = mock(TaskRepository.class);

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		this.taskService = new TaskService(invoiceRepository, taskRepository);
	}

	@Test
	public void createTask_ShouldReturnTask_WhenSaveInTaskTable() {
		Task expectedTask = Task.builder().totalElement(100).build();
		when(taskRepository.save(any(Task.class))).thenReturn(expectedTask);

		Task task = taskService.createTask(100);
		assertEquals(100, task.getTotalElement());
	}

	@Test
	public void queryTaskProgress_ShouldReturnPercentage_WhenProvideTaskId() {
		Task expectedTask = Task.builder().id(1L).totalElement(100).build();
		when(taskRepository.findById(1L)).thenReturn(Optional.of(expectedTask));
		when(invoiceRepository.count()).thenReturn(90L);

		int percentage = taskService.queryTaskProgress(1L);
		assertEquals(90, percentage);
	}

	@Test
	public void saveList_ShouldReturnPercentage_WhenProvideTaskId() {
		Invoice invoice = Invoice.builder().build();
		List<Invoice> invoiceList = Collections.singletonList(invoice);
		when(invoiceRepository.saveAll(anyList())).thenReturn(invoiceList);

		taskService.saveList(invoiceList);
		verify(invoiceRepository, times(1)).saveAll(invoiceList);
	}
}
