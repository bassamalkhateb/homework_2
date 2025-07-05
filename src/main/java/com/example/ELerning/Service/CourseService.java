package com.example.ELerning.Service;

import com.example.ELerning.DOT.CourseRequest;
import com.example.ELerning.Entity.Course;
import com.example.ELerning.Entity.User;
import com.example.ELerning.Repository.CourseRepository;
import com.example.ELerning.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

     public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }

      public List<Course> getApprovedCourses() {
        return courseRepository.findApprovedCourses();
    }

    public List<Course> getPendingCourses() {
        return courseRepository.findPendingCourses();
    }

    public List<Course> getCoursesByStatus(Course.Status status) {
        return courseRepository.findByStatus(status);
    }

    public List<Course> getCoursesByInstructor(Long instructorId) {
        return courseRepository.findByInstructorId(instructorId);
    }

    public List<Course> searchCoursesByTitle(String title) {
        return courseRepository.findByTitleContaining(title);
    }

    public List<Course> getCoursesByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return courseRepository.findByPriceRange(minPrice, maxPrice);
    }

    public List<Course> getCoursesByMaxDuration(Integer maxDuration) {
        return courseRepository.findByMaxDuration(maxDuration);
    }

   public Course createCourse(CourseRequest request) {
        User instructor = userRepository.findById(request.getInstructorId())
                .orElseThrow(() -> new RuntimeException("المدرب غير موجود"));

        if (instructor.getRole() != User.Role.INSTRUCTOR) {
            throw new RuntimeException("المستخدم ليس مدرباً");
        }

        Course newCourse = new Course();
        newCourse.setTitle(request.getTitle());
        newCourse.setDescription(request.getDescription());
        newCourse.setPrice(request.getPrice());
        newCourse.setDuration(request.getDuration());
        newCourse.setInstructor(instructor);
        newCourse.setStatus(Course.Status.PENDING);
        newCourse.setCreatedAt(LocalDateTime.now());
        
        return courseRepository.save(newCourse);
    }

     public Course updateCourse(Long id, CourseRequest request) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("الدورة غير موجودة"));

        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setPrice(request.getPrice());
        course.setDuration(request.getDuration());
        course.setUpdatedAt(LocalDateTime.now());

        return courseRepository.save(course);
    }

    public Course approveCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("الدورة غير موجودة"));

        course.setStatus(Course.Status.APPROVED);
        course.setUpdatedAt(LocalDateTime.now());

        return courseRepository.save(course);
    }

    public Course rejectCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("الدورة غير موجودة"));

        course.setStatus(Course.Status.REJECTED);
        course.setUpdatedAt(LocalDateTime.now());

        return courseRepository.save(course);
    }

    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new RuntimeException("الدورة غير موجودة");
        }
        courseRepository.deleteById(id);
    }

    public Long getEnrollmentCount(Long courseId) {
        return courseRepository.countEnrollmentsByCourseId(courseId);
    }

    public boolean canUserAccessCourse(Long userId, Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("الدورة غير موجودة"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("المستخدم غير موجود"));

        // Admin can access all courses
        if (user.getRole() == User.Role.ADMIN) {
            return true;
        }

        // Instructor can access their own courses
        if (user.getRole() == User.Role.INSTRUCTOR && 
            course.getInstructor().getId().equals(userId)) {
            return true;
        }

        // Students can only access approved courses they are enrolled in
        return course.getStatus() == Course.Status.APPROVED;
    }
}

