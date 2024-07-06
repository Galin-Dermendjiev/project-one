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

import com.example.demoproject.model.Comment;
import com.example.demoproject.model.Chapter;
import com.example.demoproject.model.Book;
import com.example.demoproject.model.Role;
import com.example.demoproject.model.User;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CommentRepositoryTests {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ChapterRepository chapterRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void commentRepository_SaveForBook() {
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

        Comment comment = new Comment();
        comment.setContent("Comment Test content");
        comment.setUser(user);
        comment.setBook(book);

        Comment savedComment = commentRepository.save(comment);

        assertNotNull(savedComment);
        assertNotNull(savedComment.getCommentId());
        assertEquals("Comment Test content", savedComment.getContent());
        assertNotNull(savedComment.getUser());
        assertNotNull(savedComment.getBook());
        assertNull(savedComment.getChapter());
    }

    @Test
    public void commentRepository_SaveForChapter() {
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

        Comment comment = new Comment();
        comment.setContent("Comment Test content");
        comment.setUser(user);
        comment.setChapter(chapter);

        Comment savedComment = commentRepository.save(comment);

        assertNotNull(savedComment);
        assertNotNull(savedComment.getCommentId());
        assertEquals("Comment Test content", savedComment.getContent());
        assertNotNull(savedComment.getUser());
        assertNotNull(savedComment.getChapter());
        assertNull(savedComment.getBook());
    }

    @Test
    public void commentRepository_FindAll() {
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

        Comment comment1 = new Comment();
        comment1.setContent("Comment test content 1.");
        comment1.setUser(user);
        comment1.setBook(book);

        Comment comment2 = new Comment();
        comment2.setContent("Comment test content 2.");
        comment2.setUser(user);
        comment2.setChapter(chapter);

        commentRepository.save(comment1);
        commentRepository.save(comment2);

        List<Comment> comments = commentRepository.findAll();

        assertNotNull(comments);
        assertEquals(2, comments.size());
    }

    @Test
    public void commentRepository_FindById() {
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

        Comment comment = new Comment();
        comment.setContent("Comment Test content");
        comment.setUser(user);
        comment.setChapter(chapter);
        Comment savedComment = commentRepository.save(comment);

        Optional<Comment> foundComment = commentRepository.findById(savedComment.getCommentId());

        assertTrue(foundComment.isPresent());
        assertEquals(savedComment.getCommentId(), foundComment.get().getCommentId());
    }

    @Test
    public void commentRepository_DeleteById() {
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

        Comment comment = new Comment();
        comment.setContent("Comment Test content");
        comment.setUser(user);
        comment.setChapter(chapter);
        Comment savedComment = commentRepository.save(comment);

        commentRepository.deleteById(savedComment.getCommentId());

        Optional<Comment> foundComment = commentRepository.findById(savedComment.getCommentId());

        assertFalse(foundComment.isPresent());
    }
}
