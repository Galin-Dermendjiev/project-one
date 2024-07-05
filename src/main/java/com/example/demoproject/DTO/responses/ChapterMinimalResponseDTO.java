package com.example.demoproject.DTO.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
class ChapterMinimalResponseDTO {
    private Long chapterId;
    private String title;
    private Boolean isPublished;
}
