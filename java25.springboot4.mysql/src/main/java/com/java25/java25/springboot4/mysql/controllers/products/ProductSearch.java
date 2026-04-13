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
public class ProductSearch {

	private final ProductService productService;
	private final KafkaProducerService kafkaProducerService;	
	
	public ProductSearch(KafkaProducerService kafkaProducerService, ProductService productService) {
	   this.productService = productService;
	   this.kafkaProducerService = kafkaProducerService;
	}
			
	@GetMapping(path="/products/search/{page}/{key}")
	public ResponseEntity<Map<String, ?>> productSearch(@PathVariable int page, @PathVariable String key) {

		String search = "%" + key + "%";
		int perpage = 5;
		int offset = (page - 1) * perpage;
		int totalRecords = productService.searchTotalProducts(search);
		double total = Math.ceil((double)totalRecords / perpage);
		int totalPage = (int) total;
		
		List<ProductDto> products = productService.productSearch(search, perpage, offset);
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
			kafkaProducerService.sendEvent(KafkaEventType.PRODUCT_SEARCH, response);
		} catch (Exception e) {
			Map<String, String> errorMap = new HashMap<>();
			errorMap.put("message", "Failed to send Kafka event");
			errorMap.put("error", e.getMessage());
			
			return ResponseEntity.badRequest().body(errorMap); 					
		}	

	    return ResponseEntity.ok(response); 						
	}
}
