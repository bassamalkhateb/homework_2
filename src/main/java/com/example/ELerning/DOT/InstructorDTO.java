package com.example.ELerning.DOT;

import com.example.ELerning.Entity.User;

import lombok.Data;

@Data
public class InstructorDTO {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private User.Role role;

    // Getters and Setters
    public static InstructorDTO fromEntity(User user) {
        InstructorDTO dto = new InstructorDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setRole(user.getRole());
        return dto;
    }
}
