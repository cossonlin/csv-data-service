package com.govtech.csvdata.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
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
}
