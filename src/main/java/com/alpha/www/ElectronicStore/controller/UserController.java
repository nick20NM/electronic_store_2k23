package com.alpha.www.ElectronicStore.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

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
import com.alpha.www.ElectronicStore.dto.ImageResponse;
import com.alpha.www.ElectronicStore.dto.PageableResponse;
import com.alpha.www.ElectronicStore.dto.UserDto;
import com.alpha.www.ElectronicStore.service.FileService;
import com.alpha.www.ElectronicStore.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
	
	private Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;
	
	@Autowired
	private FileService fileService;
	
	@Value("${user.profile.image.path}")
	private String imageUploadPath;
	
	// create
	@PostMapping
	public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto){
		UserDto savedUser = userService.createUser(userDto);
		return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
	}
	
	// update
	@PutMapping("/{userId}")
	public ResponseEntity<UserDto> updateUser(@PathVariable String userId, @Valid @RequestBody UserDto userDto){
		UserDto updatedUser = userService.updateUser(userId, userDto);
		return ResponseEntity.ok(updatedUser);
	}
	
	// delete
	@DeleteMapping("/{userId}")
	public ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable String userId){
		userService.deleteUser(userId);
		ApiResponseMessage message = ApiResponseMessage
			.builder()
			.message("user deleted successfully")
			.success(true)
			.status(HttpStatus.OK)
			.build();
		return ResponseEntity.ok(message);
	}
	
	// get all
	@GetMapping
	public ResponseEntity<PageableResponse<UserDto>> getAllUsers(
			@RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
			@RequestParam(value = "pageSize", defaultValue = "5", required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "name", required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
			){
		return ResponseEntity.ok(userService.getAllUsers(pageNo, pageSize, sortBy, sortDir));
	}
	
	// get single
	@GetMapping("/{userId}")
	public ResponseEntity<UserDto> getUserById(@PathVariable String userId){
		return ResponseEntity.ok(userService.getUserById(userId));
	}
	
	// get by email
	@GetMapping("/email/{emailId}")
	public ResponseEntity<UserDto> getUserByEmail(@PathVariable String emailId){
		return ResponseEntity.ok(userService.getUserByEmail(emailId));
	}
	
	// search user
	@GetMapping("/search/{keywords}")
	public ResponseEntity<List<UserDto>> searchUser(@PathVariable String keywords){
		return ResponseEntity.ok(userService.searchUser(keywords));
	}
	
	// upload user image
	@PostMapping("/image/{userId}")
	public ResponseEntity<ImageResponse> uploadUserImage(
			@RequestParam("userImage") MultipartFile image, 
			@PathVariable String userId
			) throws IOException{
		String imageName = fileService.uploadFile(image, imageUploadPath);
		
		// getting & updating user image name
		UserDto user = userService.getUserById(userId);
		user.setImageName(imageName);
		UserDto updatedUser = userService.updateUser(userId, user);
		
		ImageResponse response = ImageResponse
			.builder()
			.imageName(imageName)
			.message("image uploaded successfully")
			.success(true)
			.status(HttpStatus.CREATED)
			.build();
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	// serve user image
	@GetMapping("/image/{userId}")
	public void serveUserImage(@PathVariable String userId, HttpServletResponse response) throws IOException {
		UserDto user = userService.getUserById(userId);
		logger.info("user image name : {} ", user.getImageName());
		InputStream resource = fileService.getResource(imageUploadPath, user.getImageName());
		
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(resource, response.getOutputStream());
	}
}
