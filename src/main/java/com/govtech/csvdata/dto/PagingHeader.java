package com.govtech.csvdata.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PagingHeader {
	PAGE_SIZE("Page-Size"),
	PAGE_NUMBER("Page-Number"),
	PAGE_OFFSET("Page-Offset"),
	PAGE_TOTAL("Page-Total"),
	COUNT("Count");

	private final String value;
}
