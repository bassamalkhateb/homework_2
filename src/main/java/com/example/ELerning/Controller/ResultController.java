package com.example.ELerning.Controller;

import com.example.ELerning.Entity.Result;
import com.example.ELerning.Service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/results")
@CrossOrigin(origins = "*")
public class ResultController {

    @Autowired
    private ResultService resultService;

    @GetMapping
    public ResponseEntity<List<Result>> getAllResults() {
        try {
            List<Result> results = resultService.getAllResults();
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Result> getResultById(@PathVariable Long id) {
        try {
            Optional<Result> result = resultService.getResultById(id);
            return result.map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Result>> getResultsByStudent(@PathVariable Long studentId) {
        try {
            List<Result> results = resultService.getResultsByStudent(studentId);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/quiz/{quizId}")
    public ResponseEntity<List<Result>> getResultsByQuiz(@PathVariable Long quizId) {
        try {
            List<Result> results = resultService.getResultsByQuiz(quizId);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/passed")
    public ResponseEntity<List<Result>> getPassedResults() {
        try {
            List<Result> results = resultService.getPassedResults();
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/failed")
    public ResponseEntity<List<Result>> getFailedResults() {
        try {
            List<Result> results = resultService.getFailedResults();
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/student/{studentId}/passed")
    public ResponseEntity<List<Result>> getPassedResultsByStudent(@PathVariable Long studentId) {
        try {
            List<Result> results = resultService.getPassedResultsByStudent(studentId);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/quiz/{quizId}/passed")
    public ResponseEntity<List<Result>> getPassedResultsByQuiz(@PathVariable Long quizId) {
        try {
            List<Result> results = resultService.getPassedResultsByQuiz(quizId);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/student/{studentId}/quiz/{quizId}")
    public ResponseEntity<Result> getResultByStudentAndQuiz(
            @PathVariable Long studentId, 
            @PathVariable Long quizId) {
        try {
            Optional<Result> result = resultService.getResultByStudentAndQuiz(studentId, quizId);
            return result.map(ResponseEntity::ok)
                        .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/submit")
    public ResponseEntity<Result> submitQuizResult(
            @RequestParam Long studentId,
            @RequestParam Long quizId,
            @RequestParam Integer correctAnswers,
            @RequestParam Integer totalQuestions,
            @RequestParam Integer timeSpent) {
        try {
            Result result = resultService.submitQuizResult(studentId, quizId, correctAnswers, totalQuestions, timeSpent);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Result> updateResult(@PathVariable Long id, @RequestBody Result resultDetails) {
        try {
            Result updatedResult = resultService.updateResult(id, resultDetails);
            return ResponseEntity.ok(updatedResult);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResult(@PathVariable Long id) {
        try {
            resultService.deleteResult(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/quiz/{quizId}/average-score")
    public ResponseEntity<Double> getAverageScoreByQuiz(@PathVariable Long quizId) {
        try {
            Double averageScore = resultService.getAverageScoreByQuiz(quizId);
            return ResponseEntity.ok(averageScore);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/quiz/{quizId}/passed-count")
    public ResponseEntity<Long> getPassedCount(@PathVariable Long quizId) {
        try {
            Long count = resultService.getPassedCount(quizId);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/student/{studentId}/passed-count")
    public ResponseEntity<Long> getStudentPassedCount(@PathVariable Long studentId) {
        try {
            Long count = resultService.getStudentPassedCount(studentId);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/student/{studentId}/quiz/{quizId}/passed")
    public ResponseEntity<Boolean> hasStudentPassedQuiz(
            @PathVariable Long studentId, 
            @PathVariable Long quizId) {
        try {
            boolean hasPassed = resultService.hasStudentPassedQuiz(studentId, quizId);
            return ResponseEntity.ok(hasPassed);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/student/{studentId}/quiz/{quizId}/taken")
    public ResponseEntity<Boolean> hasStudentTakenQuiz(
            @PathVariable Long studentId, 
            @PathVariable Long quizId) {
        try {
            boolean hasTaken = resultService.hasStudentTakenQuiz(studentId, quizId);
            return ResponseEntity.ok(hasTaken);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/student/{studentId}/average-score")
    public ResponseEntity<Double> getStudentAverageScore(@PathVariable Long studentId) {
        try {
            Double averageScore = resultService.getStudentAverageScore(studentId);
            return ResponseEntity.ok(averageScore);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/statistics/total")
    public ResponseEntity<Long> getTotalResultsCount() {
        try {
            Long count = resultService.getTotalResultsCount();
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/statistics/passed")
    public ResponseEntity<Long> getPassedResultsCount() {
        try {
            Long count = resultService.getPassedResultsCount();
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/statistics/failed")
    public ResponseEntity<Long> getFailedResultsCount() {
        try {
            Long count = resultService.getFailedResultsCount();
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

