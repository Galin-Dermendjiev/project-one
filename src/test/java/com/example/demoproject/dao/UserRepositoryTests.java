package com.example.demoproject.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
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

import com.example.demoproject.model.Role;
import com.example.demoproject.model.User;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRepositoryTests {
	
	@Autowired
	private UserRepository userRepository;
	
	@Test
	public void userRepository_Save() {
		User user = new User(null, "ivan", "ivan@gmail.com", "12345", "", "",
							LocalDateTime.now(), null, null, null, null, Role.USER);
		
		User savedUser = userRepository.save(user);
		
		assertNotEquals(savedUser, null);
		assertNotEquals(savedUser.getUserId(), null);
		assertEquals(savedUser.getUsername(), "ivan");
		assertEquals(savedUser.getEmail(), "ivan@gmail.com");
	}
	
	@Test
	public void userRepository_FindAll() {
		User user1 = new User(null, "ivan", "ivan@gmail.com", "12345", "", "",
				LocalDateTime.now(), null, null, null, null, Role.USER);
		User user2 = new User(null, "ivan2", "ivan2@gmail.com", "12345", "", "",
				LocalDateTime.now(), null, null, null, null, Role.USER);
		
		userRepository.save(user1);
		userRepository.save(user2);
		
		List<User> users = userRepository.findAll();
		
		assertNotNull(users);
		assertEquals(users.size(), 2);
	}
	
	@Test
    public void userRepository_FindById() {
        User user = new User(null, "ivan", "ivan@gmail.com", "12345", "", "",
                LocalDateTime.now(), null, null, null, null, Role.USER);

        User savedUser = userRepository.save(user);

        Optional<User> foundUser = userRepository.findById(savedUser.getUserId());

        assertTrue(foundUser.isPresent());
        assertEquals(savedUser.getUserId(), foundUser.get().getUserId());
    }
	
	@Test 
	public void userRepository_DeleteById() {
		User user = new User(null, "ivan", "ivan@gmail.com", "12345", "", "",
				LocalDateTime.now(), null, null, null, null, Role.USER);

		User savedUser = userRepository.save(user);
		
		userRepository.deleteById(savedUser.getUserId());
		Optional<User> deletedUser = userRepository.findById(savedUser.getUserId());
		
		assertFalse(deletedUser.isPresent());
	}
	
	@Test
    public void userRepository_FindByUsername() {
        User user = new User(null, "ivan", "ivan@gmail.com", "12345", "", "",
                LocalDateTime.now(), null, null, null, null, Role.USER);

        User savedUser = userRepository.save(user);

        Optional<User> foundUser = userRepository.findByUsername(savedUser.getUsername());

        assertTrue(foundUser.isPresent());
        assertEquals(savedUser.getUsername(), foundUser.get().getUsername());
    }
	
	@Test
    public void userRepository_ExistsByRole() {
        User user = new User(null, "ivan", "ivan@gmail.com", "12345", "", "",
                LocalDateTime.now(), null, null, null, null, Role.ADMIN);

        User savedUser = userRepository.save(user);

        Boolean foundUser = userRepository.existsByRole(savedUser.getRole());

        assertTrue(foundUser);
    }
}

//private Long userId;
//private String username;
//private String email;
//private String password;
//private String profilePicture;
//private String bio;
//private LocalDateTime registrationDate;
//private List<Book> books;
//private List<Comment> comments;
//private List<Rating> ratings;
//private List<Book> favoriteBooks;
//private Role role;

