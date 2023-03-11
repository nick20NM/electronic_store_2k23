package com.alpha.www.ElectronicStore.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
import com.alpha.www.ElectronicStore.dto.UserDto;
import com.alpha.www.ElectronicStore.entity.User;
import com.alpha.www.ElectronicStore.exception.ResourceNotFoundException;
import com.alpha.www.ElectronicStore.repository.UserRepository;
import com.alpha.www.ElectronicStore.service.UserService;
import com.alpha.www.ElectronicStore.util.MyUtility;

@Service
public class UserServiceImpl implements UserService {
	
	private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Value("${user.profile.image.path}")
	private String imagePath;
	
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
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user not found with id: "+userId));
		
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
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user not found with id: "+userId));
		
		// delete user profile image
		// images/users/ + abc.png -> images/users/abc.png 
		String fullImagePath = imagePath + user.getImageName();
		
		
		try {
			Path path = Paths.get(fullImagePath);
			Files.delete(path);
		} catch (NoSuchFileException e) {
			logger.info("user image not found in folder"); // need some modification
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		userRepository.delete(user);
	}

	@Override
	public PageableResponse<UserDto> getAllUsers(int pageNo, int pageSize, String sortBy, String sortDir) {
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
		Page<User> page = userRepository.findAll(pageable);
		
//		List<User> users = page.getContent();
//		List<UserDto> userDtos = users.stream().map(user -> modelMapper.map(user, UserDto.class)).collect(Collectors.toList());
//		
//		PageableResponse<UserDto> response = new PageableResponse<>();
//		response.setContent(userDtos);
//		response.setPageNo(page.getNumber());
//		response.setPageSize(page.getSize());
//		response.setTotalElements(page.getTotalElements());
//		response.setTotalPages(page.getTotalPages());
//		response.setLastPage(page.isLast());
		
		PageableResponse<UserDto> response = MyUtility.getPageableResponse(page, UserDto.class);
		
		return response;
	}

	@Override
	public UserDto getUserById(String userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user not found with id: "+userId));
		return modelMapper.map(user, UserDto.class);
	}

	@Override
	public UserDto getUserByEmail(String email) {
		User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("user not found with email: "+email));
		return modelMapper.map(user, UserDto.class);
	}

	@Override
	public List<UserDto> searchUser(String keyword) {
		List<User> users = userRepository.findByNameContaining(keyword);
		return users.stream().map(user -> modelMapper.map(user, UserDto.class)).collect(Collectors.toList());
	}

}
