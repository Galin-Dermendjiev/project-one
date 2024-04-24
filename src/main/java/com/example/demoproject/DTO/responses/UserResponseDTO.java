package com.example.demoproject.DTO.responses;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
	
	private Long userId;
	private String username;
	private String email;
	private String profilePicture;
	private String bio;
	private LocalDateTime registrationDate;
	private List<BookMinimalResponseDTO> books;
	private List<CommentResponseDTO> comments;
	private List<RatingResponseDTO> ratings;
	private List<BookMinimalResponseDTO> favoriteBooks;
}
