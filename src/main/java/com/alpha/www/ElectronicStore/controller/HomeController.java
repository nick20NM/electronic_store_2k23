package com.alpha.www.ElectronicStore.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class HomeController {

	@GetMapping
	public String test() {
		return "test";
	}
}
