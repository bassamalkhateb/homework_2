package com.example.ELerning.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.ELerning.Entity.Course;
import com.example.ELerning.Entity.Payment;
import com.example.ELerning.Entity.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    
    List<Payment> findByUser(User user);
    
    List<Payment> findByUserId(Long userId);
    
    List<Payment> findByCourse(Course course);
    
    List<Payment> findByCourseId(Long courseId);
    
    List<Payment> findByStatus(Payment.Status status);
    
    List<Payment> findByPaymentMethod(Payment.PaymentMethod paymentMethod);
    
    Optional<Payment> findByTransactionId(String transactionId);
    
    Optional<Payment> findByUserAndCourse(User user, Course course);
    
    Optional<Payment> findByUserIdAndCourseId(Long userId, Long courseId);
    
    @Query("SELECT p FROM Payment p WHERE p.user.id = :userId AND p.status = :status")
    List<Payment> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") Payment.Status status);
    
    @Query("SELECT p FROM Payment p WHERE p.course.id = :courseId AND p.status = :status")
    List<Payment> findByCourseIdAndStatus(@Param("courseId") Long courseId, @Param("status") Payment.Status status);
    
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.status = 'COMPLETED' AND p.completedAt BETWEEN :startDate AND :endDate")
    BigDecimal getTotalRevenueByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT COUNT(p) FROM Payment p WHERE p.course.id = :courseId AND p.status = 'COMPLETED'")
    Long countCompletedByCourseId(@Param("courseId") Long courseId);
}

