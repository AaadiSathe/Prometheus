package com.smart.dao;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smart.entity.addProduct;
@Repository
public interface addProductRepository extends JpaRepository<addProduct, Integer> {

	// In the repository interface
	Page<addProduct> findByCategory(String category, Pageable pageable);

}
