package com.govtech.csvdata.service

import com.govtech.csvdata.entity.Invoice
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVRecord
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.CompletableFuture

@Service
class CsvService(private val taskService: TaskService) {

    val log = LoggerFactory.getLogger(this::class.java)

    @Value("\${datetime.format}")
    private var dateTimeFormat: String? = "M/d/yyyy H:m"

    fun saveCsvFile(file: MultipartFile): Long {
        log.info("start saveCsvFile()")
        try {
            file.inputStream.use { inputStream ->
                BufferedReader(InputStreamReader(inputStream)).use { fileReader ->
                    val records: Iterable<CSVRecord> = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(fileReader)
                    val invoiceList: MutableList<Invoice> = ArrayList()
                    val format = DateTimeFormatter.ofPattern(dateTimeFormat)
                    log.info("after parsed CSV")
                    for (record in records) {
                        val invoice = Invoice.builder()
                            .invoiceNo(record[0])
                            .stockCode(record[1])
                            .description(record[2])
                            .quantity(record[3].toInt())
                            .invoiceDate(LocalDateTime.parse(record[4], format))
                            .unitPrice(BigDecimal(record[5]))
                            .customerID(record[6])
                            .country(record[7])
                            .build()
                        invoiceList.add(invoice)
                    }
                    log.info("done populating list")
                    CompletableFuture.runAsync { taskService.saveList(invoiceList) }
                    val size = invoiceList.size
                    val task = taskService.createTask(size)
                    return task!!.id
                }
            }
        } catch (e: IOException) {
            log.error("Encounter error while loading CSV file: {} ", e.message)
            return 0
        }
    }
}