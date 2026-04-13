package com.java25.java25.springboot4.mysql.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "products")
@EntityListeners(AuditingEntityListener.class) // Enable auditing for this entity
public class Product implements Serializable {
    
    private static final long serialVersionUID = 5926468583005150707L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)	
    private long id;
	
    private String category;
	
    private String descriptions;

    @Column(columnDefinition = "integer default 0")
    private int qty;

    private String unit;

    @Column(precision = 10, scale = 2)
    @ColumnDefault("0.00")
    private BigDecimal costprice;

    @Column(precision = 10, scale = 2)
    @ColumnDefault("0.00")
    private BigDecimal sellprice;

    @Column(precision = 10, scale = 2)
    @ColumnDefault("0.00")	
    private BigDecimal saleprice;

    private String productpicture;

    @Column(columnDefinition = "integer default 0")
    private int alertstocks;
	
    @Column(columnDefinition = "integer default 0")
    private int criticalstocks;
	
    @CreatedDate // Spring Data annotation matching your listener
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate // Spring Data annotation matching your listener
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Default Constructor
    public Product() {}

    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDescriptions() { return descriptions; }
    public void setDescriptions(String descriptions) { this.descriptions = descriptions; }

    public int getQty() { return qty; }
    public void setQty(int qty) { this.qty = qty; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public BigDecimal getCostprice() { return costprice; }
    public void setCostprice(BigDecimal costprice) { this.costprice = costprice; }

    public BigDecimal getSellprice() { return sellprice; }
    public void setSellprice(BigDecimal sellprice) { this.sellprice = sellprice; }

    public BigDecimal getSaleprice() { return saleprice; }
    public void setSaleprice(BigDecimal saleprice) { this.saleprice = saleprice; }

    public String getProductpicture() { return productpicture; }
    public void setProductpicture(String productpicture) { this.productpicture = productpicture; }

    public int getAlertstocks() { return alertstocks; }
    public void setAlertstocks(int alertstocks) { this.alertstocks = alertstocks; }

    public int getCriticalstocks() { return criticalstocks; }
    public void setCriticalstocks(int criticalstocks) { this.criticalstocks = criticalstocks; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }		
}
