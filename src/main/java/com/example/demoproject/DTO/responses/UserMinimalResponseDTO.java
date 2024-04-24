package com.example.demoproject.DTO.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMinimalResponseDTO {
    private Long userId;
    private String username;
    private String email;
}
