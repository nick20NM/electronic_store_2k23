package com.alpha.www.ElectronicStore.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
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

import com.alpha.www.ElectronicStore.dto.PageableResponse;
import com.alpha.www.ElectronicStore.dto.ProductDto;
import com.alpha.www.ElectronicStore.dto.UserDto;
import com.alpha.www.ElectronicStore.entity.Category;
import com.alpha.www.ElectronicStore.entity.Product;
import com.alpha.www.ElectronicStore.entity.User;
import com.alpha.www.ElectronicStore.exception.ResourceNotFoundException;
import com.alpha.www.ElectronicStore.repository.CategoryRepository;
import com.alpha.www.ElectronicStore.repository.ProductRepository;
import com.alpha.www.ElectronicStore.service.FileService;
import com.alpha.www.ElectronicStore.service.ProductService;
import com.alpha.www.ElectronicStore.util.MyUtility;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	private Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
	
	@Value("${product.image.path}")
	private String productImagePath;
	
	@Override
	public ProductDto createProduct(ProductDto productDto) {
		Product product = modelMapper.map(productDto, Product.class);
		
		// generating & setting random product id
		String productId = UUID.randomUUID().toString();
		product.setProductId(productId);
		
		// setting date
		product.setAddedDate(new Date());
		
		// saving product to DB
		Product savedProduct = productRepository.save(product);
		
		return modelMapper.map(savedProduct, ProductDto.class);
	}

	@Override
	public ProductDto updateProduct(String productId, ProductDto productDto) {
		// get product from DB
		Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("product not found with id: " + productId));
		
		// updating product
		product.setTitle(productDto.getTitle());
		product.setDescription(productDto.getDescription());
		product.setOriginalPrice(productDto.getOriginalPrice());
		product.setDiscountedPrice(productDto.getDiscountedPrice());
		// date
		product.setQuantity(productDto.getQuantity());
		product.setLive(productDto.isLive());
		product.setStock(productDto.isStock());
		product.setProductImageName(productDto.getProductImageName());
		
		// save product to DB
		Product updatedProduct = productRepository.save(product);
		
		return modelMapper.map(updatedProduct, ProductDto.class);
	}

	@Override
	public void deleteProduct(String productId) {
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("product not found with id: "+productId));
		
		// delete product image
		// images/products/ + abc.png -> images/products/abc.png 
		String fullImagePath = productImagePath + product.getProductImageName();
		
		
		try {
			Path path = Paths.get(fullImagePath);
			Files.delete(path);
		} catch (NoSuchFileException e) {
			logger.info("product image not found in folder"); // need some modification
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		productRepository.delete(product);
	}

	@Override
	public ProductDto getProductById(String productId) {
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("product not found with id: "+productId));
		return modelMapper.map(product, ProductDto.class);
	}

	@Override
	public PageableResponse<ProductDto> getAllProducts(int pageNo, int pageSize, String sortBy, String sortDir) {
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
		Page<Product> page = productRepository.findAll(pageable);
		PageableResponse<ProductDto> response = MyUtility.getPageableResponse(page, ProductDto.class);
		return response;
	}

	@Override
	public PageableResponse<ProductDto> getAllLiveProducts(int pageNo, int pageSize, String sortBy, String sortDir) {
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
		Page<Product> page = productRepository.findByLiveTrue(pageable);
		PageableResponse<ProductDto> response = MyUtility.getPageableResponse(page, ProductDto.class);
		return response;
	}

	@Override
	public PageableResponse<ProductDto> searchProductByTitle(String title, int pageNo, int pageSize, String sortBy, String sortDir) {
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
		Page<Product> page = productRepository.findByTitleContaining(title, pageable);
		PageableResponse<ProductDto> response = MyUtility.getPageableResponse(page, ProductDto.class);
		return response;
	}

	@Override
	public ProductDto createProductWithCategory(String categoryId, ProductDto productDto) {
		// fetch category from DB
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("category not found with id: "+categoryId));
		
		Product product = modelMapper.map(productDto, Product.class);
		
		// generating & setting random product id
		String productId = UUID.randomUUID().toString();
		product.setProductId(productId);
		
		// setting date
		product.setAddedDate(new Date());
		
		// seting category to product
		product.setCategory(category);
		
		// saving product to DB
		Product savedProduct = productRepository.save(product);
		
		return modelMapper.map(savedProduct, ProductDto.class);
	}

	@Override
	public ProductDto updateProductCategory(String productId, String categoryId) {
		// get product from DB
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("product not found with id: "+productId));
		// get category from DB
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("category not found with id: "+categoryId));
		// set category to product
		product.setCategory(category);
		
		// saved/updated product
		Product savedProduct = productRepository.save(product);
		return modelMapper.map(savedProduct, ProductDto.class);
	}

	@Override
	public PageableResponse<ProductDto> getCategoryProducts(String categoryId, int pageNo, int pageSize, String sortBy, String sortDir) {
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("category not found with id: "+categoryId));
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
		Page<Product> page = productRepository.findByCategory(category, pageable);
		return MyUtility.getPageableResponse(page, ProductDto.class);
	}

}
