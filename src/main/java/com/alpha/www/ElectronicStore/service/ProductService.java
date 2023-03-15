package com.alpha.www.ElectronicStore.service;

import java.util.List;

import com.alpha.www.ElectronicStore.dto.PageableResponse;
import com.alpha.www.ElectronicStore.dto.ProductDto;

public interface ProductService {

	// create
	ProductDto createProduct(ProductDto productDto);
	
	// update
	ProductDto updateProduct(String productId, ProductDto productDto);
	
	// delete
	void deleteProduct(String productId);
	
	// get single
	ProductDto getProductById(String productId);
	
	// get all
	PageableResponse<ProductDto> getAllProducts(int pageNo, int pageSize, String sortBy, String sortDir);
	
	// get all - live
	PageableResponse<ProductDto> getAllLiveProducts(int pageNo, int pageSize, String sortBy, String sortDir);
	
	// search product
	PageableResponse<ProductDto> searchProductByTitle(String title, int pageNo, int pageSize, String sortBy, String sortDir);
	
	// create product with category
	ProductDto createProductWithCategory(String categoryId, ProductDto productDto);
	
	// update/set product category / assign product to a category
	ProductDto updateProductCategory(String productId, String categoryId);
	
	// get category products
	PageableResponse<ProductDto> getCategoryProducts(String categoryId, int pageNo, int pageSize, String sortBy, String sortDir);
	
	// other methods
	
	
}
