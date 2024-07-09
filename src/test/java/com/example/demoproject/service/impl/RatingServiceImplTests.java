package com.example.demoproject.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

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

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
public class RatingServiceImplTests {

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private ChapterRepository chapterRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private RatingServiceImpl ratingService;

    private User user;
    private Book book;
    private Chapter chapter;
    private Rating rating;
    private RatingRequestDTO ratingRequestDTO;
    private RatingUpdateRequestDTO ratingUpdateRequestDTO;
    private RatingResponseDTO ratingResponseDTO;

    @BeforeEach
    public void setup() {
        user = new User();
        user.setUserId(1L);
        user.setUsername("testuser");

        book = new Book();
        book.setBookId(1L);
        book.setTitle("Test Book");

        chapter = new Chapter();
        chapter.setChapterId(1L);
        chapter.setTitle("Test Chapter");

        rating = new Rating();
        rating.setRatingId(1L);
        rating.setScore(5);
        rating.setUser(user);
        rating.setBook(book);

        ratingRequestDTO = new RatingRequestDTO();
        ratingRequestDTO.setScore(5);

        ratingUpdateRequestDTO = new RatingUpdateRequestDTO();
        ratingUpdateRequestDTO.setId(1L);
        ratingUpdateRequestDTO.setScore(4);

        ratingResponseDTO = new RatingResponseDTO();
        ratingResponseDTO.setRatingId(1L);
        ratingResponseDTO.setScore(5);
    }

    @Test
    public void createRatingForBook_NewRating_ReturnsRatingResponseDTO() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(ratingRepository.findByUser_UserIdAndBook_BookId(1L, 1L)).thenReturn(Optional.empty());
        when(ratingRepository.save(any(Rating.class))).thenReturn(rating);
        when(modelMapper.map(ratingRequestDTO, Rating.class)).thenReturn(rating);
        when(modelMapper.map(rating, RatingResponseDTO.class)).thenReturn(ratingResponseDTO);

        RatingResponseDTO result = ratingService.createRatingForBook(1L, 1L, ratingRequestDTO);

        assertNotNull(result);
        assertEquals(ratingResponseDTO, result);
    }

    @Test
    public void createRatingForBook_UpdateExistingRating_ReturnsRatingResponseDTO() {
        Rating existingRating = new Rating();
        existingRating.setRatingId(1L);
        existingRating.setScore(3);
        existingRating.setUser(user);
        existingRating.setBook(book);

        when(ratingRepository.findByUser_UserIdAndBook_BookId(1L, 1L)).thenReturn(Optional.of(existingRating));
        when(ratingRepository.save(existingRating)).thenReturn(existingRating);
        when(modelMapper.map(existingRating, RatingResponseDTO.class)).thenReturn(ratingResponseDTO);

        RatingRequestDTO updatedRatingRequestDTO = new RatingRequestDTO();
        updatedRatingRequestDTO.setScore(5);

        RatingResponseDTO result = ratingService.createRatingForBook(1L, 1L, updatedRatingRequestDTO);

        assertNotNull(result);
        assertEquals(ratingResponseDTO, result);
        verify(ratingRepository, times(1)).save(existingRating);
    }

    @Test
    public void createRatingForBook_UserDoesNotExist_ThrowsEntityNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> ratingService.createRatingForBook(1L, 1L, ratingRequestDTO));
    }

    @Test
    public void createRatingForBook_BookDoesNotExist_ThrowsEntityNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> ratingService.createRatingForBook(1L, 1L, ratingRequestDTO));
    }

    @Test
    public void createRatingForChapter_NewRating_ReturnsRatingResponseDTO() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(chapterRepository.findById(1L)).thenReturn(Optional.of(chapter));
        when(ratingRepository.findByUser_UserIdAndChapter_ChapterId(1L, 1L)).thenReturn(Optional.empty());
        when(ratingRepository.save(any(Rating.class))).thenReturn(rating);
        when(modelMapper.map(ratingRequestDTO, Rating.class)).thenReturn(rating);
        when(modelMapper.map(rating, RatingResponseDTO.class)).thenReturn(ratingResponseDTO);

        RatingResponseDTO result = ratingService.createRatingForChapter(1L, 1L, ratingRequestDTO);

        assertNotNull(result);
        assertEquals(ratingResponseDTO, result);
    }

    @Test
    public void createRatingForChapter_UpdateExistingRating_ReturnsRatingResponseDTO() {
        Rating existingRating = new Rating();
        existingRating.setRatingId(1L);
        existingRating.setScore(3);
        existingRating.setUser(user);
        existingRating.setChapter(chapter);

        when(ratingRepository.findByUser_UserIdAndChapter_ChapterId(1L, 1L)).thenReturn(Optional.of(existingRating));
        when(ratingRepository.save(existingRating)).thenReturn(existingRating);
        when(modelMapper.map(existingRating, RatingResponseDTO.class)).thenReturn(ratingResponseDTO);

        RatingRequestDTO updatedRatingRequestDTO = new RatingRequestDTO();
        updatedRatingRequestDTO.setScore(5);

        RatingResponseDTO result = ratingService.createRatingForChapter(1L, 1L, updatedRatingRequestDTO);

        assertNotNull(result);
        assertEquals(ratingResponseDTO, result);
        verify(ratingRepository, times(1)).save(existingRating);
    }

    @Test
    public void createRatingForChapter_UserDoesNotExist_ThrowsEntityNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> ratingService.createRatingForChapter(1L, 1L, ratingRequestDTO));
    }

    @Test
    public void createRatingForChapter_ChapterDoesNotExist_ThrowsEntityNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(chapterRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> ratingService.createRatingForChapter(1L, 1L, ratingRequestDTO));
    }

    @Test
    public void getRatingById_RatingExists_ReturnsRatingResponseDTO() {
        when(ratingRepository.findById(1L)).thenReturn(Optional.of(rating));
        when(modelMapper.map(rating, RatingResponseDTO.class)).thenReturn(ratingResponseDTO);

        RatingResponseDTO result = ratingService.getRatingById(1L);

        assertNotNull(result);
        assertEquals(ratingResponseDTO, result);
    }

    @Test
    public void getRatingById_RatingDoesNotExist_ThrowsEntityNotFoundException() {
        when(ratingRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> ratingService.getRatingById(1L));
    }

    @Test
    public void getAllRatingsByBookId_BookExists_ReturnsAllRatings() {
        List<Rating> ratingList = Stream.of(rating).toList();
        book.setRatings(ratingList);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(modelMapper.map(any(Rating.class), eq(RatingResponseDTO.class))).thenReturn(ratingResponseDTO);

        List<RatingResponseDTO> result = ratingService.getAllRatingsByBookId(1L);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(ratingList.size(), result.size());
        assertEquals(ratingResponseDTO, result.get(0));
    }

    @Test
    public void getAllRatingsByBookId_BookDoesNotExist_ThrowsEntityNotFoundException() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> ratingService.getAllRatingsByBookId(1L));
    }

    @Test
    public void getAllRatingsByChapterId_ChapterExists_ReturnsAllRatings() {
        List<Rating> ratingList = Stream.of(rating).toList();
        chapter.setRatings(ratingList);
        when(chapterRepository.findById(1L)).thenReturn(Optional.of(chapter));
        when(modelMapper.map(any(Rating.class), eq(RatingResponseDTO.class))).thenReturn(ratingResponseDTO);

        List<RatingResponseDTO> result = ratingService.getAllRatingsByChapterId(1L);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(ratingList.size(), result.size());
        assertEquals(ratingResponseDTO, result.get(0));
    }

    @Test
    public void getAllRatingsByChapterId_ChapterDoesNotExist_ThrowsEntityNotFoundException() {
        when(chapterRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> ratingService.getAllRatingsByChapterId(1L));
    }

    @Test
    public void updateRating_RatingExists_ReturnsUpdatedRatingResponseDTO() {
        when(ratingRepository.findById(1L)).thenReturn(Optional.of(rating));
        when(ratingRepository.save(rating)).thenReturn(rating);
        when(modelMapper.map(rating, RatingResponseDTO.class)).thenReturn(ratingResponseDTO);

        RatingResponseDTO result = ratingService.updateRating(ratingUpdateRequestDTO);
        
        assertNotNull(result);
        assertEquals(ratingResponseDTO, result);
        verify(ratingRepository, times(1)).save(rating);
    }

    @Test
    public void updateRating_RatingDoesNotExist_ThrowsEntityNotFoundException() {
        when(ratingRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> ratingService.updateRating(ratingUpdateRequestDTO));
    }

    @Test
    public void deleteRatingById_RatingExists_DeletesRating() {
        when(ratingRepository.findById(1L)).thenReturn(Optional.of(rating));
        doNothing().when(ratingRepository).deleteById(1L);

        ratingService.deleteRatingById(1L);

        verify(ratingRepository, times(1)).deleteById(1L);
    }

    @Test
    public void deleteRatingById_RatingDoesNotExist_ThrowsEntityNotFoundException() {
        when(ratingRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> ratingService.deleteRatingById(1L));
    }
}
