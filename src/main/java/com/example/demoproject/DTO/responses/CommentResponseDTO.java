package com.example.demoproject.DTO.responses;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDTO {
	private Long commentId;
    private String content;
    private LocalDateTime creationDate;
    private LocalDateTime updateDate;
    
    private UserMinimalResponseDTO user;
}
