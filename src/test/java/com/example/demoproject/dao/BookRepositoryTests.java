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
import com.example.demoproject.model.Role;
import com.example.demoproject.model.User;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class BookRepositoryTests {

    @Autowired
    private BookRepository bookRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Test
    public void bookRepository_Save() {
        User user = new User(null, "ivan", "ivan@gmail.com", "12345", "", "",
                LocalDateTime.now(), null, null, null, null, Role.USER);
        userRepository.save(user);

        Book book = new Book();
        book.setTitle("Test Book");
        book.setDescription("Test Description");
        book.setGenre("Fiction");
        book.setCoverImage("cover.jpg");
        book.setUser(user);

        Book savedBook = bookRepository.save(book);

        assertNotNull(savedBook);
        assertNotNull(savedBook.getBookId());
        assertEquals("Test Book", savedBook.getTitle());
        assertEquals("Test Description", savedBook.getDescription());
    }

    @Test
    public void bookRepository_FindAll() {
    	User user = new User(null, "ivan", "ivan@gmail.com", "12345", "", "",
                LocalDateTime.now(), null, null, null, null, Role.USER);
        userRepository.save(user);

        Book book1 = new Book();
        book1.setTitle("Test Book 1");
        book1.setDescription("Test Description 1");
        book1.setGenre("Fiction");
        book1.setCoverImage("cover1.jpg");
        book1.setUser(user);

        Book book2 = new Book();
        book2.setTitle("Test Book 2");
        book2.setDescription("Test Description 2");
        book2.setGenre("Non-Fiction");
        book2.setCoverImage("cover2.jpg");
        book2.setUser(user);

        bookRepository.save(book1);
        bookRepository.save(book2);

        List<Book> books = bookRepository.findAll();

        assertNotNull(books);
        assertEquals(2, books.size());
    }

    @Test
    public void bookRepository_FindById() {
    	User user = new User(null, "ivan", "ivan@gmail.com", "12345", "", "",
                LocalDateTime.now(), null, null, null, null, Role.USER);
        userRepository.save(user);

        Book book = new Book();
        book.setTitle("Test Book");
        book.setDescription("Test Description");
        book.setGenre("Fiction");
        book.setCoverImage("cover.jpg");
        book.setUser(user);

        Book savedBook = bookRepository.save(book);

        Optional<Book> foundBook = bookRepository.findById(savedBook.getBookId());

        assertTrue(foundBook.isPresent());
        assertEquals(savedBook.getBookId(), foundBook.get().getBookId());
    }

    @Test
    public void bookRepository_DeleteById() {
    	User user = new User(null, "ivan", "ivan@gmail.com", "12345", "", "",
                LocalDateTime.now(), null, null, null, null, Role.USER);
        userRepository.save(user);

        Book book = new Book();
        book.setTitle("Test Book");
        book.setDescription("Test Description");
        book.setGenre("Fiction");
        book.setCoverImage("cover.jpg");
        book.setUser(user);

        Book savedBook = bookRepository.save(book);

        bookRepository.deleteById(savedBook.getBookId());

        Optional<Book> deletedBook = bookRepository.findById(savedBook.getBookId());

        assertFalse(deletedBook.isPresent());
    }
}

//private Long bookId;
//private String title;
//private String description;
//private String genre;
//private String coverImage;
//private LocalDateTime creationDate;
//private LocalDateTime updateDate;
//private User user;
//private List<Chapter> chapters;
//private List<Rating> ratings;
//private List<Comment> comments;
