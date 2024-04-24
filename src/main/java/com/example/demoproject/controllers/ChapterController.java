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

import com.example.demoproject.DTO.requests.ChapterRequestDTO;
import com.example.demoproject.DTO.requests.ChapterUpdateRequestDTO;
import com.example.demoproject.DTO.responses.ChapterResponseDTO;
import com.example.demoproject.service.ChapterService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/chapters")
public class ChapterController {

    private final ChapterService chapterService;
    
    @GetMapping("/byBook/{bookId}")
    public ResponseEntity<List<ChapterResponseDTO>> getAllChaptersForBook(@PathVariable Long bookId){
        return ResponseEntity.ok(chapterService.getAllChaptersByBookId(bookId));
    }
    
    @GetMapping("/{chapterId}")
    public ResponseEntity<ChapterResponseDTO> getChapterById(@PathVariable Long chapterId){
        return ResponseEntity.ok(chapterService.getChapterById(chapterId));
    }
    
    @PostMapping("/create/{bookId}")
    @PreAuthorize("@authorizationService.checkBookOwnership(#bookId)")
    public ResponseEntity<ChapterResponseDTO> createChapter(@PathVariable Long bookId, @RequestBody ChapterRequestDTO chapter){
        return ResponseEntity.ok(chapterService.createChapter(bookId, chapter));
    }
    
    @PutMapping("/update")
    @PreAuthorize("@authorizationService.checkChapterOwnership(#chapter.id)")
    public ResponseEntity<ChapterResponseDTO> updateChapter(@RequestBody ChapterUpdateRequestDTO chapter){
        return ResponseEntity.ok(chapterService.updateChapter(chapter));
    }
    
    @DeleteMapping("/delete/{chapterId}")
    @PreAuthorize("@authorizationService.checkChapterOwnership(#chapterId)")
    public ResponseEntity<Void> deleteChapterById(@PathVariable Long chapterId){
        chapterService.deleteChapterById(chapterId);
        return ResponseEntity.ok().build();
    }
}

