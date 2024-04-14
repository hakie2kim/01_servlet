package com.portfolio.www.domain;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class ElectricBillForm {
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date measureDate;

	private int elecAmount;
	private int discountType1;
	private int discountType2;
}