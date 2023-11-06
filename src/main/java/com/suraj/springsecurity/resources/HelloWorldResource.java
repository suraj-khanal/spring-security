package com.suraj.springsecurity.resources;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class HelloWorldResource {
	
	@GetMapping("/csrf-token")
	public CsrfToken retrieveCsrfToken(HttpServletRequest request) {
		
		return (CsrfToken) request.getAttribute("_csrf");
	}
	
	
}
