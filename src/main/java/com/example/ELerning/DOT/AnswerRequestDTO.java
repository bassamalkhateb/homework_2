package com.example.ELerning.DOT;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AnswerRequestDTO {
    @NotBlank(message = "نص الإجابة مطلوب")
    private String answerText;
    
    @NotNull(message = "حالة الصواب مطلوبة")
    private Boolean isCorrect;
    
    @NotNull(message = "معرف السؤال مطلوب")
    private Long questionId;
}
