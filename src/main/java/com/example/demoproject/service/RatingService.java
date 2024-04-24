package com.example.demoproject.service;

import java.util.List;

import com.example.demoproject.DTO.requests.RatingRequestDTO;
import com.example.demoproject.DTO.requests.RatingUpdateRequestDTO;
import com.example.demoproject.DTO.responses.RatingResponseDTO;

public interface RatingService {
	 
	RatingResponseDTO createRatingForBook(Long userId, Long bookId, RatingRequestDTO ratingRequestDTO);
    
    RatingResponseDTO createRatingForChapter(Long userId, Long chapterId, RatingRequestDTO ratingRequestDTO);

    RatingResponseDTO getRatingById(Long ratingId);
    
    List<RatingResponseDTO> getAllRatingsByBookId(Long bookId);
    
    List<RatingResponseDTO> getAllRatingsByChapterId(Long chapterId);
    
    RatingResponseDTO updateRating(RatingUpdateRequestDTO ratingRequestDTO);
    
    void deleteRatingById(Long ratingId);
}
