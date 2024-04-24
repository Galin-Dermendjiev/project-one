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

import com.example.demoproject.DTO.requests.RatingRequestDTO;
import com.example.demoproject.DTO.requests.RatingUpdateRequestDTO;
import com.example.demoproject.DTO.responses.RatingResponseDTO;
import com.example.demoproject.config.security.AuthorizationService;
import com.example.demoproject.service.RatingService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/ratings")
public class RatingController {

    private final RatingService ratingService;
    private final AuthorizationService authorizationService;
    
    @GetMapping("/rating/{ratingId}")
    public ResponseEntity<RatingResponseDTO> getRatingById(@PathVariable Long ratingId){
        return ResponseEntity.ok(ratingService.getRatingById(ratingId));
    }
    
    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<RatingResponseDTO>> getAllRatingsForBook(@PathVariable Long bookId){
        return ResponseEntity.ok(ratingService.getAllRatingsByBookId(bookId));
    }
    
    @GetMapping("/chapter/{chapterId}")
    public ResponseEntity<List<RatingResponseDTO>> getAllRatingsForChapter(@PathVariable Long chapterId){
        return ResponseEntity.ok(ratingService.getAllRatingsByChapterId(chapterId));
    }
    
    @PostMapping("/book/{bookId}")
    public ResponseEntity<RatingResponseDTO> createRatingForBook(@PathVariable Long bookId, 
                                                                @RequestBody RatingRequestDTO rating){
        Long userId = authorizationService.getUserIdFromUsername();
        return ResponseEntity.ok(ratingService.createRatingForBook(userId, bookId, rating));
    }
    
    @PostMapping("/chapter/{chapterId}")
    public ResponseEntity<RatingResponseDTO> createRatingForChapter(@PathVariable Long chapterId, 
                                                                @RequestBody RatingRequestDTO rating){
        Long userId = authorizationService.getUserIdFromUsername();
        return ResponseEntity.ok(ratingService.createRatingForChapter(userId, chapterId, rating));
    }
    
    @PutMapping("/update")
    @PreAuthorize("@authorizationService.checkRatingOwnership(#rating.id)")
    public ResponseEntity<RatingResponseDTO> updateRating(@RequestBody RatingUpdateRequestDTO rating){
        return ResponseEntity.ok(ratingService.updateRating(rating));
    }
    
    @DeleteMapping("/delete/{ratingId}")
    @PreAuthorize("@authorizationService.checkRatingOwnership(#ratingId)")
    public ResponseEntity<Void> deleteRatingById(@PathVariable Long ratingId){
        ratingService.deleteRatingById(ratingId);
        return ResponseEntity.ok().build();
    }
}
