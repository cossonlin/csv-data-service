package com.govtech.csvdata.service;

import com.govtech.csvdata.entity.Invoice;
import com.govtech.csvdata.entity.Task;
import com.govtech.csvdata.repo.InvoiceRepository;
import com.govtech.csvdata.repo.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Slf4j
@Service
public class TaskService {

	private final InvoiceRepository invoiceRepository;
	private final TaskRepository taskRepository;

	@Autowired
	public TaskService(InvoiceRepository invoiceRepository, TaskRepository taskRepository) {
		this.invoiceRepository = invoiceRepository;
		this.taskRepository = taskRepository;
	}

	private static <T> Stream<List<T>> batches(List<T> source, int length) {
		if (length <= 0)
			throw new IllegalArgumentException("length = " + length);
		int size = source.size();
		if (size <= 0)
			return Stream.empty();
		int fullChunks = (size - 1) / length;
		return IntStream.range(0, fullChunks + 1).mapToObj(
				n -> source.subList(n * length, n == fullChunks ? size : (n + 1) * length));
	}

	public Task createTask(int size) {
		Task task = Task.builder()
				.totalElement(size)
				.build();
		return taskRepository.save(task);
	}

	@Async
	public void saveList(List<Invoice> invoiceList) {
		invoiceRepository.deleteAllInBatch();
		log.info("Cleaned historical data");
		Stream<List<Invoice>> invoiceStream = batches(invoiceList, 30);
		invoiceStream.forEach(invoiceRepository::saveAll);
		log.info("Persisted new data");
	}

	public int queryTaskProgress(long taskId) {
		Optional<Task> optionalTask = taskRepository.findById(taskId);
		if (optionalTask.isPresent()) {
			Task task = optionalTask.get();
			int total = task.getTotalElement();
			int savedElement = (int) invoiceRepository.count();
			return (savedElement * 100) / total;
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Can't find this task ID " + taskId);
		}
	}
}
