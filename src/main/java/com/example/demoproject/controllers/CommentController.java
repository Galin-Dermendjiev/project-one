package com.example.demoproject.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demoproject.DTO.requests.CommentRequestDTO;
import com.example.demoproject.DTO.requests.CommentUpdateRequestDTO;
import com.example.demoproject.DTO.responses.CommentResponseDTO;
import com.example.demoproject.config.security.AuthorizationService;
import com.example.demoproject.service.CommentService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/comments")
public class CommentController {

    private final CommentService commentService;
    private final AuthorizationService authorizationService;
    
    @GetMapping("/comment/{commentId}")
    public ResponseEntity<CommentResponseDTO> getCommentById(@PathVariable Long commentId) {
        return ResponseEntity.ok(commentService.getCommentById(commentId));
    }
    
    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<CommentResponseDTO>> getAllCommentsForBook(@PathVariable Long bookId){
        return ResponseEntity.ok(commentService.getAllCommentsByBookId(bookId));
    }
    
    @GetMapping("/chapter/{chapterId}")
    public ResponseEntity<List<CommentResponseDTO>> getAllCommentsForChapter(@PathVariable Long chapterId){
        return ResponseEntity.ok(commentService.getAllCommentsByChapterId(chapterId));
    }
    
    @PostMapping("/book/{bookId}")
    public ResponseEntity<CommentResponseDTO> createCommentForBook(@PathVariable Long bookId, 
                                                            @RequestBody CommentRequestDTO comment){
        Long userId = authorizationService.getUserIdFromUsername();
        return ResponseEntity.ok(commentService.createCommentForBook(userId, bookId, comment));
    }
    
    @PostMapping("/chapter/{chapterId}")
    public ResponseEntity<CommentResponseDTO> createCommentForChapter(@PathVariable Long chapterId, 
                                                            @RequestBody CommentRequestDTO comment){
        Long userId = authorizationService.getUserIdFromUsername();
        return ResponseEntity.ok(commentService.createCommentForChapter(userId, chapterId, comment));
    }
    
    @PutMapping("/update")
    @PreAuthorize("@authorizationService.checkCommentOwnership(#comment.id)")
    public ResponseEntity<CommentResponseDTO> updateComment(@RequestBody CommentUpdateRequestDTO comment){
        return ResponseEntity.ok(commentService.updateComment(comment));
    }
    
    @DeleteMapping("/delete/{commentId}")
    @PreAuthorize("@authorizationService.checkCommentOwnership(#commentId)")
    public ResponseEntity<Void> deleteCommentById(@PathVariable Long commentId){
        commentService.deleteCommentById(commentId);
        return ResponseEntity.ok().build();
    }
}
