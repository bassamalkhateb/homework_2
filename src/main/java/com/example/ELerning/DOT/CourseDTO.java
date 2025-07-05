package com.example.ELerning.DOT;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.example.ELerning.Entity.Course;

@Data
public class CourseDTO {
    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private Integer duration;
    private Course.Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private InstructorDTO instructor;

    public static CourseDTO fromEntity(Course course) {
        CourseDTO dto = new CourseDTO();
        dto.setId(course.getId());
        dto.setTitle(course.getTitle());
        dto.setDescription(course.getDescription());
        dto.setPrice(course.getPrice());
        dto.setDuration(course.getDuration());
        dto.setStatus(course.getStatus());
        dto.setCreatedAt(course.getCreatedAt());
        dto.setUpdatedAt(course.getUpdatedAt());
        
        if (course.getInstructor() != null) {
            dto.setInstructor(InstructorDTO.fromEntity(course.getInstructor()));
        }
        
        return dto;
    }
     public static List<CourseDTO> fromEntities(List<Course> courses) {
        return courses.stream()
                .map(CourseDTO::fromEntity)
                .collect(Collectors.toList());
    }
    
}