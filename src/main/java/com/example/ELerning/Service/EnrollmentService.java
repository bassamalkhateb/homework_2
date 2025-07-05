package com.example.ELerning.Service;

import com.example.ELerning.Entity.Enrollment;
import com.example.ELerning.Entity.User;
import com.example.ELerning.Entity.Course;
import com.example.ELerning.Repository.EnrollmentRepository;
import com.example.ELerning.Repository.UserRepository;
import com.example.ELerning.exception.CourseNotApprovedException;
import com.example.ELerning.exception.StudentNotFoundException;
import com.example.ELerning.Repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
    }

    public Optional<Enrollment> getEnrollmentById(Long id) {
        return enrollmentRepository.findById(id);
    }

    public List<Enrollment> getEnrollmentsByStudent(Long studentId) {
        return enrollmentRepository.findByStudentId(studentId);
    }

    public List<Enrollment> getEnrollmentsByCourse(Long courseId) {
        return enrollmentRepository.findByCourseId(courseId);
    }

    public List<Enrollment> getEnrollmentsByStatus(Enrollment.Status status) {
        return enrollmentRepository.findByStatus(status);
    }

    public List<Enrollment> getStudentEnrollmentsByStatus(Long studentId, Enrollment.Status status) {
        return enrollmentRepository.findByStudentIdAndStatus(studentId, status);
    }

    public List<Enrollment> getCourseEnrollmentsByStatus(Long courseId, Enrollment.Status status) {
        return enrollmentRepository.findByCourseIdAndStatus(courseId, status);
    }

    public Optional<Enrollment> getEnrollmentByStudentAndCourse(Long studentId, Long courseId) {
        return enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId);
    }

    public Enrollment enrollStudent(Long studentId, Long courseId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(studentId));

        if (student.getRole() != User.Role.STUDENT) {
            throw new RuntimeException("المستخدم ليس طالباً");
        }

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("الدورة غير موجودة"));

        if (course.getStatus() != Course.Status.APPROVED) {
            throw new CourseNotApprovedException(courseId);
        }

        if (enrollmentRepository.existsByStudentIdAndCourseId(studentId, courseId)) {
            throw new RuntimeException("الطالب مسجل بالفعل في هذه الدورة");
        }

        Enrollment enrollment = new Enrollment(student, course);
        enrollment.setEnrollmentDate(LocalDateTime.now());
        enrollment.setStatus(Enrollment.Status.ACTIVE);
        enrollment.setProgress(0);

        return enrollmentRepository.save(enrollment);
    }

    public Enrollment updateProgress(Long enrollmentId, Integer progress) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("التسجيل غير موجود"));

        if (progress < 0 || progress > 100) {
            throw new RuntimeException("نسبة التقدم يجب أن تكون بين 0 و 100");
        }

        enrollment.setProgress(progress);

        if (progress == 100) {
            enrollment.setStatus(Enrollment.Status.COMPLETED);
            enrollment.setCompletionDate(LocalDateTime.now());
        }

        return enrollmentRepository.save(enrollment);
    }

    public Enrollment completeEnrollment(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("التسجيل غير موجود"));

        enrollment.setStatus(Enrollment.Status.COMPLETED);
        enrollment.setProgress(100);
        enrollment.setCompletionDate(LocalDateTime.now());

        return enrollmentRepository.save(enrollment);
    }

    public Enrollment cancelEnrollment(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("التسجيل غير موجود"));

        enrollment.setStatus(Enrollment.Status.CANCELLED);

        return enrollmentRepository.save(enrollment);
    }

    public void deleteEnrollment(Long id) {
        if (!enrollmentRepository.existsById(id)) {
            throw new RuntimeException("التسجيل غير موجود");
        }
        enrollmentRepository.deleteById(id);
    }

    public Long getActiveEnrollmentCount(Long courseId) {
        return enrollmentRepository.countActiveByCourseId(courseId);
    }

    public Long getCompletedEnrollmentCount(Long studentId) {
        return enrollmentRepository.countCompletedByStudentId(studentId);
    }

    public boolean isStudentEnrolled(Long studentId, Long courseId) {
        return enrollmentRepository.existsByStudentIdAndCourseId(studentId, courseId);
    }

    public boolean hasStudentCompletedCourse(Long studentId, Long courseId) {
        Optional<Enrollment> enrollment = enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId);
        return enrollment.isPresent() && enrollment.get().getStatus() == Enrollment.Status.COMPLETED;
    }
}
