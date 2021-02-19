package com.govtech.csvdata.service

import com.govtech.csvdata.dto.PagingBody
import com.govtech.csvdata.dto.PagingHeader
import com.govtech.csvdata.entity.Invoice
import com.govtech.csvdata.repo.InvoiceRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.*

@Service
class DataService(private val invoiceRepository: InvoiceRepository) {
    @Value("\${page.size}")
    private val defaultPageSize = 100

    fun fetchBySpec(spec: Specification<Invoice?>?, headers: HttpHeaders, sort: Sort?): PagingBody {
        val pageable = buildPageRequest(headers, sort)
        val page = invoiceRepository.findAll(spec, pageable)
        val content = page.content
        return PagingBody.builder()
            .count(page.totalElements)
            .pageNumber(page.number.toLong())
            .pageSize(page.numberOfElements.toLong())
            .pageOffset(pageable.offset)
            .pageTotal(page.totalPages.toLong())
            .elements(content)
            .build()
    }

    private fun buildPageRequest(headers: HttpHeaders, sort: Sort?): Pageable {
        // Page number can't be negative
        var page = 0
        if (headers.containsKey(PagingHeader.PAGE_NUMBER.value)) {
            val pageNumber = Objects.requireNonNull(headers[PagingHeader.PAGE_NUMBER.value]!![0])
            page = try {
                Math.max(0, pageNumber.toInt())
            } catch (exception: NumberFormatException) {
                throw ResponseStatusException(
                    HttpStatus.REQUEST_HEADER_FIELDS_TOO_LARGE,
                    String.format("The input Page-Number %s is an invalid Integer", pageNumber)
                )
            }
        }
        // Page size is capped at 100
        var size = defaultPageSize
        if (headers.containsKey(PagingHeader.PAGE_SIZE.value)) {
            val pageSize = Objects.requireNonNull(headers[PagingHeader.PAGE_SIZE.value]!![0])
            size = try {
                Math.min(size, pageSize.toInt())
            } catch (exception: NumberFormatException) {
                throw ResponseStatusException(
                    HttpStatus.REQUEST_HEADER_FIELDS_TOO_LARGE,
                    String.format("The input Page-Size %s is an invalid Integer", pageSize)
                )
            }
        }
        return PageRequest.of(page, size, sort)
    }
}