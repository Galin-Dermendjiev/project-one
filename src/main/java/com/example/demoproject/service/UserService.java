package com.example.demoproject.service;

import java.util.List;

import com.example.demoproject.DTO.requests.UserRequestDTO;
import com.example.demoproject.DTO.responses.UserResponseDTO;

public interface UserService {
    
    UserResponseDTO getUserById(Long userId);
    
    List<UserResponseDTO> getAllUsers();
    
    UserResponseDTO updateUser(UserRequestDTO userRequestDTO);
    
    void deleteUserById(Long userId);
}
