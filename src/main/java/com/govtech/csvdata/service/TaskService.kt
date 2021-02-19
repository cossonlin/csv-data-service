package com.govtech.csvdata.service

import com.govtech.csvdata.entity.Invoice
import com.govtech.csvdata.entity.Task
import com.govtech.csvdata.repo.InvoiceRepository
import com.govtech.csvdata.repo.TaskRepository
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.stream.IntStream
import java.util.stream.Stream

@Service
class TaskService(private val invoiceRepository: InvoiceRepository, private val taskRepository: TaskRepository) {

    val log = LoggerFactory.getLogger(this::class.java)

    private fun <T> batches(source: List<T>, length: Int): Stream<List<T>> {
        require(length > 0) { "length = $length" }
        val size = source.size
        if (size <= 0) return Stream.empty()
        val fullChunks = (size - 1) / length
        return IntStream.range(0, fullChunks + 1)
            .mapToObj { n: Int -> source.subList(n * length, if (n == fullChunks) size else (n + 1) * length) }
    }

    fun createTask(size: Int): Task? {
        val task = Task.builder()
            .totalElement(size)
            .build()
        return taskRepository.save(task)
    }

    fun saveList(invoiceList: List<Invoice>) {
        invoiceRepository.deleteAllInBatch()
        log.info("Cleaned historical data")
        val invoiceStream = batches(invoiceList, 1000)
        invoiceStream.forEach { iterable: List<Invoice> ->
            invoiceRepository.saveAll(iterable)
        }
        log.info("Persisted new data")
    }

    fun queryTaskProgress(taskId: Long): Int {
        val optionalTask = taskRepository.findById(taskId)
        return if (optionalTask.isPresent) {
            val task = optionalTask.get()
            val total = task.totalElement
            val savedElement = invoiceRepository.count().toInt()
            savedElement * 100 / total
        } else {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Can't find this task ID $taskId")
        }
    }
}