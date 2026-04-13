package com.java25.java25.springboot4.mysql.dto;

import java.util.Date;

public class SaleDto {

	private Double amount;
	private Date date;
	
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
}
