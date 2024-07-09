package com.example.demoproject.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
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

import com.example.demoproject.DTO.requests.CommentRequestDTO;
import com.example.demoproject.DTO.requests.CommentUpdateRequestDTO;
import com.example.demoproject.DTO.responses.CommentResponseDTO;
import com.example.demoproject.dao.BookRepository;
import com.example.demoproject.dao.ChapterRepository;
import com.example.demoproject.dao.CommentRepository;
import com.example.demoproject.dao.UserRepository;
import com.example.demoproject.model.Book;
import com.example.demoproject.model.Chapter;
import com.example.demoproject.model.Comment;
import com.example.demoproject.model.User;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
public class CommentServiceImplTests {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private ChapterRepository chapterRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CommentServiceImpl commentService;

    private User user;
    private Book book;
    private Chapter chapter;
    private Comment comment;
    private CommentRequestDTO commentRequestDTO;
    private CommentUpdateRequestDTO commentUpdateRequestDTO;
    private CommentResponseDTO commentResponseDTO;

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

        comment = new Comment();
        comment.setCommentId(1L);
        comment.setContent("Test Comment");
        comment.setCreationDate(LocalDateTime.now());
        comment.setUpdateDate(LocalDateTime.now());
        comment.setUser(user);
        comment.setBook(book);

        commentRequestDTO = new CommentRequestDTO();
        commentRequestDTO.setContent("Test Comment");

        commentUpdateRequestDTO = new CommentUpdateRequestDTO();
        commentUpdateRequestDTO.setId(1L);
        commentUpdateRequestDTO.setContent("Updated Comment");

        commentResponseDTO = new CommentResponseDTO();
        commentResponseDTO.setCommentId(1L);
        commentResponseDTO.setContent("Test Comment");
    }

    @Test
    public void createCommentForBook_UserAndBookExist_ReturnsCommentResponseDTO() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        when(modelMapper.map(commentRequestDTO, Comment.class)).thenReturn(comment);
        when(modelMapper.map(comment, CommentResponseDTO.class)).thenReturn(commentResponseDTO);

        CommentResponseDTO result = commentService.createCommentForBook(1L, 1L, commentRequestDTO);

        assertNotNull(result);
        assertEquals(commentResponseDTO, result);
    }

    @Test
    public void createCommentForBook_UserDoesNotExist_ThrowsEntityNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> commentService.createCommentForBook(1L, 1L, commentRequestDTO));
    }

    @Test
    public void createCommentForBook_BookDoesNotExist_ThrowsEntityNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> commentService.createCommentForBook(1L, 1L, commentRequestDTO));
    }

    @Test
    public void createCommentForChapter_UserAndChapterExist_ReturnsCommentResponseDTO() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(chapterRepository.findById(1L)).thenReturn(Optional.of(chapter));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        when(modelMapper.map(commentRequestDTO, Comment.class)).thenReturn(comment);
        when(modelMapper.map(comment, CommentResponseDTO.class)).thenReturn(commentResponseDTO);

        CommentResponseDTO result = commentService.createCommentForChapter(1L, 1L, commentRequestDTO);

        assertNotNull(result);
        assertEquals(commentResponseDTO, result);
    }

    @Test
    public void createCommentForChapter_UserDoesNotExist_ThrowsEntityNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> commentService.createCommentForChapter(1L, 1L, commentRequestDTO));
    }

    @Test
    public void createCommentForChapter_ChapterDoesNotExist_ThrowsEntityNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(chapterRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> commentService.createCommentForChapter(1L, 1L, commentRequestDTO));
    }

    @Test
    public void getCommentById_CommentExists_ReturnsCommentResponseDTO() {
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        when(modelMapper.map(comment, CommentResponseDTO.class)).thenReturn(commentResponseDTO);

        CommentResponseDTO result = commentService.getCommentById(1L);

        assertNotNull(result);
        assertEquals(commentResponseDTO, result);
    }

    @Test
    public void getCommentById_CommentDoesNotExist_ThrowsEntityNotFoundException() {
        when(commentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> commentService.getCommentById(1L));
    }

    @Test
    public void getAllCommentsByBookId_BookExists_ReturnsAllComments() {
        List<Comment> commentList = Stream.of(comment).toList();
        book.setComments(commentList);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(modelMapper.map(any(Comment.class), eq(CommentResponseDTO.class))).thenReturn(commentResponseDTO);

        List<CommentResponseDTO> result = commentService.getAllCommentsByBookId(1L);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(commentList.size(), result.size());
        assertEquals(commentResponseDTO, result.get(0));
    }

    @Test
    public void getAllCommentsByBookId_BookDoesNotExist_ThrowsEntityNotFoundException() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> commentService.getAllCommentsByBookId(1L));
    }

    @Test
    public void getAllCommentsByChapterId_ChapterExists_ReturnsAllComments() {
        List<Comment> commentList = Stream.of(comment).toList();
        chapter.setComments(commentList);
        when(chapterRepository.findById(1L)).thenReturn(Optional.of(chapter));
        when(modelMapper.map(any(Comment.class), eq(CommentResponseDTO.class))).thenReturn(commentResponseDTO);

        List<CommentResponseDTO> result = commentService.getAllCommentsByChapterId(1L);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(commentList.size(), result.size());
        assertEquals(commentResponseDTO, result.get(0));
    }

    @Test
    public void getAllCommentsByChapterId_ChapterDoesNotExist_ThrowsEntityNotFoundException() {
        when(chapterRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> commentService.getAllCommentsByChapterId(1L));
    }

    @Test
    public void updateComment_CommentExists_ReturnsUpdatedCommentResponseDTO() {
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        when(commentRepository.save(comment)).thenReturn(comment);
        when(modelMapper.map(comment, CommentResponseDTO.class)).thenReturn(commentResponseDTO);

        CommentResponseDTO result = commentService.updateComment(commentUpdateRequestDTO);

        assertNotNull(result);
        assertEquals(commentResponseDTO, result);
    }

    @Test
    public void updateComment_CommentDoesNotExist_ThrowsEntityNotFoundException() {
        when(commentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> commentService.updateComment(commentUpdateRequestDTO));
    }

    @Test
    public void deleteCommentById_CommentExists_DeletesComment() {
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        commentService.deleteCommentById(1L);

        verify(commentRepository, times(1)).deleteById(1L);
    }

    @Test
    public void deleteCommentById_CommentDoesNotExist_ThrowsEntityNotFoundException() {
        when(commentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> commentService.deleteCommentById(1L));
    }
}
