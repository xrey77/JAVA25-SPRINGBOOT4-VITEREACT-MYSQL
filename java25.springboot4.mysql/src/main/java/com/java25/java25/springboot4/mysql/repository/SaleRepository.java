package com.java25.java25.springboot4.mysql.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.java25.java25.springboot4.mysql.entities.Sale;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

}
