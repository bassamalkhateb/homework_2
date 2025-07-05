package com.example.ELerning.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.ELerning.Entity.Answer;
import com.example.ELerning.Entity.Question;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    
    List<Answer> findByQuestion(Question question);
    
    List<Answer> findByQuestionId(Long questionId);
    
    List<Answer> findByIsCorrect(Boolean isCorrect);
    
    @Query("SELECT a FROM Answer a WHERE a.question.id = :questionId AND a.isCorrect = true")
    List<Answer> findCorrectAnswersByQuestionId(@Param("questionId") Long questionId);
    
    @Query("SELECT a FROM Answer a WHERE a.question.id = :questionId ORDER BY a.id")
    List<Answer> findByQuestionIdOrderById(@Param("questionId") Long questionId);
}

