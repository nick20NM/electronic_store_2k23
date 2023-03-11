package com.alpha.www.ElectronicStore.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {

	private String categoryId;
	
	@NotBlank(message = "title is required")
	@Size(min = 4, message = "title should be minimum of 4 characters")
	private String title;
	
	@NotBlank(message = "description required")
	private String description;
	
	private String coverImage;
	
	// other fields
}
