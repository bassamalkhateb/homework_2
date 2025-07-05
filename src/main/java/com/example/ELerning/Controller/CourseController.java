package com.example.ELerning.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.ELerning.DOT.CourseDTO;
import com.example.ELerning.DOT.CourseRequest;
import com.example.ELerning.Entity.Course;
import com.example.ELerning.Service.CourseService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

   @GetMapping
    public ResponseEntity<List<CourseDTO>> getAllCourses() {
        try {
            List<Course> courses = courseService.getAllCourses();
            List<CourseDTO> courseDTOs = courses.stream()
                    .map(CourseDTO::fromEntity)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(courseDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDTO> getCourseById(@PathVariable Long id) {
        try {
            Optional<Course> course = courseService.getCourseById(id);
            return course.map(c -> ResponseEntity.ok(CourseDTO.fromEntity(c)))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/approved")
    public ResponseEntity<List<CourseDTO>> getApprovedCourses() {
        try {
            List<Course> courses = courseService.getApprovedCourses();
            return ResponseEntity.ok(CourseDTO.fromEntities(courses));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/pending")
    public ResponseEntity<List<CourseDTO>> getPendingCourses() {
        try {
            List<Course> courses = courseService.getPendingCourses();
            return ResponseEntity.ok(CourseDTO.fromEntities(courses));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<CourseDTO>> getCoursesByStatus(@PathVariable Course.Status status) {
        try {
            List<Course> courses = courseService.getCoursesByStatus(status);
            return ResponseEntity.ok(CourseDTO.fromEntities(courses));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/instructor/{instructorId}")
    public ResponseEntity<List<CourseDTO>> getCoursesByInstructor(@PathVariable Long instructorId) {
        try {
            List<Course> courses = courseService.getCoursesByInstructor(instructorId);
            return ResponseEntity.ok(CourseDTO.fromEntities(courses));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<CourseDTO>> searchCoursesByTitle(@RequestParam String title) {
        try {
            List<Course> courses = courseService.searchCoursesByTitle(title);
            return ResponseEntity.ok(CourseDTO.fromEntities(courses));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/price-range")
    public ResponseEntity<List<CourseDTO>> getCoursesByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        try {
            List<Course> courses = courseService.getCoursesByPriceRange(minPrice, maxPrice);
            return ResponseEntity.ok(CourseDTO.fromEntities(courses));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/max-duration/{maxDuration}")
    public ResponseEntity<List<CourseDTO>> getCoursesByMaxDuration(@PathVariable Integer maxDuration) {
        try {
            List<Course> courses = courseService.getCoursesByMaxDuration(maxDuration);
            return ResponseEntity.ok(CourseDTO.fromEntities(courses));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<CourseDTO> createCourse(@RequestBody CourseRequest request) {
        try {
            Course createdCourse = courseService.createCourse(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(CourseDTO.fromEntity(createdCourse));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseDTO> updateCourse(@PathVariable Long id, @RequestBody CourseRequest courseDetails) {
        try {
            Course updatedCourse = courseService.updateCourse(id, courseDetails);
            return ResponseEntity.ok(CourseDTO.fromEntity(updatedCourse));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<CourseDTO> approveCourse(@PathVariable Long id) {
        try {
            Course approvedCourse = courseService.approveCourse(id);
            return ResponseEntity.ok(CourseDTO.fromEntity(approvedCourse));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<CourseDTO> rejectCourse(@PathVariable Long id) {
        try {
            Course rejectedCourse = courseService.rejectCourse(id);
            return ResponseEntity.ok(CourseDTO.fromEntity(rejectedCourse));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        try {
            courseService.deleteCourse(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}/enrollment-count")
    public ResponseEntity<Long> getEnrollmentCount(@PathVariable Long id) {
        try {
            Long count = courseService.getEnrollmentCount(id);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{courseId}/access/{userId}")
    public ResponseEntity<Boolean> canUserAccessCourse(
            @PathVariable Long userId, 
            @PathVariable Long courseId) {
        try {
            boolean canAccess = courseService.canUserAccessCourse(userId, courseId);
            return ResponseEntity.ok(canAccess);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
