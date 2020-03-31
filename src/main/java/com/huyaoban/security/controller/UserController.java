package com.huyaoban.security.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/api")
public class UserController {

	@GetMapping("/hello")
	public String hello() {
		String currentUser = "";

		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			currentUser = ((UserDetails) principal).getUsername();
		} else {
			currentUser = principal.toString();
		}

		return "Hello, user " + currentUser;
	}
}
