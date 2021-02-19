package com.govtech.csvdata.controller

import com.govtech.csvdata.dto.PagingBody
import com.govtech.csvdata.dto.PagingHeader
import com.govtech.csvdata.entity.Invoice
import com.govtech.csvdata.service.CsvService
import com.govtech.csvdata.service.DataService
import lombok.extern.slf4j.Slf4j
import net.kaczmarzyk.spring.data.jpa.domain.*
import net.kaczmarzyk.spring.data.jpa.web.annotation.And
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@Slf4j
@RestController
@RequestMapping("/csvdata")
class CsvDataController(private val csvService: CsvService, private val dataService: DataService) {

    @PostMapping("/file")
    fun uploadCsvFile(@RequestParam("file") file: MultipartFile): ResponseEntity<Long>? {
        val taskId = csvService.saveCsvFile(file)
        return ResponseEntity.ok(taskId)
    }

    @GetMapping("/data/search")
    fun searchData(
        @And(
            Spec(path = "invoiceNo", params = ["invoiceNo"], spec = Like::class),
            Spec(path = "stockCode", params = ["stockCode"], spec = Like::class),
            Spec(path = "description", params = ["description"], spec = Like::class),
            Spec(path = "quantity", params = ["quantityMin", "quantityMax"], spec = Between::class),
            Spec(path = "invoiceDate", params = ["invoiceDateMin", "invoiceDateMax"], spec = Between::class),
            Spec(path = "unitPrice", params = ["unitPriceLow", "unitPriceHigh"], spec = Between::class),
            Spec(path = "unitPrice", params = ["unitPriceMin"], spec = GreaterThanOrEqual::class),
            Spec(path = "unitPrice", params = ["unitPriceMax"], spec = LessThanOrEqual::class),
            Spec(path = "customerID", params = ["customerID"], spec = Equal::class),
            Spec(path = "country", params = ["country"], spec = Like::class)
        )
        spec: Specification<Invoice?>?,
        sort: Sort?,
        @RequestHeader headers: HttpHeaders
    ): ResponseEntity<List<Invoice?>?> {
        val response = dataService.fetchBySpec(spec, headers, sort)
        return ResponseEntity(response.elements, createHttpHeaders(response), HttpStatus.OK)
    }

    private fun createHttpHeaders(response: PagingBody): HttpHeaders {
        val headers = HttpHeaders()
        headers[PagingHeader.COUNT.value] = response.count.toString()
        headers[PagingHeader.PAGE_SIZE.value] = response.pageSize.toString()
        headers[PagingHeader.PAGE_OFFSET.value] = response.pageOffset.toString()
        headers[PagingHeader.PAGE_NUMBER.value] = response.pageNumber.toString()
        headers[PagingHeader.PAGE_TOTAL.value] = response.pageTotal.toString()
        return headers
    }
}