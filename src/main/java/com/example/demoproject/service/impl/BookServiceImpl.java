package com.example.demoproject.service.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.example.demoproject.DTO.requests.BookRequestDTO;
import com.example.demoproject.DTO.requests.BookUpdateRequestDTO;
import com.example.demoproject.DTO.responses.BookResponseDTO;
import com.example.demoproject.dao.BookRepository;
import com.example.demoproject.dao.UserRepository;
import com.example.demoproject.model.Book;
import com.example.demoproject.model.User;
import com.example.demoproject.service.BookService;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class BookServiceImpl implements BookService{
	
	private final BookRepository bookRepository;
	private final UserRepository userRepository;
	private final ModelMapper modelMapper;

	@Override
	public BookResponseDTO createBook(Long userId, BookRequestDTO bookRequestDTO) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
		
		Book newBook = modelMapper.map(bookRequestDTO, Book.class);
		newBook.setUser(user);
		
		bookRepository.save(newBook);
		return modelMapper.map(newBook, BookResponseDTO.class);
	}

	@Override
	public BookResponseDTO getBookById(Long bookId) {
		Book book = bookRepository.findById(bookId)
				.orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + bookId));
		return modelMapper.map(book, BookResponseDTO.class);
	}

	@Override
	public List<BookResponseDTO> getAllBooks() {
		List<Book> books = bookRepository.findAll();
		return books.stream().map(book -> modelMapper.map(book, BookResponseDTO.class)).toList();
	}

	@Override
	public BookResponseDTO updateBook(BookUpdateRequestDTO book) {
		Book updatedBook = bookRepository.findById(book.getId())
				.orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + book.getId()));
		
		updatedBook.setTitle(book.getTitle());
		updatedBook.setDescription(book.getDescription());
		updatedBook.setGenre(book.getGenre());
		updatedBook.setCoverImage(book.getCoverImage());
		
		bookRepository.save(updatedBook);
		return modelMapper.map(updatedBook, BookResponseDTO.class);
	}

	@Override
	public void deleteBookById(Long bookId) {
		bookRepository.deleteById(bookId);
	}

	@Override
	public void addToFavorites(Long userId, Long bookId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
		Book book = bookRepository.findById(bookId)
				.orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + bookId));
		
		if(!user.getFavoriteBooks().contains(book)) {
			user.getFavoriteBooks().add(book);
			userRepository.save(user);
		}
	}

	@Override
	public void removeFromFavorites(Long userId, Long bookId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
		Book book = bookRepository.findById(bookId)
				.orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + bookId));
		
		if(user.getFavoriteBooks().contains(book)) {
			user.getFavoriteBooks().remove(book);
			userRepository.save(user);
		}
		
	}
	
	public List<Book> recommendBooksByGenre(Long userId) {
	    User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
	    Set<String> favoriteGenres = user.getFavoriteBooks().stream()
                .map(Book::getGenre)
                .collect(Collectors.toSet());

	    List<Book> recommendedBooks = bookRepository.findAll().stream()
	        .filter(book -> favoriteGenres.contains(book.getGenre()) && !user.getFavoriteBooks().contains(book))
	        .limit(20).toList();

	    return recommendedBooks;
	}


}
