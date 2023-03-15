package com.alpha.www.ElectronicStore.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.alpha.www.ElectronicStore.entity.Category;
import com.alpha.www.ElectronicStore.entity.Product;

public interface ProductRepository extends JpaRepository<Product, String> {

	// search
	Page<Product> findByTitleContaining(String title, Pageable pageable);
	
	Page<Product> findByLiveTrue(Pageable pageable);
	
	Page<Product> findByCategory(Category category, Pageable pageable);
	
	// other methods
	
	// custom finder methods
	
	// query methods
}
