package com.example.ELerning.DOT;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class QuizRequestDTO {
    @NotBlank(message = "عنوان الاختبار مطلوب")
    private String title;
    
    @NotBlank(message = "وصف الاختبار مطلوب")
    private String description;
    
    @NotNull(message = "معرف الدورة مطلوب")
    private Long courseId;
}
