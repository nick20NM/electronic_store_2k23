package com.alpha.www.ElectronicStore.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.alpha.www.ElectronicStore.validator.CheckImageName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

	private String userId;

	@Size(min = 3, max = 15, message = "name should be between 3 to 15 characters")
	private String name;

//	@Email(message = "invalid email")
	@Pattern(
			regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$", 
			message = "invalid email")
	@NotBlank(message = "email is required")
	private String email;

	@NotBlank(message = "password is required")
	private String password;

	@Size(min = 4, max = 15, message = "gender should be between 4 to 15 characters")
	private String gender;

	@NotBlank(message = "about is required")
	private String about;
	
//	@Pattern
	// custom validator

	@CheckImageName
	private String imageName;
}
