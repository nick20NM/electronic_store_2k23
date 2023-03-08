package com.alpha.www.ElectronicStore.service;

import java.util.List;

import com.alpha.www.ElectronicStore.dto.UserDto;

public interface UserService {

	// create
	UserDto createUser(UserDto userDto);
	
	// update
	UserDto updateUser(String userId, UserDto userDto);
	
	// delete
	void deleteUser(String userId);
	
	// get all users
	List<UserDto> getAllUsers();
	
	// get single user by id
	UserDto getUserById(String userId);
	
	// get single user by email
	UserDto getUserByEmail(String email);
	
	// search user
	List<UserDto> searchUser(String keyword);
	
	// other user specific features
}
