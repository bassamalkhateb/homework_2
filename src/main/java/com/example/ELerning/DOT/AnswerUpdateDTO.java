package com.example.ELerning.DOT;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AnswerUpdateDTO {
    @NotBlank(message = "نص الإجابة مطلوب")
    private String answerText;
    
    @NotNull(message = "حالة الصواب مطلوبة")
    private Boolean isCorrect;
}
