package com.example.demoproject.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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
import com.example.demoproject.service.CommentService;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

	private final CommentRepository commentRepository;
	private final UserRepository userRepository;
	private final BookRepository bookRepository;
	private final ChapterRepository chapterRepository;
	private final ModelMapper modelMapper;
	
	@Override
	public CommentResponseDTO createCommentForBook(Long userId, Long bookId, CommentRequestDTO commentRequestDTO) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
		Book book = bookRepository.findById(bookId)
				.orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + bookId));
		
		Comment newComment = modelMapper.map(commentRequestDTO, Comment.class);
		newComment.setUser(user);
		newComment.setBook(book);
		
		commentRepository.save(newComment);
		return modelMapper.map(newComment, CommentResponseDTO.class);
	}

	@Override
	public CommentResponseDTO createCommentForChapter(Long userId, Long chapterId,
			CommentRequestDTO commentRequestDTO) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
		Chapter chapter = chapterRepository.findById(chapterId)
				.orElseThrow(() -> new EntityNotFoundException("Chapter not found with id: " + chapterId));
		
		Comment newComment = modelMapper.map(commentRequestDTO, Comment.class);
		newComment.setUser(user);
		newComment.setChapter(chapter);
		
		commentRepository.save(newComment);
		return modelMapper.map(newComment, CommentResponseDTO.class);
	}

	@Override
	public CommentResponseDTO getCommentById(Long commentId) {
		Comment comment = commentRepository.findById(commentId)
				.orElseThrow(() -> new EntityNotFoundException("Comment not found with id: " + commentId));
		return modelMapper.map(comment, CommentResponseDTO.class);
	}

	@Override
	public List<CommentResponseDTO> getAllCommentsByBookId(Long bookId) {
		Book book = bookRepository.findById(bookId)
				.orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + bookId));
		List<Comment> comments = book.getComments();
		return comments.stream().map(comment -> modelMapper.map(comment, CommentResponseDTO.class)).toList();
	}

	@Override
	public List<CommentResponseDTO> getAllCommentsByChapterId(Long chapterId) {
		Chapter chapter = chapterRepository.findById(chapterId)
				.orElseThrow(() -> new EntityNotFoundException("Chapter not found with id: " + chapterId));
		List<Comment> comments = chapter.getComments();
		return comments.stream().map(comment -> modelMapper.map(comment, CommentResponseDTO.class)).toList();
	}

	@Override
	public CommentResponseDTO updateComment(CommentUpdateRequestDTO comment) {
		Comment updatedComment = commentRepository.findById(comment.getId())
				.orElseThrow(() -> new EntityNotFoundException("Comment not found with id: " + comment.getId()));
		
		updatedComment.setContent(comment.getContent());
		commentRepository.save(updatedComment);
		return modelMapper.map(updatedComment, CommentResponseDTO.class);
	}

	@Override
	public void deleteCommentById(Long commentId) {
		commentRepository.deleteById(commentId);
	}

}
