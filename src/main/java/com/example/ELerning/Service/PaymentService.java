package com.example.ELerning.Service;

import com.example.ELerning.Entity.Payment;
import com.example.ELerning.Entity.User;
import com.example.ELerning.Entity.Course;
import com.example.ELerning.Repository.PaymentRepository;
import com.example.ELerning.Repository.UserRepository;
import com.example.ELerning.Repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EnrollmentService enrollmentService;

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Optional<Payment> getPaymentById(Long id) {
        return paymentRepository.findById(id);
    }

    public List<Payment> getPaymentsByUser(Long userId) {
        return paymentRepository.findByUserId(userId);
    }

    public List<Payment> getPaymentsByCourse(Long courseId) {
        return paymentRepository.findByCourseId(courseId);
    }

    public List<Payment> getPaymentsByStatus(Payment.Status status) {
        return paymentRepository.findByStatus(status);
    }

    public List<Payment> getPaymentsByMethod(Payment.PaymentMethod paymentMethod) {
        return paymentRepository.findByPaymentMethod(paymentMethod);
    }

    public List<Payment> getUserPaymentsByStatus(Long userId, Payment.Status status) {
        return paymentRepository.findByUserIdAndStatus(userId, status);
    }

    public List<Payment> getCoursePaymentsByStatus(Long courseId, Payment.Status status) {
        return paymentRepository.findByCourseIdAndStatus(courseId, status);
    }

    public Optional<Payment> getPaymentByTransactionId(String transactionId) {
        return paymentRepository.findByTransactionId(transactionId);
    }

    public Optional<Payment> getPaymentByUserAndCourse(Long userId, Long courseId) {
        return paymentRepository.findByUserIdAndCourseId(userId, courseId);
    }

    public Payment createPayment(Long userId, Long courseId, Payment.PaymentMethod paymentMethod) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("المستخدم غير موجود"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("الدورة غير موجودة"));

        if (course.getStatus() != Course.Status.APPROVED) {
            throw new RuntimeException("الدورة غير معتمدة للدفع");
        }

        // Check if user already has a payment for this course
        if (paymentRepository.findByUserIdAndCourseId(userId, courseId).isPresent()) {
            throw new RuntimeException("يوجد دفعة سابقة لهذه الدورة");
        }

        // Check if user is already enrolled
        if (enrollmentService.isStudentEnrolled(userId, courseId)) {
            throw new RuntimeException("المستخدم مسجل بالفعل في هذه الدورة");
        }

        Payment payment = new Payment(user, course, course.getPrice(), paymentMethod);
        payment.setStatus(Payment.Status.PENDING);
        payment.setCreatedAt(LocalDateTime.now());
        payment.setTransactionId(generateTransactionId());

        return paymentRepository.save(payment);
    }

    public Payment processPayment(Long paymentId, String externalTransactionId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("الدفعة غير موجودة"));

        if (payment.getStatus() != Payment.Status.PENDING) {
            throw new RuntimeException("الدفعة ليست في حالة انتظار");
        }

        // Simulate payment processing
        // In real implementation, this would integrate with payment gateway
        boolean paymentSuccessful = simulatePaymentProcessing();

        if (paymentSuccessful) {
            payment.setStatus(Payment.Status.COMPLETED);
            payment.setCompletedAt(LocalDateTime.now());
            if (externalTransactionId != null) {
                payment.setTransactionId(externalTransactionId);
            }

            // Auto-enroll student after successful payment
            enrollmentService.enrollStudent(payment.getUser().getId(), payment.getCourse().getId());
        } else {
            payment.setStatus(Payment.Status.FAILED);
        }

        return paymentRepository.save(payment);
    }

    public Payment completePayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("الدفعة غير موجودة"));

        if (payment.getStatus() != Payment.Status.PENDING) {
            throw new RuntimeException("الدفعة ليست في حالة انتظار");
        }

        payment.setStatus(Payment.Status.COMPLETED);
        payment.setCompletedAt(LocalDateTime.now());

        // Auto-enroll student after successful payment
        enrollmentService.enrollStudent(payment.getUser().getId(), payment.getCourse().getId());

        return paymentRepository.save(payment);
    }

    public Payment failPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("الدفعة غير موجودة"));

        if (payment.getStatus() != Payment.Status.PENDING) {
            throw new RuntimeException("الدفعة ليست في حالة انتظار");
        }

        payment.setStatus(Payment.Status.FAILED);

        return paymentRepository.save(payment);
    }

    public Payment refundPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("الدفعة غير موجودة"));

        if (payment.getStatus() != Payment.Status.COMPLETED) {
            throw new RuntimeException("لا يمكن استرداد دفعة غير مكتملة");
        }

        payment.setStatus(Payment.Status.REFUNDED);

        return paymentRepository.save(payment);
    }

    public void deletePayment(Long id) {
        if (!paymentRepository.existsById(id)) {
            throw new RuntimeException("الدفعة غير موجودة");
        }
        paymentRepository.deleteById(id);
    }

    public BigDecimal getTotalRevenue(LocalDateTime startDate, LocalDateTime endDate) {
        BigDecimal revenue = paymentRepository.getTotalRevenueByDateRange(startDate, endDate);
        return revenue != null ? revenue : BigDecimal.ZERO;
    }

    public Long getCompletedPaymentCount(Long courseId) {
        return paymentRepository.countCompletedByCourseId(courseId);
    }

    public boolean hasUserPaidForCourse(Long userId, Long courseId) {
        Optional<Payment> payment = paymentRepository.findByUserIdAndCourseId(userId, courseId);
        return payment.isPresent() && payment.get().getStatus() == Payment.Status.COMPLETED;
    }

    public BigDecimal getTotalRevenueForCourse(Long courseId) {
        List<Payment> completedPayments = paymentRepository.findByCourseIdAndStatus(courseId, Payment.Status.COMPLETED);
        return completedPayments.stream()
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private String generateTransactionId() {
        return "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private boolean simulatePaymentProcessing() {
        // Simulate payment processing with 90% success rate
        return Math.random() > 0.1;
    }
}

