package com.alpha.www.ElectronicStore;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ElectronicStoreApplicationTests {

	@Test
	void contextLoads() {
	}
	
	@Test
	void testCode() {
		try {
			Path path = Paths.get("");
			Files.delete(path);
		} catch (NoSuchFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	

}
