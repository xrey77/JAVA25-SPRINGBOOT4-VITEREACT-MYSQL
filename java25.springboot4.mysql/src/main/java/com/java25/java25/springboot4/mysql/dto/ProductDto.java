package com.java25.java25.springboot4.mysql.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record ProductDto(
    Long id,
    
    @NotBlank(message = "Category is required")
    String category,
    
    String descriptions,
    
    @NotNull(message = "Quantity is required")
    @PositiveOrZero(message = "Quantity cannot be negative")
    Integer qty,
    
    String unit,
    
    @NotNull(message = "Cost price is required")
    @PositiveOrZero(message = "Cost price cannot be negative")
    BigDecimal costPrice,
    
    @NotNull(message = "Sell price is required")
    @PositiveOrZero(message = "Sell price cannot be negative")
    BigDecimal sellPrice,
    
    BigDecimal salePrice,
    
    String productPicture,
    
    Integer alertStocks,
    
    Integer criticalStocks
) {}
