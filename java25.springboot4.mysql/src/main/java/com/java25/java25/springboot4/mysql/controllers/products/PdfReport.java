package com.java25.java25.springboot4.mysql.controllers.products;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.java25.java25.springboot4.mysql.dto.ProductDto;
import com.java25.java25.springboot4.mysql.services.KafkaProducerService;
import com.java25.java25.springboot4.mysql.services.KafkaProducerService.KafkaEventType;
import com.java25.java25.springboot4.mysql.services.PdfService;

@RestController
@RequestMapping("/take")
public class PdfReport {
    
    private final PdfService pdfService;
    private final KafkaProducerService kafkaProducerService;	
    
    public PdfReport(KafkaProducerService kafkaProducerService, PdfService pdfService) {
        this.pdfService = pdfService;
        this.kafkaProducerService = kafkaProducerService;
    }
    
    @GetMapping("/productreport")
    public ResponseEntity<?> downloadPdf() throws IOException { 
        List<ProductDto> products = pdfService.listAllProducts();
        byte[] pdfContents = pdfService.generatePdfFromList(products);
        
        try {
            kafkaProducerService.sendEvent(KafkaEventType.PDF_REPORT, pdfContents);
        } catch (Exception e) {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("message", "Failed to send Kafka event");
            errorMap.put("error", e.getMessage());
            
            return ResponseEntity.badRequest().body(errorMap); 					
        }				

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"product-report.pdf\"")
                .body(pdfContents); 
    }
}
