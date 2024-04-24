package com.example.demoproject.service;

import java.util.List;

import com.example.demoproject.DTO.requests.BookRequestDTO;
import com.example.demoproject.DTO.requests.BookUpdateRequestDTO;
import com.example.demoproject.DTO.responses.BookResponseDTO;
import com.example.demoproject.model.Book;

public interface BookService {
	BookResponseDTO createBook(Long userId, BookRequestDTO bookRequestDTO);
    
    BookResponseDTO getBookById(Long bookId);
    
    List<BookResponseDTO> getAllBooks();
    
    BookResponseDTO updateBook(BookUpdateRequestDTO bookRequestDTO);
    
    void deleteBookById(Long bookId);
    
    void addToFavorites(Long userId, Long bookId);
    void removeFromFavorites(Long userId, Long bookId);
    
    List<BookResponseDTO> recommendBooksByGenre(Long userId);
}
