package com.java25.java25.springboot4.mysql.controllers.products;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.java25.java25.springboot4.mysql.dto.SaleDto;
import com.java25.java25.springboot4.mysql.entities.Sale;
import com.java25.java25.springboot4.mysql.services.KafkaProducerService;
import com.java25.java25.springboot4.mysql.services.KafkaProducerService.KafkaEventType;
import com.java25.java25.springboot4.mysql.services.ProductService;

@RestController
@RequestMapping("/take")
public class SalesChart {

    private final ProductService productService;
    private final KafkaProducerService kafkaProducerService;	

    public SalesChart(KafkaProducerService kafkaProducerService, ProductService productService) {
        this.productService = productService;
        this.kafkaProducerService = kafkaProducerService;        
    }

    @GetMapping(path="/chartdata")
    public ResponseEntity<?> showChart() {
        List<SaleDto> sales = productService.getAllSales();   
        
        try {
            kafkaProducerService.sendEvent(KafkaEventType.SALES_CHART, sales);
        } catch (Exception e) {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("message", "Failed to send Kafka event");
            errorMap.put("error", e.getMessage());
            
            return ResponseEntity.badRequest().body(errorMap); 					
        }

        return ResponseEntity.ok(sales);		      
    }
}
