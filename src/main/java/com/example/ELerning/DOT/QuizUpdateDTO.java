package com.example.ELerning.DOT;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class QuizUpdateDTO {
    @NotBlank(message = "عنوان الاختبار مطلوب")
    private String title;
    
    @Size(max = 1000, message = "الوصف يجب ألا يتجاوز 1000 حرف")
    private String description;
    
    @NotNull(message = "الحد الزمني مطلوب")
    @Min(value = 1, message = "الحد الزمني يجب أن يكون على الأقل دقيقة واحدة")
    private Integer timeLimit;
    
    @NotNull(message = "النسبة المطلوبة مطلوبة")
    @Min(value = 0, message = "النسبة المطلوبة يجب أن تكون موجبة")
    @Max(value = 100, message = "النسبة المطلوبة يجب ألا تتجاوز 100%")
    private Integer passingScore;
}
