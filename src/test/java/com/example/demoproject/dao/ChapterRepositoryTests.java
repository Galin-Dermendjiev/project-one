package com.example.demoproject.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.demoproject.model.Book;
import com.example.demoproject.model.Chapter;
import com.example.demoproject.model.Role;
import com.example.demoproject.model.User;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ChapterRepositoryTests {

    @Autowired
    private ChapterRepository chapterRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void chapterRepository_Save() {
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

        Chapter savedChapter = chapterRepository.save(chapter);

        assertNotNull(savedChapter);
        assertNotNull(savedChapter.getChapterId());
        assertEquals("Test Chapter", savedChapter.getTitle());
        assertEquals("Test chapter content.", savedChapter.getContent());
        assertTrue(savedChapter.getIsPublished());
    }

    @Test
    public void chapterRepository_FindAll() {
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

        Chapter chapter1 = new Chapter();
        chapter1.setTitle("Test Chapter 1");
        chapter1.setContent("Test chapter content 1.");
        chapter1.setIsPublished(true);
        chapter1.setBook(book);

        Chapter chapter2 = new Chapter();
        chapter2.setTitle("Test Chapter 2");
        chapter2.setContent("Test chapter content 2.");
        chapter2.setIsPublished(false);
        chapter2.setBook(book);

        chapterRepository.save(chapter1);
        chapterRepository.save(chapter2);

        List<Chapter> chapters = chapterRepository.findAll();

        assertNotNull(chapters);
        assertEquals(2, chapters.size());
    }

    @Test
    public void chapterRepository_FindById() {
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

        Chapter savedChapter = chapterRepository.save(chapter);

        Optional<Chapter> foundChapter = chapterRepository.findById(savedChapter.getChapterId());

        assertTrue(foundChapter.isPresent());
        assertEquals(savedChapter.getChapterId(), foundChapter.get().getChapterId());
    }

    @Test
    public void chapterRepository_DeleteById() {
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

        Chapter savedChapter = chapterRepository.save(chapter);

        chapterRepository.deleteById(savedChapter.getChapterId());

        Optional<Chapter> deletedChapter = chapterRepository.findById(savedChapter.getChapterId());

        assertFalse(deletedChapter.isPresent());
    }
}

//private Long chapterId;
//private String title;
//private String content;
//private LocalDateTime creationDate;
//private LocalDateTime updateDate;
//private Boolean isPublished;
//private Book book;
//private List<Rating> ratings;
//private List<Comment> comments;