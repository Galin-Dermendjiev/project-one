package com.example.demoproject.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demoproject.DTO.requests.UserRequestDTO;
import com.example.demoproject.DTO.responses.UserResponseDTO;
import com.example.demoproject.service.UserService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
	
	private final UserService userService;
	
	@GetMapping
	public ResponseEntity<List<UserResponseDTO>> getAllUsers(){
		return ResponseEntity.ok(userService.getAllUsers());
	}
	
	@GetMapping("/{userId}")
	public ResponseEntity<UserResponseDTO> getUserById(@PathVariable("userId") Long userId){
		return ResponseEntity.ok(userService.getUserById(userId));
	}
	
	@PutMapping
	@PreAuthorize("@authorizationService.checkUserOwnership(#user.id)")
	public ResponseEntity<UserResponseDTO> updateUser(@RequestBody UserRequestDTO user){
		return ResponseEntity.ok(userService.updateUser(user));
		
	}
	
	@DeleteMapping("/{userId}")
	@PreAuthorize("@authorizationService.checkUserOwnership(#userId)")
	public void deleteUser(@PathVariable Long userId) {
		userService.deleteUserById(userId);
	}
	
}
