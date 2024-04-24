package com.example.demoproject.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demoproject.DTO.requests.BookRequestDTO;
import com.example.demoproject.DTO.requests.BookUpdateRequestDTO;
import com.example.demoproject.DTO.responses.BookResponseDTO;
import com.example.demoproject.config.security.AuthorizationService;
import com.example.demoproject.service.BookService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/books")
@AllArgsConstructor
public class BookController {

	private final BookService bookService;
	private final AuthorizationService authorizationService;
	
	
	@GetMapping
	public ResponseEntity<List<BookResponseDTO>> getAllBooks() {
		List<BookResponseDTO> books = bookService.getAllBooks();
		return ResponseEntity.ok(books);
	}
	
	@GetMapping("/{bookId}")
	public ResponseEntity<BookResponseDTO> getBookById(@PathVariable Long bookId) {
		BookResponseDTO bookResponseDTO = bookService.getBookById(bookId);
		return ResponseEntity.ok(bookResponseDTO);
	}
	
	@PostMapping
    public ResponseEntity<BookResponseDTO> createBook(@RequestBody BookRequestDTO bookRequestDTO) {
		Long userId = authorizationService.getUserIdFromUsername();

		BookResponseDTO createdBook = bookService.createBook(userId, bookRequestDTO);
        return ResponseEntity.ok(createdBook);
    }

    @PutMapping
    @PreAuthorize("@authorizationService.checkBookOwnership(#bookUpdateRequestDTO.userId)")
    public ResponseEntity<BookResponseDTO> updateBook(@RequestBody BookUpdateRequestDTO bookUpdateRequestDTO) {
        BookResponseDTO updatedBook = bookService.updateBook(bookUpdateRequestDTO);
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/{bookId}")
    @PreAuthorize("@authorizationService.checkBookOwnership(#bookId)")
    public ResponseEntity<Void> deleteBookById(@PathVariable Long bookId) {
        bookService.deleteBookById(bookId);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/favorites/{bookId}")
    public ResponseEntity<Void> addBookToFavorites(@PathVariable Long bookId){
    	Long userId = authorizationService.getUserIdFromUsername();
    	bookService.addToFavorites(userId, bookId);
    	return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/favorites/{bookId}")
    public ResponseEntity<Void> removeBookFromFavorites(@PathVariable Long bookId){
    	Long userId = authorizationService.getUserIdFromUsername();
    	bookService.removeFromFavorites(userId, bookId);
    	return ResponseEntity.noContent().build();
    }

}
