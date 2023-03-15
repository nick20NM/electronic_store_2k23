package com.alpha.www.ElectronicStore.controller;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

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
import com.alpha.www.ElectronicStore.dto.ImageResponse;
import com.alpha.www.ElectronicStore.dto.PageableResponse;
import com.alpha.www.ElectronicStore.dto.ProductDto;
import com.alpha.www.ElectronicStore.dto.UserDto;
import com.alpha.www.ElectronicStore.service.FileService;
import com.alpha.www.ElectronicStore.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private FileService fileService;
	
	@Value("${product.image.path}")
	private String productImagePath;
	
	private Logger logger = LoggerFactory.getLogger(ProductController.class);

	// create
	@PostMapping
	public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto){
		ProductDto createdProduct = productService.createProduct(productDto);
		return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
	}
	
	// update
	@PutMapping("/{productId}")
	public ResponseEntity<ProductDto> updateProduct(
			@PathVariable String productId, 
			@RequestBody ProductDto productDto
			){
		ProductDto updatedProduct = productService.updateProduct(productId, productDto);
		return ResponseEntity.ok(updatedProduct);
	}
	
	
	// delete
	@DeleteMapping("/{productId}")
	public ResponseEntity<ApiResponseMessage> deleteProduct(@PathVariable String productId){
		productService.deleteProduct(productId);
		
		ApiResponseMessage response = ApiResponseMessage
				.builder()
				.message("product deleted successfully with id: "+productId)
				.success(true)
				.status(HttpStatus.OK)
				.build();
		
		return ResponseEntity.ok(response);
	}
	
	// get single
	@GetMapping("/{productId}")
	public ResponseEntity<ProductDto> getProductById(@PathVariable String productId){
		ProductDto productDto = productService.getProductById(productId);
		return ResponseEntity.ok(productDto);
	}
	
	// get all
	@GetMapping
	public ResponseEntity<PageableResponse<ProductDto>> getAllProducts(
			@RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
			@RequestParam(value = "pageSize", defaultValue = "5", required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
			){
		return ResponseEntity.ok(productService.getAllProducts(pageNo, pageSize, sortBy, sortDir));
	}
	
	// get all - live
	// localhost:8080/products/live
	@GetMapping("/live")
	public ResponseEntity<PageableResponse<ProductDto>> getAllLiveProducts(
			@RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
			@RequestParam(value = "pageSize", defaultValue = "5", required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
			){
		return ResponseEntity.ok(productService.getAllLiveProducts(pageNo, pageSize, sortBy, sortDir));
	}
	
	// search
	@GetMapping("/search/{title}") // title or keywords
	public ResponseEntity<PageableResponse<ProductDto>> searchProductsByTitle(
			@PathVariable String title,
			@RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
			@RequestParam(value = "pageSize", defaultValue = "5", required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
			){
		return ResponseEntity.ok(productService.searchProductByTitle(title, pageNo, pageSize, sortBy, sortDir));
	}
	
	// upload product image
	@PostMapping("/image/{productId}")
	public ResponseEntity<ImageResponse> uploadProductImage(
			@RequestParam("productImage") MultipartFile image, 
			@PathVariable String productId
			) throws IOException{
		String imageName = fileService.uploadFile(image, productImagePath);
		
		// getting & updating user image name
		ProductDto productDto = productService.getProductById(productId);
		productDto.setProductImageName(imageName);
		ProductDto updatedProduct = productService.updateProduct(productId, productDto);
		
		ImageResponse response = ImageResponse
			.builder()
			.imageName(imageName) // .imageName(updatedProduct.getPoductImageName())
			.message("image uploaded successfully")
			.success(true)
			.status(HttpStatus.CREATED)
			.build();
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	// serve product image
	@GetMapping("/image/{productId}")
	public void serveUserImage(
			@PathVariable String productId, 
			HttpServletResponse response
			) throws IOException {
		ProductDto productDto = productService.getProductById(productId);
		logger.info("product image name : {} ", productDto.getProductImageName());
		InputStream resource = fileService.getResource(productImagePath, productDto.getProductImageName());
		
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(resource, response.getOutputStream());
	}
}
