package com.example.demoproject.DTO.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDTO {
	private long id;
	private String username;
    private String email;
    private String profilePicture;
    private String bio;

}
