package com.alpha.www.ElectronicStore.controller;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alpha.www.ElectronicStore.dto.ApiResponseMessage;
import com.alpha.www.ElectronicStore.dto.CategoryDto;
import com.alpha.www.ElectronicStore.dto.ImageResponse;
import com.alpha.www.ElectronicStore.dto.PageableResponse;
import com.alpha.www.ElectronicStore.dto.ProductDto;
import com.alpha.www.ElectronicStore.service.CategoryService;
import com.alpha.www.ElectronicStore.service.FileService;
import com.alpha.www.ElectronicStore.service.ProductService;

@RestController
@RequestMapping("/categories")
public class CategoryController {
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private ProductService productService;
	
	@Value("${category.image.path}")
	private String categoryUploadPath;
	
	private Logger logger = LoggerFactory.getLogger(CategoryController.class);

	// create
	@PostMapping
	public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto){
		CategoryDto createdCategory = categoryService.createCategory(categoryDto);
		return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
	}
	
	// update
	@PutMapping("/{categoryId}")
	public ResponseEntity<CategoryDto> updateCategory(
			@PathVariable String categoryId, 
			@Valid @RequestBody CategoryDto categoryDto
			){
		CategoryDto updatedCategory = categoryService.updateCategory(categoryId, categoryDto);
		return ResponseEntity.ok(updatedCategory);
	}
	
	// delete
	@DeleteMapping("/{categoryId}")
	public ResponseEntity<ApiResponseMessage> deleteCategory(@PathVariable String categoryId){
		categoryService.deleteCategory(categoryId);
		ApiResponseMessage response = ApiResponseMessage
			.builder()
			.message("category deleted successfully")
			.success(true)
			.status(HttpStatus.OK)
			.build();
		return ResponseEntity.ok(response);
	}
	
	// get all
	@GetMapping
	public ResponseEntity<PageableResponse<CategoryDto>> getAllCategories(
			@RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
			@RequestParam(value = "pageSize", defaultValue = "5", required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
			){
		return ResponseEntity.ok(categoryService.getAllCategories(pageNo, pageSize, sortBy, sortDir));
	}
	
	// get single
	@GetMapping("/{categoryId}")
	public ResponseEntity<CategoryDto> getCategoryById(@PathVariable String categoryId){
		CategoryDto categoryDto = categoryService.getCategoryById(categoryId);
		return ResponseEntity.ok(categoryDto);
	}
	
	// upload category image
	@PostMapping("/image/{categoryId}")
	public ResponseEntity<ImageResponse> uploadCategoryImage(
			@RequestParam("categoryImage") MultipartFile image, 
			@PathVariable String categoryId
			) throws IOException{
		String imageName = fileService.uploadFile(image, categoryUploadPath);
		
		// getting & updating user image name
		CategoryDto categoryDto = categoryService.getCategoryById(categoryId);
		categoryDto.setCoverImage(imageName);
		CategoryDto updatedCategoryDto = categoryService.updateCategory(categoryId, categoryDto);
		
		ImageResponse response = ImageResponse
			.builder()
			.imageName(imageName)
			.message("image uploaded successfully")
			.success(true)
			.status(HttpStatus.CREATED)
			.build();
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
		
	// serve category image
	@GetMapping("/image/{categoryId}")
	public void serveUserImage(@PathVariable String categoryId, HttpServletResponse response) throws IOException {
		CategoryDto categoryDto = categoryService.getCategoryById(categoryId);
		logger.info("category image name : {} ", categoryDto.getCoverImage());
		InputStream resource = fileService.getResource(categoryUploadPath, categoryDto.getCoverImage());
		
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(resource, response.getOutputStream());
	}
	
	// create product with category
	@PostMapping("/{categoryId}/products")
	public ResponseEntity<ProductDto> createProductWithCategory(
			@PathVariable String categoryId,
			@RequestBody ProductDto productDto
			){
		ProductDto productWithCategory = productService.createProductWithCategory(categoryId, productDto);
		return new ResponseEntity<>(productWithCategory, HttpStatus.CREATED);
	}
	
	// update/set product category
	@PutMapping("/{categoryId}/products/{productId}")
	public ResponseEntity<ProductDto> updateProductCategory(
			@PathVariable String categoryId,
			@PathVariable String productId
			){
		ProductDto productDto = productService.updateProductCategory(productId, categoryId);
		return new ResponseEntity<>(productDto, HttpStatus.CREATED);
	}
	
	// get category products
	@GetMapping("/{categoryId}/products")
	public ResponseEntity<PageableResponse<ProductDto>> getCategoryProducts(
			@PathVariable String categoryId,
			@RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
			@RequestParam(value = "pageSize", defaultValue = "5", required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
			){
		PageableResponse<ProductDto> response = productService.getCategoryProducts(categoryId, pageNo, pageSize, sortBy, sortDir);
		return ResponseEntity.ok(response);
	}
}
