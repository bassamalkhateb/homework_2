package com.example.ELerning.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.ELerning.Entity.Course;
import com.example.ELerning.Entity.User;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    
    List<Course> findByStatus(Course.Status status);
    
    List<Course> findByInstructor(User instructor);
    
    List<Course> findByInstructorId(Long instructorId);
    
    @Query("SELECT c FROM Course c WHERE c.status = 'APPROVED'")
    List<Course> findApprovedCourses();
    
    @Query("SELECT c FROM Course c WHERE c.status = 'PENDING'")
    List<Course> findPendingCourses();
    
    @Query("SELECT c FROM Course c WHERE c.title LIKE %:title%")
    List<Course> findByTitleContaining(@Param("title") String title);
    
    @Query("SELECT c FROM Course c WHERE c.price BETWEEN :minPrice AND :maxPrice")
    List<Course> findByPriceRange(@Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice);
    
    @Query("SELECT c FROM Course c WHERE c.duration <= :maxDuration")
    List<Course> findByMaxDuration(@Param("maxDuration") Integer maxDuration);
    
    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.course.id = :courseId")
    Long countEnrollmentsByCourseId(@Param("courseId") Long courseId);
}

