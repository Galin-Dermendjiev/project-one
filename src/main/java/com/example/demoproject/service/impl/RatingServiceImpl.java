package com.example.demoproject.service.impl;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.example.demoproject.DTO.requests.RatingRequestDTO;
import com.example.demoproject.DTO.requests.RatingUpdateRequestDTO;
import com.example.demoproject.DTO.responses.RatingResponseDTO;
import com.example.demoproject.dao.BookRepository;
import com.example.demoproject.dao.ChapterRepository;
import com.example.demoproject.dao.RatingRepository;
import com.example.demoproject.dao.UserRepository;
import com.example.demoproject.model.Book;
import com.example.demoproject.model.Chapter;
import com.example.demoproject.model.Rating;
import com.example.demoproject.model.User;
import com.example.demoproject.service.RatingService;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RatingServiceImpl implements RatingService {
	
	private final RatingRepository ratingRepository;
	private final UserRepository userRepository;
	private final BookRepository bookRepository;
	private final ChapterRepository chapterRepository;
	private final ModelMapper modelMapper;
	
	@Override
	public RatingResponseDTO createRatingForBook(Long userId, Long bookId, RatingRequestDTO ratingRequestDTO) {
		 Optional<Rating> existingRating = ratingRepository.findByUser_UserIdAndBook_BookId(userId, bookId);

		    if (existingRating.isPresent()) {
		        // Update existing rating
		        Rating rating = existingRating.get();
		        rating.setScore(ratingRequestDTO.getScore()); // Update score
		        ratingRepository.save(rating);
		        return modelMapper.map(rating, RatingResponseDTO.class);
		    } else {
		        // Create new rating
		        User user = userRepository.findById(userId)
		                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
		        Book book = bookRepository.findById(bookId)
		                .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + bookId));

		        Rating newRating = modelMapper.map(ratingRequestDTO, Rating.class);
		        newRating.setUser(user);
		        newRating.setBook(book);

		        ratingRepository.save(newRating);
		        return modelMapper.map(newRating, RatingResponseDTO.class);
		    }
	}

	@Override
	public RatingResponseDTO createRatingForChapter(Long userId, Long chapterId, RatingRequestDTO ratingRequestDTO) {
		Optional<Rating> existingRating = ratingRepository.findByUser_UserIdAndChapter_ChapterId(userId, chapterId);

	    if (existingRating.isPresent()) {
	        // Update existing rating
	        Rating rating = existingRating.get();
	        rating.setScore(ratingRequestDTO.getScore()); // Update score
	        ratingRepository.save(rating);
	        return modelMapper.map(rating, RatingResponseDTO.class);
	    } else {
	        // Create new rating
	    	User user = userRepository.findById(userId)
					.orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
			Chapter chapter = chapterRepository.findById(chapterId)
					.orElseThrow(() -> new EntityNotFoundException("Chapter not found with id: " + chapterId));
			
			Rating newRating = modelMapper.map(ratingRequestDTO, Rating.class);
			newRating.setUser(user);
			newRating.setChapter(chapter);
			
			ratingRepository.save(newRating);
			return modelMapper.map(newRating, RatingResponseDTO.class);
	    }
	}

	@Override
	public RatingResponseDTO getRatingById(Long ratingId) {
		Rating rating = ratingRepository.findById(ratingId)
				.orElseThrow(() -> new EntityNotFoundException("Rating not found with id: " + ratingId));
		return modelMapper.map(rating, RatingResponseDTO.class);
	}

	@Override
	public List<RatingResponseDTO> getAllRatingsByBookId(Long bookId) {
		Book book = bookRepository.findById(bookId)
				.orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + bookId));
		List<Rating> ratings = book.getRatings();
		return ratings.stream().map(rating -> modelMapper.map(rating, RatingResponseDTO.class)).toList();
	}

	@Override
	public List<RatingResponseDTO> getAllRatingsByChapterId(Long chapterId) {
		Chapter chapter = chapterRepository.findById(chapterId)
				.orElseThrow(() -> new EntityNotFoundException("Chapter not found with id: " + chapterId));
		List<Rating> ratings = chapter.getRatings();
		return ratings.stream().map(rating -> modelMapper.map(rating, RatingResponseDTO.class)).toList();
	}

	@Override
	public RatingResponseDTO updateRating(RatingUpdateRequestDTO rating) {
		Rating updatedRating = ratingRepository.findById(rating.getId())
				.orElseThrow(() -> new EntityNotFoundException("Rating not found with id: " + rating.getId()));
		updatedRating.setScore(rating.getScore());
		return modelMapper.map(updatedRating, RatingResponseDTO.class);
	}

	@Override
	public void deleteRatingById(Long ratingId) {
		ratingRepository.deleteById(ratingId);
	}

}
