package com.example.demoproject.DTO.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookMinimalResponseDTO {
	private Long bookId;
    private String title;
    private String description;
    private String genre;
}
