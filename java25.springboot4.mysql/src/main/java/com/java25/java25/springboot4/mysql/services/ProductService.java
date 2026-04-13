package com.java25.java25.springboot4.mysql.services;

import java.util.List;
import org.springframework.stereotype.Service;
import com.java25.java25.springboot4.mysql.dto.ProductDto;
import com.java25.java25.springboot4.mysql.dto.SaleDto;
import com.java25.java25.springboot4.mysql.entities.Product;
import com.java25.java25.springboot4.mysql.entities.Sale;
import com.java25.java25.springboot4.mysql.repository.ProductRepository;
import com.java25.java25.springboot4.mysql.repository.SaleRepository;
import com.java25.java25.springboot4.mysql.repository.UserMapper;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProductService {

	private final SaleRepository saleRepository;
	private final ProductRepository productRepository;	
    private final UserMapper userMapper;		
	
	public ProductService(
            UserMapper userMapper,            			
			SaleRepository saleRepository,			
			ProductRepository productRepository) {
		this.saleRepository = saleRepository;
		this.productRepository = productRepository;
        this.userMapper = userMapper;        		
	}
	
	public int totalProductRecords() {
		return (int) productRepository.count();
	}
	
	public int searchTotalProducts(String keyword) {
		return productRepository.countProductsByKeyword(keyword);
	}
		
    public List<ProductDto> productSearch(String keyword, int perpage, int offset) {
        List<Product> products = productRepository.searchProduct(keyword, perpage, offset);
        return userMapper.toProductDtoList(products);    	
    }        
		
    public List<ProductDto> productList(int perpage, int offset) {
        List<Product> products = productRepository.findProducts(perpage, offset);
        return userMapper.toProductDtoList(products);    	
    }        
	        
    public List<SaleDto> getAllSales() {
        List<Sale> sales = saleRepository.findAll();
        return userMapper.toSaleDtoList(sales);    	
    }        	
}
