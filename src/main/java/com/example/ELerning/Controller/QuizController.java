package com.example.ELerning.Controller;

import com.example.ELerning.Entity.Quiz;
import com.example.ELerning.Entity.Question;
import com.example.ELerning.DOT.AnswerRequestDTO;
import com.example.ELerning.DOT.AnswerUpdateDTO;
import com.example.ELerning.DOT.QuestionRequestDTO;
import com.example.ELerning.DOT.QuizRequestDTO;
import com.example.ELerning.DOT.QuizUpdateDTO;
import com.example.ELerning.Entity.Answer;
import com.example.ELerning.Service.QuizService;
import com.example.ELerning.exception.ResourceNotFoundException;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    @Autowired
    private QuizService quizService;

    // Quiz endpoints
    @GetMapping
    public ResponseEntity<List<Quiz>> getAllQuizzes() {
        try {
            List<Quiz> quizzes = quizService.getAllQuizzes();
            return ResponseEntity.ok(quizzes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Quiz> getQuizById(@PathVariable Long id) {
        try {
            Optional<Quiz> quiz = quizService.getQuizById(id);
            return quiz.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Quiz>> getQuizzesByCourse(@PathVariable Long courseId) {
        try {
            List<Quiz> quizzes = quizService.getQuizzesByCourse(courseId);
            return ResponseEntity.ok(quizzes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/course/{courseId}/active")
    public ResponseEntity<List<Quiz>> getActiveQuizzesByCourse(@PathVariable Long courseId) {
        try {
            List<Quiz> quizzes = quizService.getActiveQuizzesByCourse(courseId);
            return ResponseEntity.ok(quizzes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Quiz>> searchQuizzesByTitle(@RequestParam String title) {
        try {
            List<Quiz> quizzes = quizService.searchQuizzesByTitle(title);
            return ResponseEntity.ok(quizzes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createQuiz(@Valid @RequestBody QuizRequestDTO quizRequest) {
        try {
            Quiz createdQuiz = quizService.createQuiz(quizRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdQuiz);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("حدث خطأ أثناء إنشاء الاختبار");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateQuiz(
            @PathVariable Long id,
            @Valid @RequestBody QuizUpdateDTO quizUpdateDTO) {
        try {
            Quiz updatedQuiz = quizService.updateQuiz(id, quizUpdateDTO);
            return ResponseEntity.ok(updatedQuiz);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("حدث خطأ أثناء تحديث الاختبار");
        }
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<Quiz> activateQuiz(@PathVariable Long id) {
        try {
            Quiz activatedQuiz = quizService.activateQuiz(id);
            return ResponseEntity.ok(activatedQuiz);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Quiz> deactivateQuiz(@PathVariable Long id) {
        try {
            Quiz deactivatedQuiz = quizService.deactivateQuiz(id);
            return ResponseEntity.ok(deactivatedQuiz);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuiz(@PathVariable Long id) {
        try {
            quizService.deleteQuiz(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Question endpoints
    @GetMapping("/{quizId}/questions")
    public ResponseEntity<List<Question>> getQuestionsByQuiz(@PathVariable Long quizId) {
        try {
            List<Question> questions = quizService.getQuestionsByQuiz(quizId);
            return ResponseEntity.ok(questions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/questions/{id}")
    public ResponseEntity<Question> getQuestionById(@PathVariable Long id) {
        try {
            Optional<Question> question = quizService.getQuestionById(id);
            return question.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/questions") // ← المسار النسبي بعد /api
    public ResponseEntity<?> createQuestion(@Valid @RequestBody QuestionRequestDTO questionRequest) {
        try {
            Question createdQuestion = quizService.createQuestion(questionRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdQuestion);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("حدث خطأ أثناء إنشاء السؤال");
        }
    }

    @PutMapping("/questions/{id}")
    public ResponseEntity<Question> updateQuestion(@PathVariable Long id, @RequestBody Question questionDetails) {
        try {
            Question updatedQuestion = quizService.updateQuestion(id, questionDetails);
            return ResponseEntity.ok(updatedQuestion);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/questions/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
        try {
            quizService.deleteQuestion(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Answer endpoints
    @GetMapping("/questions/{questionId}/answers")
    public ResponseEntity<List<Answer>> getAnswersByQuestion(@PathVariable Long questionId) {
        try {
            List<Answer> answers = quizService.getAnswersByQuestion(questionId);
            return ResponseEntity.ok(answers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/questions/{questionId}/correct-answers")
    public ResponseEntity<List<Answer>> getCorrectAnswersByQuestion(@PathVariable Long questionId) {
        try {
            List<Answer> answers = quizService.getCorrectAnswersByQuestion(questionId);
            return ResponseEntity.ok(answers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/answers/{id}")
    public ResponseEntity<Answer> getAnswerById(@PathVariable Long id) {
        try {
            Optional<Answer> answer = quizService.getAnswerById(id);
            return answer.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/answers") // استخدم هذا بدلاً من @PostMapping("/answers/{id}")
    public ResponseEntity<?> createAnswer(@Valid @RequestBody AnswerRequestDTO answerRequest) {
        try {
            Answer createdAnswer = quizService.createAnswer(answerRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAnswer);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("حدث خطأ أثناء إنشاء الإجابة");
        }
    }

    @PutMapping(value = "/answers/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateAnswer(
            @PathVariable Long id,
            @Valid @RequestBody AnswerUpdateDTO answerUpdateDTO) {
        try {
            Answer updatedAnswer = quizService.updateAnswer(id, answerUpdateDTO);
            return ResponseEntity.ok(updatedAnswer);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("حدث خطأ أثناء تحديث الإجابة");
        }
    }

    @DeleteMapping("/answers/{id}")
    public ResponseEntity<Void> deleteAnswer(@PathVariable Long id) {
        try {
            quizService.deleteAnswer(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Utility endpoints
    @GetMapping("/{quizId}/question-count")
    public ResponseEntity<Long> getQuestionCount(@PathVariable Long quizId) {
        try {
            Long count = quizService.getQuestionCount(quizId);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{quizId}/total-points")
    public ResponseEntity<Integer> getTotalPoints(@PathVariable Long quizId) {
        try {
            Integer totalPoints = quizService.getTotalPoints(quizId);
            return ResponseEntity.ok(totalPoints);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{quizId}/access/{userId}")
    public ResponseEntity<Boolean> canUserAccessQuiz(@PathVariable Long userId, @PathVariable Long quizId) {
        try {
            boolean canAccess = quizService.canUserAccessQuiz(userId, quizId);
            return ResponseEntity.ok(canAccess);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
