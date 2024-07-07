package com.example.demoproject.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.example.demoproject.DTO.requests.UserRequestDTO;
import com.example.demoproject.DTO.responses.UserResponseDTO;
import com.example.demoproject.dao.UserRepository;
import com.example.demoproject.model.User;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserRequestDTO userRequestDTO;
    private UserResponseDTO userResponseDTO;

    @BeforeEach
    public void setup() {
        user = new User();
        user.setUserId(1L);
        user.setUsername("ivan");
        user.setEmail("ivan@gmail.com");
        user.setProfilePicture("profile.jpg");
        user.setBio("Bio");

        userRequestDTO = new UserRequestDTO();
        userRequestDTO.setId(1L);
        userRequestDTO.setUsername("ivan");
        userRequestDTO.setEmail("ivan@gmail.com");
        userRequestDTO.setProfilePicture("profile.jpg");
        userRequestDTO.setBio("Bio");

        userResponseDTO = new UserResponseDTO();
        userResponseDTO.setUserId(1L);
        userResponseDTO.setUsername("ivan");
        userResponseDTO.setEmail("ivan@gmail.com");
        userResponseDTO.setProfilePicture("profile.jpg");
        userResponseDTO.setBio("Bio");
    }

    @Test
    public void getUserById_UserExists_ReturnsUserResponseDTO() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserResponseDTO.class)).thenReturn(userResponseDTO);

        UserResponseDTO result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals(userResponseDTO, result);
    }

    @Test
    public void getUserById_UserDoesNotExist_ThrowsEntityNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.getUserById(1L));
    }

    @Test
    public void getAllUsers_ReturnsListOfUserResponseDTO() {
        List<User> userList = Stream.of(user).toList();
        List<UserResponseDTO> userResponseDTOList = Stream.of(userResponseDTO).toList();

        when(userRepository.findAll()).thenReturn(userList);
        when(modelMapper.map(any(User.class), eq(UserResponseDTO.class))).thenReturn(userResponseDTO);

        List<UserResponseDTO> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(userResponseDTOList.size(), result.size());
        assertEquals(userResponseDTOList, result);
    }

    @Test
    public void updateUser_UserExists_ReturnsUpdatedUserResponseDTO() {
        when(userRepository.findById(userRequestDTO.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        when(modelMapper.map(user, UserResponseDTO.class)).thenReturn(userResponseDTO);

        UserResponseDTO result = userService.updateUser(userRequestDTO);

        assertNotNull(result);
        assertEquals(userResponseDTO, result);
    }

    @Test
    public void updateUser_UserDoesNotExist_ThrowsEntityNotFoundException() {
        when(userRepository.findById(userRequestDTO.getId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.updateUser(userRequestDTO));
    }

    @Test
    public void deleteUserById_UserExists_DeletesUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.deleteUserById(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    public void deleteUserById_UserDoesNotExist_ThrowsEntityNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.deleteUserById(1L));
    }
}
