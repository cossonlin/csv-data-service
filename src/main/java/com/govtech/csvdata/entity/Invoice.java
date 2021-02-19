package com.govtech.csvdata.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private String invoiceNo;

	@Column
	private String stockCode;

	@Column
	private String description;

	@Column
	private int quantity;

	@Column
	private LocalDateTime invoiceDate;

	@Column
	private BigDecimal unitPrice;

	@Column
	private String customerID;

	@Column
	private String country;

	public static InvoiceBuilder builder() {
		return new InvoiceBuilder();
	}

	public static class InvoiceBuilder {
		private Long id;
		private String invoiceNo;
		private String stockCode;
		private String description;
		private int quantity;
		private LocalDateTime invoiceDate;
		private BigDecimal unitPrice;
		private String customerID;
		private String country;

		InvoiceBuilder() {
		}

		public InvoiceBuilder id(long id) {
			this.id = id;
			return this;
		}

		public InvoiceBuilder invoiceNo(String invoiceNo) {
			this.invoiceNo = invoiceNo;
			return this;
		}

		public InvoiceBuilder stockCode(String stockCode) {
			this.stockCode = stockCode;
			return this;
		}

		public InvoiceBuilder description(String description) {
			this.description = description;
			return this;
		}

		public InvoiceBuilder quantity(int quantity) {
			this.quantity = quantity;
			return this;
		}

		public InvoiceBuilder invoiceDate(LocalDateTime invoiceDate) {
			this.invoiceDate = invoiceDate;
			return this;
		}

		public InvoiceBuilder unitPrice(BigDecimal unitPrice) {
			this.unitPrice = unitPrice;
			return this;
		}

		public InvoiceBuilder customerID(String customerID) {
			this.customerID = customerID;
			return this;
		}

		public InvoiceBuilder country(String country) {
			this.country = country;
			return this;
		}

		public Invoice build() {
			return new Invoice(id, invoiceNo, stockCode, description, quantity, invoiceDate, unitPrice, customerID, country);
		}
	}
}
