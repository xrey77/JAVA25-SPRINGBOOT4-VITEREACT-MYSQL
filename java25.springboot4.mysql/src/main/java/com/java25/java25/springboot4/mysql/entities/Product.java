package com.java25.java25.springboot4.mysql.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity()
@Table(name="products")
@EntityListeners(AuditingEntityListener.class) // Enable auditing for this entity
public class Product implements Serializable {
	private static final long serialVersionUID = 5926468583005150707L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)	
	private int id;
	
	@Column(name="category")
	private String category;
	
	@Column(name="descriptions")
	private String descriptions;

	@Column(name="qty", columnDefinition = "integer default 0")
	private int qty;

	@Column(name="unit")
	private String unit;

	@Column(name="costprice", precision = 10, scale = 2)
	@ColumnDefault("0.00")
	private BigDecimal costprice;

	@Column(name="sellprice", precision = 10, scale = 2)
	@ColumnDefault("0.00")
	private BigDecimal sellprice;

	@Column(name="saleprice", precision = 10, scale = 2)
	@ColumnDefault("0.00")	
	private BigDecimal saleprice;

	@Column(name="productpicture")
	private String productpicture;

	@Column(name="alertstocks", columnDefinition = "integer default 0")
	private int alertstocks;
	
	@Column(name="criticalstocks", columnDefinition = "integer default 0")
	private int criticalstocks;
	
	// Auditing fields
//    @CreatedDate
    @CreationTimestamp
	@Column(name="created_at", nullable = false, updatable = false) // Ensures column name is correct and value set once
	private LocalDateTime created_at;
    
    @CreationTimestamp
    @LastModifiedDate
	@Column(name="updated_at", updatable = true)
	private LocalDateTime updated_at;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(String descriptions) {
		this.descriptions = descriptions;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public BigDecimal getCostprice() {
		return costprice;
	}

	public void setCostprice(BigDecimal costprice) {
		this.costprice = costprice;
	}

	public BigDecimal getSellprice() {
		return sellprice;
	}

	public void setSellprice(BigDecimal sellprice) {
		this.sellprice = sellprice;
	}

	public BigDecimal getSaleprice() {
		return saleprice;
	}

	public void setSaleprice(BigDecimal saleprice) {
		this.saleprice = saleprice;
	}

	public String getProductpicture() {
		return productpicture;
	}

	public void setProductpicture(String productpicture) {
		this.productpicture = productpicture;
	}

	public int getAlertstocks() {
		return alertstocks;
	}

	public void setAlertstocks(int alertstocks) {
		this.alertstocks = alertstocks;
	}

	public int getCriticalstocks() {
		return criticalstocks;
	}

	public void setCriticalstocks(int criticalstocks) {
		this.criticalstocks = criticalstocks;
	}

	public LocalDateTime getCreated_at() {
		return created_at;
	}

	public void setCreated_at(LocalDateTime created_at) {
		this.created_at = created_at;
	}

	public LocalDateTime getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(LocalDateTime updated_at) {
		this.updated_at = updated_at;
	}		
	
    
}
