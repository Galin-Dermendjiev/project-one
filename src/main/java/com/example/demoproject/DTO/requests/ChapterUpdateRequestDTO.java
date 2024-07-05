package com.example.demoproject.DTO.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChapterUpdateRequestDTO {
	private Long id;
	private String title;
    private String content;
    private Boolean isPublished;
}
