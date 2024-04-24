package com.example.demoproject.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demoproject.config.JwtService;
import com.example.demoproject.dao.UserRepository;
import com.example.demoproject.model.Role;
import com.example.demoproject.model.User;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AuthenticationService {
	
	private final UserRepository userRepository;	
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;

	public AuthenticationResponse register(RegisterRequest request) {
		
		User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
		userRepository.save(user);
		
		var jwtToken = jwtService.generateToken(user);
		
		return AuthenticationResponse.builder()
									.token(jwtToken)
									.build();
	}

	public AuthenticationResponse login(AuthenticationRequest request) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						request.getUsername(), 
						request.getPassword())
				);
		var user = userRepository.findByUsername(request.getUsername()).orElseThrow();
		var jwtToken = jwtService.generateToken(user);
		
		return AuthenticationResponse.builder()
									.token(jwtToken)
									.build();
	}
	
	@PostConstruct
	public void initAdmin() {
        if (userRepository.existsByRole(Role.ADMIN)) {
            throw new IllegalStateException("Admin user already exists.");
        }

        User adminUser = User.builder()
                .username("admin")
                .email("admin@example.com")
                .password(passwordEncoder.encode("admin"))
                .role(Role.ADMIN)
                .build();

        userRepository.save(adminUser);
    }
	
	public AuthenticationResponse registerAdmin(RegisterRequest request) {
        User adminUser = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ADMIN)
                .build();

        userRepository.save(adminUser);

        String jwtToken = jwtService.generateToken(adminUser);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
