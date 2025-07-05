package com.example.ELerning.DOT; // تأكد من أن الحزمة صحيحة (DOT -> DTO)

import com.example.ELerning.Entity.User;
import lombok.Data;

@Data
public class StudentDTO {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private User.Role role;

    // Constructor for manual creation
    public StudentDTO(Long id, String username, String firstName, String lastName, User.Role role) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

    // Default constructor for JPA
    public StudentDTO() {}

    public static StudentDTO fromEntity(User user) {
        if (user == null) {
            return null;
        }
        return new StudentDTO(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole()
        );
    }
}