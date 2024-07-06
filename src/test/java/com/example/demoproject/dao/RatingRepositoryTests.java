package com.example.demoproject.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.demoproject.model.Rating;
import com.example.demoproject.model.Chapter;
import com.example.demoproject.model.Book;
import com.example.demoproject.model.Role;
import com.example.demoproject.model.User;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class RatingRepositoryTests {

    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private ChapterRepository chapterRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void ratingRepository_SaveForBook() {
        User user = new User(null, "ivan", "ivan@gmail.com", "12345", "", "",
                LocalDateTime.now(), null, null, null, null, Role.USER);
        userRepository.save(user);

        Book book = new Book();
        book.setTitle("Test Book");
        book.setDescription("Test Description");
        book.setGenre("Fiction");
        book.setCoverImage("cover.jpg");
        book.setUser(user);
        bookRepository.save(book);

        Rating rating = new Rating();
        rating.setScore(5);
        rating.setUser(user);
        rating.setBook(book);

        Rating savedRating = ratingRepository.save(rating);

        assertNotNull(savedRating);
        assertNotNull(savedRating.getRatingId());
        assertEquals(5, savedRating.getScore());
        assertNotNull(savedRating.getUser());
        assertNotNull(savedRating.getBook());
        assertNull(savedRating.getChapter());
    }

    @Test
    public void ratingRepository_SaveForChapter() {
        User user = new User(null, "ivan", "ivan@gmail.com", "12345", "", "",
                LocalDateTime.now(), null, null, null, null, Role.USER);
        userRepository.save(user);

        Book book = new Book();
        book.setTitle("Test Book");
        book.setDescription("Test Description");
        book.setGenre("Fiction");
        book.setCoverImage("cover.jpg");
        book.setUser(user);
        bookRepository.save(book);

        Chapter chapter = new Chapter();
        chapter.setTitle("Test Chapter");
        chapter.setContent("Test chapter content.");
        chapter.setIsPublished(true);
        chapter.setBook(book);
        chapterRepository.save(chapter);

        Rating rating = new Rating();
        rating.setScore(4);
        rating.setUser(user);
        rating.setChapter(chapter);

        Rating savedRating = ratingRepository.save(rating);

        assertNotNull(savedRating);
        assertNotNull(savedRating.getRatingId());
        assertEquals(4, savedRating.getScore());
        assertNotNull(savedRating.getUser());
        assertNotNull(savedRating.getChapter());
        assertNull(savedRating.getBook());
    }

    @Test
    public void ratingRepository_FindAll() {
        User user = new User(null, "ivan", "ivan@gmail.com", "12345", "", "",
                LocalDateTime.now(), null, null, null, null, Role.USER);
        userRepository.save(user);

        Book book = new Book();
        book.setTitle("Test Book");
        book.setDescription("Test Description");
        book.setGenre("Fiction");
        book.setCoverImage("cover.jpg");
        book.setUser(user);
        bookRepository.save(book);

        Chapter chapter = new Chapter();
        chapter.setTitle("Test Chapter");
        chapter.setContent("Test chapter content.");
        chapter.setIsPublished(true);
        chapter.setBook(book);
        chapterRepository.save(chapter);

        Rating rating1 = new Rating();
        rating1.setScore(5);
        rating1.setUser(user);
        rating1.setBook(book);

        Rating rating2 = new Rating();
        rating2.setScore(4);
        rating2.setUser(user);
        rating2.setChapter(chapter);

        ratingRepository.save(rating1);
        ratingRepository.save(rating2);

        List<Rating> ratings = ratingRepository.findAll();

        assertNotNull(ratings);
        assertEquals(2, ratings.size());
    }

    @Test
    public void ratingRepository_FindById() {
        User user = new User(null, "ivan", "ivan@gmail.com", "12345", "", "",
                LocalDateTime.now(), null, null, null, null, Role.USER);
        userRepository.save(user);

        Book book = new Book();
        book.setTitle("Test Book");
        book.setDescription("Test Description");
        book.setGenre("Fiction");
        book.setCoverImage("cover.jpg");
        book.setUser(user);
        bookRepository.save(book);

        Chapter chapter = new Chapter();
        chapter.setTitle("Test Chapter");
        chapter.setContent("Test chapter content.");
        chapter.setIsPublished(true);
        chapter.setBook(book);
        chapterRepository.save(chapter);

        Rating rating = new Rating();
        rating.setScore(4);
        rating.setUser(user);
        rating.setChapter(chapter);
        Rating savedRating = ratingRepository.save(rating);

        Optional<Rating> foundRating = ratingRepository.findById(savedRating.getRatingId());

        assertTrue(foundRating.isPresent());
        assertEquals(savedRating.getRatingId(), foundRating.get().getRatingId());
    }

    @Test
    public void ratingRepository_DeleteById() {
        User user = new User(null, "ivan", "ivan@gmail.com", "12345", "", "",
                LocalDateTime.now(), null, null, null, null, Role.USER);
        userRepository.save(user);

        Book book = new Book();
        book.setTitle("Test Book");
        book.setDescription("Test Description");
        book.setGenre("Fiction");
        book.setCoverImage("cover.jpg");
        book.setUser(user);
        bookRepository.save(book);

        Chapter chapter = new Chapter();
        chapter.setTitle("Test Chapter");
        chapter.setContent("Test chapter content.");
        chapter.setIsPublished(true);
        chapter.setBook(book);
        chapterRepository.save(chapter);

        Rating rating = new Rating();
        rating.setScore(4);
        rating.setUser(user);
        rating.setChapter(chapter);
        Rating savedRating = ratingRepository.save(rating);

        ratingRepository.deleteById(savedRating.getRatingId());

        Optional<Rating> foundRating = ratingRepository.findById(savedRating.getRatingId());

        assertFalse(foundRating.isPresent());
    }
}
