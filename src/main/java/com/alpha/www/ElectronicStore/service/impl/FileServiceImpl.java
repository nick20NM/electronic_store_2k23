package com.alpha.www.ElectronicStore.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alpha.www.ElectronicStore.exception.BadApiRequestException;
import com.alpha.www.ElectronicStore.service.FileService;

@Service
public class FileServiceImpl implements FileService {
	
	private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

	@Override
	public String uploadFile(MultipartFile file, String path) throws IOException {
		
		String originalFilename = file.getOriginalFilename();
		logger.info("originalFilename : {}", originalFilename);
		
		String fileName = UUID.randomUUID().toString();
		
		// abc.png  -> .png
		String extention = originalFilename.substring(originalFilename.lastIndexOf("."));
		
		String fileNameWithExtention = fileName + extention;
		
//		String fullPathWithFileName = path + File.separator + fileNameWithExtention;
		String fullPathWithFileName = path + fileNameWithExtention;
		
		logger.info("full image path : {} ", fullPathWithFileName);
		if (extention.equalsIgnoreCase(".png") || extention.equalsIgnoreCase(".jpg") || extention.equalsIgnoreCase(".jpeg")) {
			// then save file
			logger.info("file extention is: {} ", extention);
			File folder = new File(path);
			
			if (!folder.exists()) {
				// then create folder(s)
				folder.mkdirs();
			}
			
			// then upload file
			Files.copy(file.getInputStream(), Paths.get(fullPathWithFileName));
			
			return fileNameWithExtention;
		} else {
			throw new BadApiRequestException("file with extention: " + extention + " not allowed");
		}
	}

	@Override
	public InputStream getResource(String path, String name) throws FileNotFoundException {
		String fullPath = path + File.separator + name;
		InputStream inputStream = new FileInputStream(fullPath);
		return inputStream;
	}

}
