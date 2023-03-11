package com.alpha.www.ElectronicStore.service;

import com.alpha.www.ElectronicStore.dto.CategoryDto;
import com.alpha.www.ElectronicStore.dto.PageableResponse;

public interface CategoryService {

	// create
	CategoryDto createCategory(CategoryDto categoryDto);
	
	// update
	CategoryDto updateCategory(String categoryId, CategoryDto categoryDto);
	
	// delete
	void deleteCategory(String categoryId);
	
	// get all
	PageableResponse<CategoryDto> getAllCategories(int pageNo, int pageSize, String sortBy, String sortDir);
	
	// get single
	CategoryDto getCategoryById(String categoryId);
	
	// search & other methods as per requirement
}
