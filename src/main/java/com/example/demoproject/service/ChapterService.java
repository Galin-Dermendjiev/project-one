package com.example.demoproject.service;

import java.util.List;

import com.example.demoproject.DTO.requests.ChapterRequestDTO;
import com.example.demoproject.DTO.requests.ChapterUpdateRequestDTO;
import com.example.demoproject.DTO.responses.ChapterResponseDTO;

public interface ChapterService {
	ChapterResponseDTO createChapter(Long bookId, ChapterRequestDTO chapterRequestDTO);
    
    ChapterResponseDTO getChapterById(Long chapterId);
    
    List<ChapterResponseDTO> getAllChaptersByBookId(Long bookId);
    
    ChapterResponseDTO updateChapter(ChapterUpdateRequestDTO chapterRequestDTO);
    
    void deleteChapterById(Long chapterId);
    
    void publishChapter(Long chapterId);
}
