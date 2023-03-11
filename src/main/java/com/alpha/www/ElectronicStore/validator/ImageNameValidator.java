package com.alpha.www.ElectronicStore.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImageNameValidator implements ConstraintValidator<CheckImageName, String> {
	
	private Logger logger = LoggerFactory.getLogger(ImageNameValidator.class);

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		
		logger.info("message from isValid : {} ", value);
		
		// logic
		if (value.isBlank()) {
			return false;
		} else {
			return true;
		}
		
	}

}
