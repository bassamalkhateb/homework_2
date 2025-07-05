package com.example.ELerning.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.ELerning.Entity.Quiz;
import com.example.ELerning.Entity.Result;
import com.example.ELerning.Entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {
    
    List<Result> findByStudent(User student);
    
    List<Result> findByStudentId(Long studentId);
    
    List<Result> findByQuiz(Quiz quiz);
    
    List<Result> findByQuizId(Long quizId);
    
    List<Result> findByPassed(Boolean passed);
    
    Optional<Result> findByStudentAndQuiz(User student, Quiz quiz);
    
    Optional<Result> findByStudentIdAndQuizId(Long studentId, Long quizId);
    
    @Query("SELECT r FROM Result r WHERE r.student.id = :studentId AND r.passed = true")
    List<Result> findPassedResultsByStudentId(@Param("studentId") Long studentId);
    
    @Query("SELECT r FROM Result r WHERE r.quiz.id = :quizId AND r.passed = true")
    List<Result> findPassedResultsByQuizId(@Param("quizId") Long quizId);
    
    @Query("SELECT AVG(r.score) FROM Result r WHERE r.quiz.id = :quizId")
    Double getAverageScoreByQuizId(@Param("quizId") Long quizId);
    
    @Query("SELECT COUNT(r) FROM Result r WHERE r.quiz.id = :quizId AND r.passed = true")
    Long countPassedByQuizId(@Param("quizId") Long quizId);
    
    @Query("SELECT COUNT(r) FROM Result r WHERE r.student.id = :studentId AND r.passed = true")
    Long countPassedByStudentId(@Param("studentId") Long studentId);
}

