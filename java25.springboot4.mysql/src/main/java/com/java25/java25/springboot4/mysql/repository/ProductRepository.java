package com.java25.java25.springboot4.mysql.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.java25.java25.springboot4.mysql.entities.Product;

@Repository
public interface ProductRepository  extends JpaRepository<Product, Integer> {
    @Query(value = "SELECT * FROM products WHERE descriptions LIKE :keyword LIMIT :perpage OFFSET :offset", nativeQuery = true)
    List<Product> searchProduct(@Param("keyword") String keyword, @Param("perpage") int perpage, @Param("offset") int offset);
    
    @Query(value = "SELECT COUNT(*) as count FROM products WHERE descriptions LIKE :keyword", nativeQuery = true)
    int countProductsByKeyword(@Param("keyword") String keyword);    
    
    
    @Query(value = "SELECT * FROM products LIMIT :perpage OFFSET :offset", nativeQuery = true)
     List<Product> findProducts(@Param("perpage") int perpage, @Param("offset") int offset);
    
    
    
}
