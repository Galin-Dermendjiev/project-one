package com.example.demoproject.DTO.responses;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookResponseDTO {
	private Long bookId;
    private String title;
    private String description;
    private String genre;
    private String coverImage;
    private LocalDateTime creationDate;
    private LocalDateTime updateDate;
    
    private UserMinimalResponseDTO user; 
    private List<ChapterMinimalResponseDTO> chapters;
    private List<RatingResponseDTO> ratings;
    private List<CommentResponseDTO> comments; // make a minimal dto for comments w/out book check if chapter can stay
}
