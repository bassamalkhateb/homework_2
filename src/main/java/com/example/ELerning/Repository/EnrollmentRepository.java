package com.example.ELerning.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.ELerning.Entity.Course;
import com.example.ELerning.Entity.Enrollment;
import com.example.ELerning.Entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    List<Enrollment> findByStudent(User student);

    List<Enrollment> findByStudentId(Long studentId);

    List<Enrollment> findByCourse(Course course);

    List<Enrollment> findByCourseId(Long courseId);

    List<Enrollment> findByStatus(Enrollment.Status status);

    Optional<Enrollment> findByStudentAndCourse(User student, Course course);

    Optional<Enrollment> findByStudentIdAndCourseId(Long studentId, Long courseId);

    boolean existsByStudentAndCourse(User student, Course course);

    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);
    

    @Query("SELECT e FROM Enrollment e WHERE e.student.id = :studentId AND e.status = :status")
    List<Enrollment> findByStudentIdAndStatus(@Param("studentId") Long studentId,
            @Param("status") Enrollment.Status status);

    @Query("SELECT e FROM Enrollment e WHERE e.course.id = :courseId AND e.status = :status")
    List<Enrollment> findByCourseIdAndStatus(@Param("courseId") Long courseId,
            @Param("status") Enrollment.Status status);

    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.course.id = :courseId AND e.status = 'ACTIVE'")
    Long countActiveByCourseId(@Param("courseId") Long courseId);

    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.student.id = :studentId AND e.status = 'COMPLETED'")
    Long countCompletedByStudentId(@Param("studentId") Long studentId);
}
