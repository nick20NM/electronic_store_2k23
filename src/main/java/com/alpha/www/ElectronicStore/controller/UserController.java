package com.alpha.www.ElectronicStore.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alpha.www.ElectronicStore.dto.ApiResponseMessage;
import com.alpha.www.ElectronicStore.dto.UserDto;
import com.alpha.www.ElectronicStore.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;
	
	// create
	@PostMapping
	public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto){
		UserDto savedUser = userService.createUser(userDto);
		return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
	}
	
	// update
	@PutMapping("/{userId}")
	public ResponseEntity<UserDto> updateUser(@PathVariable String userId, @RequestBody UserDto userDto){
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
	public ResponseEntity<List<UserDto>> getAllUsers(){
		return ResponseEntity.ok(userService.getAllUsers());
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
}
