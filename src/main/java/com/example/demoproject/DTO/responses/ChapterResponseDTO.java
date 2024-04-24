package com.example.demoproject.DTO.responses;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChapterResponseDTO {
	private Long chapterId;
    private String title;
    private String content;
    private LocalDateTime creationDate;
    private LocalDateTime updateDate;
    
    private BookMinimalResponseDTO book;  
    private List<RatingResponseDTO> ratings;
    private List<CommentResponseDTO> comments; 
}
