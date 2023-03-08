package com.alpha.www.ElectronicStore.service.impl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alpha.www.ElectronicStore.dto.UserDto;
import com.alpha.www.ElectronicStore.entity.User;
import com.alpha.www.ElectronicStore.repository.UserRepository;
import com.alpha.www.ElectronicStore.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public UserDto createUser(UserDto userDto) {
		
		User user = modelMapper.map(userDto, User.class);
		String userId = UUID.randomUUID().toString();
		user.setUserId(userId);
		User savedUser = userRepository.save(user);
		return modelMapper.map(savedUser, UserDto.class);
	}

	@Override
	public UserDto updateUser(String userId, UserDto userDto) {
		// get user from db
		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("user not found with id: "+userId));
		
		// update user
		user.setName(userDto.getName());
		// email -> update nhi karna h
		user.setPassword(userDto.getPassword());
		user.setGender(userDto.getGender());
		user.setAbout(userDto.getAbout());
		user.setImageName(userDto.getImageName());
		
		// save user to db
		User savedUser = userRepository.save(user);
		
		return modelMapper.map(savedUser, UserDto.class);
	}

	@Override
	public void deleteUser(String userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("user not found with id: "+userId));
		userRepository.delete(user);
	}

	@Override
	public List<UserDto> getAllUsers() {
		List<User> users = userRepository.findAll();
		List<UserDto> userDtos = users.stream().map(user -> modelMapper.map(user, UserDto.class)).collect(Collectors.toList());
		return userDtos;
	}

	@Override
	public UserDto getUserById(String userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("user not found with id: "+userId));
		return modelMapper.map(user, UserDto.class);
	}

	@Override
	public UserDto getUserByEmail(String email) {
		User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("user not found with email: "+email));
		return modelMapper.map(user, UserDto.class);
	}

	@Override
	public List<UserDto> searchUser(String keyword) {
		List<User> users = userRepository.findByNameContaining(keyword);
		return users.stream().map(user -> modelMapper.map(user, UserDto.class)).collect(Collectors.toList());
	}

}
