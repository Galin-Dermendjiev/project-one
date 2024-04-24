package com.example.demoproject.DTO.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingResponseDTO {
	private Long ratingId;
    private int score;
    
    private UserMinimalResponseDTO user;
}
