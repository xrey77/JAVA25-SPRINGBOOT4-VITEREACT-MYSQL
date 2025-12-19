package com.java25.java25.springboot4.mysql.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.java25.java25.springboot4.mysql.dto.ProductDto;
import com.java25.java25.springboot4.mysql.entities.Product;
import com.java25.java25.springboot4.mysql.repository.ProductRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProductService {

	private final ProductRepository productRepository;
	private final ModelMapper modelMapper;

	
	public ProductService(
			ModelMapper modelMapper,
			ProductRepository productRepository) {
		this.modelMapper = modelMapper;
		this.productRepository = productRepository;
	}
	
	public int totalProductRecords() {
		return (int) productRepository.count();
	}
	
	public int searchTotalProducts(String keyword) {
		return productRepository.countProductsByKeyword(keyword);
	}
	
	
    public List<ProductDto> productSearch(String keyword, int perpage, int offset) {
        List<Product> products = productRepository.searchProduct(keyword, perpage, offset);
        List<ProductDto> productlistDto = products.stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .collect(Collectors.toList());
        return productlistDto;
    }        
	
	
    public List<ProductDto> productList(int perpage, int offset) {
        List<Product> products = productRepository.findProducts(perpage, offset);
        List<ProductDto> productlistDto = products.stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .collect(Collectors.toList());
        return productlistDto;
    }        
	
	
}
