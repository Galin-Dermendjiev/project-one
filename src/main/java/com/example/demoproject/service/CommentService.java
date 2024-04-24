package com.example.demoproject.service;

import java.util.List;

import com.example.demoproject.DTO.requests.CommentRequestDTO;
import com.example.demoproject.DTO.requests.CommentUpdateRequestDTO;
import com.example.demoproject.DTO.responses.CommentResponseDTO;

public interface CommentService {

	CommentResponseDTO createCommentForBook(Long userId, Long bookId, CommentRequestDTO commentRequestDTO);
    
    CommentResponseDTO createCommentForChapter(Long userId, Long chapterId, CommentRequestDTO commentRequestDTO);

    CommentResponseDTO getCommentById(Long commentId);
    
    List<CommentResponseDTO> getAllCommentsByBookId(Long bookId);
    
    List<CommentResponseDTO> getAllCommentsByChapterId(Long chapterId);
    
    CommentResponseDTO updateComment(CommentUpdateRequestDTO commentRequestDTO);
    
    void deleteCommentById(Long commentId);
}
