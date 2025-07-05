package com.example.ELerning.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.ELerning.Entity.Question;
import com.example.ELerning.Entity.Quiz;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    
    List<Question> findByQuiz(Quiz quiz);
    
    List<Question> findByQuizId(Long quizId);
    
    List<Question> findByType(Question.Type type);
    
    @Query("SELECT q FROM Question q WHERE q.quiz.id = :quizId ORDER BY q.id")
    List<Question> findByQuizIdOrderById(@Param("quizId") Long quizId);
    
    @Query("SELECT SUM(q.points) FROM Question q WHERE q.quiz.id = :quizId")
    Integer getTotalPointsByQuizId(@Param("quizId") Long quizId);
}

