package com.example.demoproject.DTO.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingUpdateRequestDTO {
	private Long id;
	private int score;
}
