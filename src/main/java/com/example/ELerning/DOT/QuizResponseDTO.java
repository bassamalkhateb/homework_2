package com.example.ELerning.DOT;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QuizResponseDTO {
    private Long id;
    private String title;
    private String description;
    private boolean isActive;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    private Long courseId;
    private String courseTitle;
}
