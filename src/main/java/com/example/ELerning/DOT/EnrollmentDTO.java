package com.example.ELerning.DOT; // تصحيح اسم الحزمة من DOT إلى DTO

import com.example.ELerning.Entity.Enrollment;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class EnrollmentDTO {
    private Long id;
    private LocalDateTime enrollmentDate;
    private String status;
    private Integer progress;
    private Long studentId;
    private String studentName;
    private Long courseId;
    private String courseTitle;

    public static EnrollmentDTO convertToEnrollmentDTO(Enrollment enrollment) {
        if (enrollment == null) {
            return null;
        }

        EnrollmentDTO dto = new EnrollmentDTO();
        dto.setId(enrollment.getId());
        dto.setEnrollmentDate(enrollment.getEnrollmentDate());
        dto.setStatus(enrollment.getStatus().name());
        dto.setProgress(enrollment.getProgress());
        
        if (enrollment.getStudent() != null) {
            dto.setStudentId(enrollment.getStudent().getId());
            dto.setStudentName(enrollment.getStudent().getFirstName() + " " + enrollment.getStudent().getLastName());
        }
        
        if (enrollment.getCourse() != null) {
            dto.setCourseId(enrollment.getCourse().getId());
            dto.setCourseTitle(enrollment.getCourse().getTitle());
        }
        
        return dto;
    }
}