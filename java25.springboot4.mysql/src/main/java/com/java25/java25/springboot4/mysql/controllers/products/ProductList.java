package com.java25.java25.springboot4.mysql.controllers.products;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.java25.java25.springboot4.mysql.dto.ProductDto;
import com.java25.java25.springboot4.mysql.services.KafkaProducerService;
import com.java25.java25.springboot4.mysql.services.KafkaProducerService.KafkaEventType;
import com.java25.java25.springboot4.mysql.services.ProductService;

@RestController
@RequestMapping("/take")
public class ProductList {

	private final ProductService productService;
	private final KafkaProducerService kafkaProducerService;	
	
	public ProductList(ProductService productService, KafkaProducerService kafkaProducerService) {
	   this.productService = productService;	
	   this.kafkaProducerService = kafkaProducerService;				
	}
	
	
	@GetMapping(path="/productlist/{page}")
	public ResponseEntity<?> productList(@PathVariable Integer page) {
		int perpage = 5;
		int offset = (page - 1) * perpage;
		int totalRecords = productService.totalProductRecords();
		double total = Math.ceil((double)totalRecords / perpage);
		int totalPage = (int) total;
		
		List<ProductDto> products = productService.productList(perpage, offset);
		if (products.size() == 0) {
			Map<String, Object> response = new HashMap<>();
			response.put("message", "No record(s) found.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);			
		}
		
	    Map<String, Object> response = new HashMap<>();
	    response.put("page", page);
	    response.put("totpage", totalPage);
	    response.put("totalrecords", totalRecords);
	    response.put("products", products);
	    
		try {
			kafkaProducerService.sendEvent(KafkaEventType.PRODUCTS_LIST, products);
		} catch (Exception e) {
			Map<String, Object> errorMap = new HashMap<>();
			errorMap.put("message", "Failed to send Kafka event");
			errorMap.put("error", e.getMessage());
			
			return ResponseEntity.badRequest().body(errorMap); 			
		}


	    return ResponseEntity.ok(response); 		
	}
	
}
