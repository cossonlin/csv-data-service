package com.govtech.csvdata.dto;

import com.govtech.csvdata.entity.Invoice;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PagingBody {

	public Long count;

	public Long pageNumber;

	public Long pageSize;

	public Long pageOffset;

	public Long pageTotal;

	public List<Invoice> elements;

	public static PagingBodyBuilder builder() {
		return new PagingBodyBuilder();
	}

	public static class PagingBodyBuilder {
		private Long count;
		private Long pageNumber;
		private Long pageSize;
		private Long pageOffset;
		private Long pageTotal;
		private List<Invoice> elements;

		PagingBodyBuilder() {
		}

		public PagingBodyBuilder count(long count) {
			this.count = count;
			return this;
		}

		public PagingBodyBuilder pageNumber(long pageNumber) {
			this.pageNumber = pageNumber;
			return this;
		}

		public PagingBodyBuilder pageSize(long pageSize) {
			this.pageSize = pageSize;
			return this;
		}

		public PagingBodyBuilder pageOffset(long pageOffset) {
			this.pageOffset = pageOffset;
			return this;
		}

		public PagingBodyBuilder pageTotal(long pageTotal) {
			this.pageTotal = pageTotal;
			return this;
		}

		public PagingBodyBuilder elements(List<Invoice> elements) {
			this.elements = elements;
			return this;
		}

		public PagingBody build() {
			return new PagingBody(count, pageNumber, pageSize, pageOffset, pageTotal, elements);
		}
	}
}
