package com.example.demoproject.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demoproject.model.Rating;

public interface RatingRepository extends JpaRepository<Rating, Long> {

	Optional<Rating> findByUser_UserIdAndBook_BookId(Long userId, Long bookId);
	Optional<Rating> findByUser_UserIdAndChapter_ChapterId(Long userId, Long chapterId);

}
