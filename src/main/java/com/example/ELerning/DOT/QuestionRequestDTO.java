package com.example.ELerning.DOT;
import com.example.ELerning.Entity.Question;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class QuestionRequestDTO {
    @NotBlank(message = "نص السؤال مطلوب")
    private String questionText;

    @NotNull(message = "نوع السؤال مطلوب")
    private Question.Type questionType;

    @NotNull(message = "معرف الاختبار مطلوب")
    private Long quizId;

    @Min(value = 1, message = "يجب أن تكون النقاط موجبة")
    private Integer points = 1;
}