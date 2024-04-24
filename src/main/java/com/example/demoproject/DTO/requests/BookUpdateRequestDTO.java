package com.example.demoproject.DTO.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookUpdateRequestDTO {
	private Long id;
	private String title;
    private String description;
    private String genre;
    private String coverImage;
}
