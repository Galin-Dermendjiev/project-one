package com.example.demoproject.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.example.demoproject.DTO.requests.UserRequestDTO;
import com.example.demoproject.DTO.responses.UserResponseDTO;
import com.example.demoproject.dao.UserRepository;
import com.example.demoproject.model.User;
import com.example.demoproject.service.UserService;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {
	
	private final UserRepository userRepository;
	private final ModelMapper modelMapper;

	@Override
	public UserResponseDTO getUserById(Long userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));;
		return modelMapper.map(user, UserResponseDTO.class);
	}

	@Override
	public List<UserResponseDTO> getAllUsers() {
		List<User> users = userRepository.findAll();
		return users.stream().map(user -> modelMapper.map(user, UserResponseDTO.class)).toList();
	}

	@Override
	public UserResponseDTO updateUser(UserRequestDTO user) {
		
		User updatedUser = userRepository.findById(user.getId())
                 .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + user.getId()));

		updatedUser.setUsername(user.getUsername());
		updatedUser.setEmail(user.getEmail());
		updatedUser.setProfilePicture(user.getProfilePicture());
		updatedUser.setBio(user.getBio());
		
		userRepository.save(updatedUser);
		
		return modelMapper.map(updatedUser, UserResponseDTO.class);
	}

	@Override
	public void deleteUserById(Long userId) {
		User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

		userRepository.deleteById(userId);
	}

}
