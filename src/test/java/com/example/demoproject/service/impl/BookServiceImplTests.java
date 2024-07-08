package com.example.demoproject.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
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

import com.example.demoproject.DTO.requests.BookRequestDTO;
import com.example.demoproject.DTO.requests.BookUpdateRequestDTO;
import com.example.demoproject.DTO.responses.BookResponseDTO;
import com.example.demoproject.config.security.AuthorizationService;
import com.example.demoproject.dao.BookRepository;
import com.example.demoproject.dao.UserRepository;
import com.example.demoproject.model.Book;
import com.example.demoproject.model.Chapter;
import com.example.demoproject.model.Role;
import com.example.demoproject.model.User;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
public class BookServiceImplTests {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private AuthorizationService authorizationService;

    @InjectMocks
    private BookServiceImpl bookService;

    private User user;
    private Book book;
    private BookRequestDTO bookRequestDTO;
    private BookUpdateRequestDTO bookUpdateRequestDTO;
    private BookResponseDTO bookResponseDTO;

    @BeforeEach
    public void setup() {
    	user = new User();
        user.setUserId(1L);
        user.setUsername("ivan");
        user.setEmail("ivan@gmail.com");
        user.setProfilePicture("profile.jpg");
        user.setBio("Bio");
        user.setRole(Role.USER);
        user.setFavoriteBooks(new ArrayList());

        book = new Book();
        book.setBookId(1L);
        book.setTitle("Test Book");
        book.setDescription("Test Description");
        book.setGenre("Fiction");
        book.setCoverImage("cover.jpg");
        book.setUser(user);

        bookRequestDTO = new BookRequestDTO();
        bookRequestDTO.setTitle("Test Book");
        bookRequestDTO.setDescription("Test Description");
        bookRequestDTO.setGenre("Fiction");
        bookRequestDTO.setCoverImage("cover.jpg");

        bookUpdateRequestDTO = new BookUpdateRequestDTO();
        bookUpdateRequestDTO.setId(1L);
        bookUpdateRequestDTO.setTitle("Updated Book");
        bookUpdateRequestDTO.setDescription("Updated Description");
        bookUpdateRequestDTO.setGenre("Non-Fiction");
        bookUpdateRequestDTO.setCoverImage("updated_cover.jpg");

        bookResponseDTO = new BookResponseDTO();
        bookResponseDTO.setBookId(1L);
        bookResponseDTO.setTitle("Test Book");
        bookResponseDTO.setDescription("Test Description");
        bookResponseDTO.setGenre("Fiction");
        bookResponseDTO.setCoverImage("cover.jpg");
    }

    @Test
    public void createBook_UserExists_ReturnsBookResponseDTO() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(modelMapper.map(bookRequestDTO, Book.class)).thenReturn(book);
        when(modelMapper.map(book, BookResponseDTO.class)).thenReturn(bookResponseDTO);

        BookResponseDTO result = bookService.createBook(1L, bookRequestDTO);

        assertNotNull(result);
        assertEquals(bookResponseDTO, result);
    }

    @Test
    public void createBook_UserDoesNotExist_ThrowsEntityNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookService.createBook(1L, bookRequestDTO));
    }

    @Test
    public void getBookById_BookExists_ReturnsBookResponseDTO() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(modelMapper.map(book, BookResponseDTO.class)).thenReturn(bookResponseDTO);
        when(authorizationService.checkBookOwnership(1L)).thenReturn(true);

        BookResponseDTO result = bookService.getBookById(1L);

        assertNotNull(result);
        assertEquals(bookResponseDTO, result);
    }

    @Test
    public void getBookById_BookDoesNotExist_ThrowsEntityNotFoundException() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookService.getBookById(1L));
    }

    @Test
    public void getAllBooks_ReturnsListOfBookResponseDTO() {
        // Initialize chapters
        Chapter chapter1 = new Chapter();
        chapter1.setIsPublished(true);

        Chapter chapter2 = new Chapter();
        chapter2.setIsPublished(false);

        book.setChapters(List.of(chapter1, chapter2));

        // Initialize book list and response DTO
        List<Book> bookList = List.of(book);
        List<BookResponseDTO> bookResponseDTOList = List.of(bookResponseDTO);

        // Mock repository and services
        when(bookRepository.findAll()).thenReturn(bookList);
        when(modelMapper.map(any(Book.class), eq(BookResponseDTO.class))).thenReturn(bookResponseDTO);
        when(authorizationService.getUserIdFromUsername()).thenReturn(1L); // return current user id
        when(authorizationService.isCurrentAdmin()).thenReturn(false); // mock admin check

        List<BookResponseDTO> result = bookService.getAllBooks();

        assertNotNull(result);
        assertEquals(bookResponseDTOList.size(), result.size());
        assertEquals(bookResponseDTOList, result);

        verify(bookRepository).findAll();
        verify(modelMapper, times(bookList.size())).map(any(Book.class), eq(BookResponseDTO.class));
        verify(authorizationService).getUserIdFromUsername();
        verify(authorizationService).isCurrentAdmin();
    }

    @Test
    public void filterChapters_FiltersCorrectly() {
        Chapter publishedChapter = new Chapter();
        publishedChapter.setIsPublished(true);
        
        Chapter unpublishedChapter = new Chapter();
        unpublishedChapter.setIsPublished(false);

        book.setChapters(List.of(publishedChapter, unpublishedChapter));

        Book filteredBook = bookService.filterChapters(book, 1L, true);

        assertNotNull(filteredBook);
        assertNotNull(filteredBook.getChapters());
        assertEquals(2, filteredBook.getChapters().size()); // Both chapters should be present since the user is the author/admin
    }


    @Test
    public void updateBook_BookExists_ReturnsUpdatedBookResponseDTO() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.save(book)).thenReturn(book);
        when(modelMapper.map(book, BookResponseDTO.class)).thenReturn(bookResponseDTO);

        BookResponseDTO result = bookService.updateBook(bookUpdateRequestDTO);

        assertNotNull(result);
        assertEquals(bookResponseDTO, result);
    }

    @Test
    public void updateBook_BookDoesNotExist_ThrowsEntityNotFoundException() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookService.updateBook(bookUpdateRequestDTO));
    }

    @Test
    public void deleteBookById_BookExists_DeletesBook() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        bookService.deleteBookById(1L);

        verify(bookRepository, times(1)).deleteById(1L);
    }

    @Test
    public void addToFavorites_UserAndBookExist_AddsBookToFavorites() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        
        bookService.addToFavorites(1L, 1L);

        verify(userRepository, times(1)).save(user);
        assertTrue(user.getFavoriteBooks().contains(book));
    }

    @Test
    public void removeFromFavorites_UserAndBookExist_RemovesBookFromFavorites() {
        user.getFavoriteBooks().add(book);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        bookService.removeFromFavorites(1L, 1L);

        verify(userRepository, times(1)).save(user);
        assertFalse(user.getFavoriteBooks().contains(book));
    }

    @Test
    public void recommendBooksByGenre_UserExists_ReturnsRecommendedBooks() {
    	Book secondBook = new Book();
    	secondBook.setBookId(2L);
    	secondBook.setGenre("Fiction");
    	
    	BookResponseDTO secondBookResponseDTO = new BookResponseDTO();
        secondBookResponseDTO.setBookId(2L);
        secondBookResponseDTO.setGenre("Fiction");
    	
        user.getFavoriteBooks().add(book);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findAll()).thenReturn(Stream.of(book, secondBook).toList());
        when(modelMapper.map(any(Book.class), eq(BookResponseDTO.class))).thenReturn(bookResponseDTO);

        List<BookResponseDTO> result = bookService.recommendBooksByGenre(1L);
        System.out.println(result);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    public void recommendBooksByGenre_UserDoesNotExist_ThrowsEntityNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookService.recommendBooksByGenre(1L));
    }
}
