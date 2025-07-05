package com.example.ELerning.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.ELerning.Entity.Course;
import com.example.ELerning.Entity.Quiz;

import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    
    List<Quiz> findByCourse(Course course);
    
    List<Quiz> findByCourseId(Long courseId);
    
    List<Quiz> findByIsActive(Boolean isActive);
    
    @Query("SELECT q FROM Quiz q WHERE q.course.id = :courseId AND q.isActive = true")
    List<Quiz> findActiveByCourseId(@Param("courseId") Long courseId);
    
    @Query("SELECT q FROM Quiz q WHERE q.title LIKE %:title%")
    List<Quiz> findByTitleContaining(@Param("title") String title);
    
    @Query("SELECT q FROM Quiz q WHERE q.timeLimit <= :maxTime")
    List<Quiz> findByMaxTimeLimit(@Param("maxTime") Integer maxTime);
    
    @Query("SELECT COUNT(qu) FROM Question qu WHERE qu.quiz.id = :quizId")
    Long countQuestionsByQuizId(@Param("quizId") Long quizId);
}

