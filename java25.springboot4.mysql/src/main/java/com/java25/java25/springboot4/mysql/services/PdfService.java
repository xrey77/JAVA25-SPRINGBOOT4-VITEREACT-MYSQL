package com.java25.java25.springboot4.mysql.services;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.Locale;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.java25.java25.springboot4.mysql.dto.ProductDto;
import com.java25.java25.springboot4.mysql.repository.ProductRepository;
import com.java25.java25.springboot4.mysql.repository.UserMapper;

@Service
public class PdfService {

    private final TemplateEngine templateEngine;
    private final ProductRepository productRepository;    
    private final UserMapper userMapper;    

    public PdfService(
            UserMapper userMapper,            
            TemplateEngine templateEngine,
            ProductRepository productRepository 
            ) {
        this.templateEngine = templateEngine;
        this.productRepository = productRepository;
        this.userMapper = userMapper;           
    }
    
    public List<ProductDto> listAllProducts() {
        return userMapper.toProductDtoList(productRepository.findAll());
    }    
    

    public byte[] generatePdfFromList(List<ProductDto> products) throws IOException {
        Context context = new Context();
        context.setVariable("products", products);

        String formattedDate = LocalDate.now().format(
            DateTimeFormatter.ofPattern("'As of' MMMM d, yyyy", Locale.ENGLISH)
        );
        context.setVariable("reportDate", formattedDate);

        byte[] imageBytes = new ClassPathResource("static/images/logo.png").getContentAsByteArray();
        String base64Logo = Base64.getEncoder().encodeToString(imageBytes);
        context.setVariable("logoBase64", "data:image/png;base64," + base64Logo);

        String htmlContent = templateEngine.process("report", context);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(out);
            return out.toByteArray();
        }
    }
}
