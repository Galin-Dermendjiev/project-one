package com.example.demoproject.config.security;

import java.util.Optional;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demoproject.dao.BookRepository;
import com.example.demoproject.dao.ChapterRepository;
import com.example.demoproject.dao.CommentRepository;
import com.example.demoproject.dao.RatingRepository;
import com.example.demoproject.dao.UserRepository;
import com.example.demoproject.model.Book;
import com.example.demoproject.model.Chapter;
import com.example.demoproject.model.Comment;
import com.example.demoproject.model.Rating;
import com.example.demoproject.model.Role;
import com.example.demoproject.model.User;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthorizationService {
	
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final ChapterRepository chapterRepository;
    private final CommentRepository commentRepository;
    private final RatingRepository ratingRepository;

    public boolean checkBookOwnership(Long bookId) {
        Long userId = getUserIdFromUsername();
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));
        return userId.equals(book.getUser().getUserId()) || isAdmin(userId);
    }
    
    public boolean checkUserOwnership(Long userId) {
        Long currentUserId = getUserIdFromUsername();
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return user.getUserId().equals(currentUserId) || isAdmin(currentUserId);
        }
        return false; // User not found
    }

    public boolean checkChapterOwnership(Long chapterId) {
        Optional<Chapter> optionalChapter = chapterRepository.findById(chapterId);
        if (optionalChapter.isPresent()) {
            Chapter chapter = optionalChapter.get();
            return checkBookOwnership(chapter.getBook().getBookId());
        }
        return false; // Chapter not found
    }
    
    public boolean checkCommentOwnership(Long commentId) {
        Long currentUserId = getUserIdFromUsername();
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            return comment.getUser().getUserId().equals(currentUserId) || isAdmin(currentUserId);
        }
        return false; // Comment not found
    }
    
    public boolean checkRatingOwnership(Long ratingId) {
        Long currentUserId = getUserIdFromUsername();
        Optional<Rating> optionalRating = ratingRepository.findById(ratingId);
        if (optionalRating.isPresent()) {
            Rating rating = optionalRating.get();
            return rating.getUser().getUserId().equals(currentUserId) || isAdmin(currentUserId);
        }
        return false; // Rating not found
    }
    
    public Long getUserIdFromUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUsername = authentication.getName();
            return userRepository.findByUsername(currentUsername)
                    .map(User::getUserId)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + currentUsername));
        }
        throw new IllegalStateException("Current user is not authenticated");
    }

    public boolean isAdmin(Long userId) {
    	return userRepository.findById(userId)
    				.map(user -> user.getRole() == Role.ADMIN)
    				.orElse(false);                 
    }
    
    public boolean isCurrentAdmin() {
    	Long userId = getUserIdFromUsername();
    	return userRepository.findById(userId)
    				.map(user -> user.getRole() == Role.ADMIN)
    				.orElse(false);                 
    }
}
