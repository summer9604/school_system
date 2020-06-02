package org.ricardo.school_system.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.ricardo.school_system.services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class HomeController {
	
	@Autowired
	private LoginService loginService;
	
	@GetMapping("/")
	public ResponseEntity<?> homePage(){
		return new ResponseEntity<>("Welcome to the School System!", HttpStatus.OK);
	}
	
	@GetMapping("/logout")
	public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response){
		return loginService.logout(request, response);
	}
	
}





