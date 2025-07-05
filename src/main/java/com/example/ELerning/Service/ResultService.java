package com.example.ELerning.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ELerning.Entity.Quiz;
import com.example.ELerning.Entity.Result;
import com.example.ELerning.Entity.User;
import com.example.ELerning.Repository.QuizRepository;
import com.example.ELerning.Repository.ResultRepository;
import com.example.ELerning.Repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ResultService {

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuizRepository quizRepository;

    public List<Result> getAllResults() {
        return resultRepository.findAll();
    }

    public Optional<Result> getResultById(Long id) {
        return resultRepository.findById(id);
    }

    public List<Result> getResultsByStudent(Long studentId) {
        return resultRepository.findByStudentId(studentId);
    }

    public List<Result> getResultsByQuiz(Long quizId) {
        return resultRepository.findByQuizId(quizId);
    }

    public List<Result> getPassedResults() {
        return resultRepository.findByPassed(true);
    }

    public List<Result> getFailedResults() {
        return resultRepository.findByPassed(false);
    }

    public List<Result> getPassedResultsByStudent(Long studentId) {
        return resultRepository.findPassedResultsByStudentId(studentId);
    }

    public List<Result> getPassedResultsByQuiz(Long quizId) {
        return resultRepository.findPassedResultsByQuizId(quizId);
    }

    public Optional<Result> getResultByStudentAndQuiz(Long studentId, Long quizId) {
        return resultRepository.findByStudentIdAndQuizId(studentId, quizId);
    }

    public Result submitQuizResult(Long studentId, Long quizId, Integer correctAnswers, Integer totalQuestions, Integer timeSpent) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("الطالب غير موجود"));

        if (student.getRole() != User.Role.STUDENT) {
            throw new RuntimeException("المستخدم ليس طالباً");
        }

        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("الاختبار غير موجود"));

        if (!quiz.getIsActive()) {
            throw new RuntimeException("الاختبار غير نشط");
        }

        // Check if student already has a result for this quiz
        if (resultRepository.findByStudentIdAndQuizId(studentId, quizId).isPresent()) {
            throw new RuntimeException("الطالب قد أجرى هذا الاختبار بالفعل");
        }

        // Calculate score percentage
        Integer score = (correctAnswers * 100) / totalQuestions;
        Boolean passed = score >= quiz.getPassingScore();

        Result result = new Result(student, quiz, score, totalQuestions, correctAnswers, passed, timeSpent);
        result.setCompletedAt(LocalDateTime.now());

        return resultRepository.save(result);
    }

    public Result updateResult(Long id, Result resultDetails) {
        Result result = resultRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("النتيجة غير موجودة"));

        result.setScore(resultDetails.getScore());
        result.setTotalQuestions(resultDetails.getTotalQuestions());
        result.setCorrectAnswers(resultDetails.getCorrectAnswers());
        result.setPassed(resultDetails.getPassed());
        result.setTimeSpent(resultDetails.getTimeSpent());

        return resultRepository.save(result);
    }

    public void deleteResult(Long id) {
        if (!resultRepository.existsById(id)) {
            throw new RuntimeException("النتيجة غير موجودة");
        }
        resultRepository.deleteById(id);
    }

    public Double getAverageScoreByQuiz(Long quizId) {
        Double averageScore = resultRepository.getAverageScoreByQuizId(quizId);
        return averageScore != null ? averageScore : 0.0;
    }

    public Long getPassedCount(Long quizId) {
        return resultRepository.countPassedByQuizId(quizId);
    }

    public Long getStudentPassedCount(Long studentId) {
        return resultRepository.countPassedByStudentId(studentId);
    }

    public boolean hasStudentPassedQuiz(Long studentId, Long quizId) {
        Optional<Result> result = resultRepository.findByStudentIdAndQuizId(studentId, quizId);
        return result.isPresent() && result.get().getPassed();
    }

    public boolean hasStudentTakenQuiz(Long studentId, Long quizId) {
        return resultRepository.findByStudentIdAndQuizId(studentId, quizId).isPresent();
    }

    public Double getStudentAverageScore(Long studentId) {
        List<Result> results = resultRepository.findByStudentId(studentId);
        if (results.isEmpty()) {
            return 0.0;
        }
        
        double totalScore = results.stream().mapToInt(Result::getScore).sum();
        return totalScore / results.size();
    }

    public Long getTotalResultsCount() {
        return resultRepository.count();
    }

    public Long getPassedResultsCount() {
        return  (long) resultRepository.findByPassed(true).size();
    }

    public Long getFailedResultsCount() {
        return (long) resultRepository.findByPassed(false).size();
    }
}

