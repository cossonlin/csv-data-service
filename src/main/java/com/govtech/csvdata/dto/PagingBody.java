package com.govtech.csvdata.dto;

import com.govtech.csvdata.entity.Invoice;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PagingBody {

	private Long count;

	private Long pageNumber;

	private Long pageSize;

	private Long pageOffset;

	private Long pageTotal;

	private List<Invoice> elements;
}
