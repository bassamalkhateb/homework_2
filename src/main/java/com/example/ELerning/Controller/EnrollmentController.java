package com.example.ELerning.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.ELerning.Entity.Enrollment;
import com.example.ELerning.Service.EnrollmentService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @GetMapping
    public ResponseEntity<?> getAllEnrollments() {
        try {
            List<Enrollment> enrollments = enrollmentService.getAllEnrollments();
            return ResponseEntity.ok(enrollments);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error fetching enrollments: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Enrollment> getEnrollmentById(@PathVariable Long id) {
        try {
            Optional<Enrollment> enrollment = enrollmentService.getEnrollmentById(id);
            return enrollment.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Enrollment>> getEnrollmentsByStudent(@PathVariable Long studentId) {
        try {
            List<Enrollment> enrollments = enrollmentService.getEnrollmentsByStudent(studentId);
            return ResponseEntity.ok(enrollments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Enrollment>> getEnrollmentsByCourse(@PathVariable Long courseId) {
        try {
            List<Enrollment> enrollments = enrollmentService.getEnrollmentsByCourse(courseId);
            return ResponseEntity.ok(enrollments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Enrollment>> getEnrollmentsByStatus(@PathVariable Enrollment.Status status) {
        try {
            List<Enrollment> enrollments = enrollmentService.getEnrollmentsByStatus(status);
            return ResponseEntity.ok(enrollments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/student/{studentId}/status/{status}")
    public ResponseEntity<List<Enrollment>> getStudentEnrollmentsByStatus(
            @PathVariable Long studentId,
            @PathVariable Enrollment.Status status) {
        try {
            List<Enrollment> enrollments = enrollmentService.getStudentEnrollmentsByStatus(studentId, status);
            return ResponseEntity.ok(enrollments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/course/{courseId}/status/{status}")
    public ResponseEntity<List<Enrollment>> getCourseEnrollmentsByStatus(
            @PathVariable Long courseId,
            @PathVariable Enrollment.Status status) {
        try {
            List<Enrollment> enrollments = enrollmentService.getCourseEnrollmentsByStatus(courseId, status);
            return ResponseEntity.ok(enrollments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/student/{studentId}/course/{courseId}")
    public ResponseEntity<Enrollment> getEnrollmentByStudentAndCourse(
            @PathVariable Long studentId,
            @PathVariable Long courseId) {
        try {
            Optional<Enrollment> enrollment = enrollmentService.getEnrollmentByStudentAndCourse(studentId, courseId);
            return enrollment.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/enroll")
    public ResponseEntity<Enrollment> enrollStudent(
            @RequestParam Long studentId,
            @RequestParam Long courseId) {
        try {
            Enrollment enrollment = enrollmentService.enrollStudent(studentId, courseId);
            return ResponseEntity.status(HttpStatus.CREATED).body(enrollment);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}/progress")
    public ResponseEntity<Enrollment> updateProgress(
            @PathVariable Long id,
            @RequestParam Integer progress) {
        try {
            Enrollment updatedEnrollment = enrollmentService.updateProgress(id, progress);
            return ResponseEntity.ok(updatedEnrollment);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<Enrollment> completeEnrollment(@PathVariable Long id) {
        try {
            Enrollment completedEnrollment = enrollmentService.completeEnrollment(id);
            return ResponseEntity.ok(completedEnrollment);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Enrollment> cancelEnrollment(@PathVariable Long id) {
        try {
            Enrollment cancelledEnrollment = enrollmentService.cancelEnrollment(id);
            return ResponseEntity.ok(cancelledEnrollment);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnrollment(@PathVariable Long id) {
        try {
            enrollmentService.deleteEnrollment(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/course/{courseId}/active-count")
    public ResponseEntity<Long> getActiveEnrollmentCount(@PathVariable Long courseId) {
        try {
            Long count = enrollmentService.getActiveEnrollmentCount(courseId);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/student/{studentId}/completed-count")
    public ResponseEntity<Long> getCompletedEnrollmentCount(@PathVariable Long studentId) {
        try {
            Long count = enrollmentService.getCompletedEnrollmentCount(studentId);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/student/{studentId}/course/{courseId}/enrolled")
    public ResponseEntity<Boolean> isStudentEnrolled(
            @PathVariable Long studentId,
            @PathVariable Long courseId) {
        try {
            boolean isEnrolled = enrollmentService.isStudentEnrolled(studentId, courseId);
            return ResponseEntity.ok(isEnrolled);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/student/{studentId}/course/{courseId}/completed")
    public ResponseEntity<Boolean> hasStudentCompletedCourse(
            @PathVariable Long studentId,
            @PathVariable Long courseId) {
        try {
            boolean hasCompleted = enrollmentService.hasStudentCompletedCourse(studentId, courseId);
            return ResponseEntity.ok(hasCompleted);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
