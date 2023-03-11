package com.alpha.www.ElectronicStore.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.alpha.www.ElectronicStore.dto.CategoryDto;
import com.alpha.www.ElectronicStore.dto.PageableResponse;
import com.alpha.www.ElectronicStore.entity.Category;
import com.alpha.www.ElectronicStore.exception.ResourceNotFoundException;
import com.alpha.www.ElectronicStore.repository.CategoryRepository;
import com.alpha.www.ElectronicStore.service.CategoryService;
import com.alpha.www.ElectronicStore.util.MyUtility;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Value("${category.image.path}")
	private String categoryImagePath;
	
	private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);
	
	@Override
	public CategoryDto createCategory(CategoryDto categoryDto) {
		
		// generating & setting random category id in categoryDto
//		String categoryDtoId = UUID.randomUUID().toString();
//		categoryDto.setCategoryId(categoryDtoId);
		
		Category category = modelMapper.map(categoryDto, Category.class);
		
		// generating & setting random category id
		String categoryId = UUID.randomUUID().toString();
		category.setCategoryId(categoryId);
		
		Category savedCategory = categoryRepository.save(category);
		return modelMapper.map(savedCategory, CategoryDto.class);
	}

	@Override
	public CategoryDto updateCategory(String categoryId, CategoryDto categoryDto) {
		// get category from DB
		Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("category not found with id: "+categoryId));
		
		// update category
		category.setTitle(categoryDto.getTitle());
		category.setDescription(categoryDto.getDescription());
		category.setCoverImage(categoryDto.getCoverImage());
		
		// save category to DB
		Category savedCategory = categoryRepository.save(category);
		
		return modelMapper.map(savedCategory, CategoryDto.class);
	}

	@Override
	public void deleteCategory(String categoryId) {
		Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("resource not found with id: "+categoryId));
		
		// delete category cover image
		// images/categories/ + abc.png -> images/categories/abc.png 
		String fullImagePath = categoryImagePath + category.getCoverImage();
		
		
		try {
			Path path = Paths.get(fullImagePath);
			Files.delete(path);
		} catch (NoSuchFileException e) {
			logger.info("category cover image not found in folder"); // need some modification
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		categoryRepository.delete(category);
	}

	@Override
	public PageableResponse<CategoryDto> getAllCategories(int pageNo, int pageSize, String sortBy, String sortDir) {
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
		Page<Category> page = categoryRepository.findAll(pageable);
		PageableResponse<CategoryDto> response = MyUtility.getPageableResponse(page, CategoryDto.class);
		return response;
	}

	@Override
	public CategoryDto getCategoryById(String categoryId) {
		Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("category not found with id: "+categoryId));
		return modelMapper.map(category, CategoryDto.class);
	}

}
