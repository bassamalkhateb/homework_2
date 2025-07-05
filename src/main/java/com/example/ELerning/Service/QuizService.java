package com.example.ELerning.Service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ELerning.DOT.AnswerRequestDTO;
import com.example.ELerning.DOT.AnswerUpdateDTO;
import com.example.ELerning.DOT.QuestionRequestDTO;
import com.example.ELerning.DOT.QuizRequestDTO;
import com.example.ELerning.DOT.QuizUpdateDTO;
import com.example.ELerning.Entity.Answer;
import com.example.ELerning.Entity.Course;
import com.example.ELerning.Entity.Question;
import com.example.ELerning.Entity.Quiz;
import com.example.ELerning.Repository.AnswerRepository;
import com.example.ELerning.Repository.CourseRepository;
import com.example.ELerning.Repository.QuestionRepository;
import com.example.ELerning.Repository.QuizRepository;
import com.example.ELerning.exception.ResourceNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private CourseRepository courseRepository;
    private final ModelMapper modelmapper = new ModelMapper();

    // Quiz methods
    public List<Quiz> getAllQuizzes() {
        return quizRepository.findAll();
    }

    public Optional<Quiz> getQuizById(Long id) {
        return quizRepository.findById(id);
    }

    public List<Quiz> getQuizzesByCourse(Long courseId) {
        return quizRepository.findByCourseId(courseId);
    }

    public List<Quiz> getActiveQuizzesByCourse(Long courseId) {
        return quizRepository.findActiveByCourseId(courseId);
    }

    public List<Quiz> searchQuizzesByTitle(String title) {
        return quizRepository.findByTitleContaining(title);
    }

    @Transactional
    public Quiz createQuiz(QuizRequestDTO quizRequest) {
        Course course = courseRepository.findById(quizRequest.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("الدورة غير موجودة"));

        Quiz quiz = modelmapper.map(quizRequest, Quiz.class);
        quiz.setCourse(course);
        quiz.setCreatedAt(LocalDateTime.now());
        quiz.setUpdatedAt(LocalDateTime.now());
        quiz.setIsActive(true);

        // تعيين قيم افتراضية إذا لم يتم توفيرها
        if (quiz.getPassingScore() == null) {
            quiz.setPassingScore(70); // قيمة افتراضية
        }

        if (quiz.getTimeLimit() == null) {
            quiz.setTimeLimit(30); // 30 دقيقة افتراضياً
        }

        return quizRepository.save(quiz);
    }

    @Transactional
    public Quiz updateQuiz(Long id, QuizUpdateDTO quizUpdateDTO) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("الاختبار غير موجود"));

        // التحقق من أن الدورة ما زالت موجودة
        if (!courseRepository.existsById(quiz.getCourse().getId())) {
            throw new ResourceNotFoundException("الدورة المرتبطة غير موجودة");
        }

        // التحديث الجزئي (Patch-like behavior)
        if (quizUpdateDTO.getTitle() != null) {
            quiz.setTitle(quizUpdateDTO.getTitle());
        }
        if (quizUpdateDTO.getDescription() != null) {
            quiz.setDescription(quizUpdateDTO.getDescription());
        }
        if (quizUpdateDTO.getTimeLimit() != null) {
            quiz.setTimeLimit(quizUpdateDTO.getTimeLimit());
        }
        if (quizUpdateDTO.getPassingScore() != null) {
            quiz.setPassingScore(quizUpdateDTO.getPassingScore());
        }

        quiz.setUpdatedAt(LocalDateTime.now());

        return quizRepository.save(quiz);
    }

    public Quiz activateQuiz(Long id) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("الاختبار غير موجود"));

        quiz.setIsActive(true);
        quiz.setUpdatedAt(LocalDateTime.now());

        return quizRepository.save(quiz);
    }

    public Quiz deactivateQuiz(Long id) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("الاختبار غير موجود"));

        quiz.setIsActive(false);
        quiz.setUpdatedAt(LocalDateTime.now());

        return quizRepository.save(quiz);
    }

    public void deleteQuiz(Long id) {
        if (!quizRepository.existsById(id)) {
            throw new RuntimeException("الاختبار غير موجود");
        }
        quizRepository.deleteById(id);
    }

    // Question methods
    public List<Question> getQuestionsByQuiz(Long quizId) {
        return questionRepository.findByQuizIdOrderById(quizId);
    }

    public Optional<Question> getQuestionById(Long id) {
        return questionRepository.findById(id);
    }

    @Transactional
    public Question createQuestion(QuestionRequestDTO questionRequest) {
        Quiz quiz = quizRepository.findById(questionRequest.getQuizId())
                .orElseThrow(() -> new ResourceNotFoundException("الاختبار غير موجود"));

        Question question = new Question();
        question.setQuestionText(questionRequest.getQuestionText());
        question.setType(questionRequest.getQuestionType());
        question.setPoints(questionRequest.getPoints());
        question.setQuiz(quiz);

        return questionRepository.save(question);
    }

    public Question updateQuestion(Long id, Question questionDetails) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("السؤال غير موجود"));

        question.setQuestionText(questionDetails.getQuestionText());
        question.setType(questionDetails.getType());
        question.setPoints(questionDetails.getPoints());

        return questionRepository.save(question);
    }

    public void deleteQuestion(Long id) {
        if (!questionRepository.existsById(id)) {
            throw new RuntimeException("السؤال غير موجود");
        }
        questionRepository.deleteById(id);
    }

    // Answer methods
    public List<Answer> getAnswersByQuestion(Long questionId) {
        return answerRepository.findByQuestionIdOrderById(questionId);
    }

    public List<Answer> getCorrectAnswersByQuestion(Long questionId) {
        return answerRepository.findCorrectAnswersByQuestionId(questionId);
    }

    public Optional<Answer> getAnswerById(Long id) {
        return answerRepository.findById(id);
    }

    @Transactional
    public Answer createAnswer(AnswerRequestDTO answerRequest) {
        Question question = questionRepository.findById(answerRequest.getQuestionId())
                .orElseThrow(() -> new ResourceNotFoundException("السؤال غير موجود"));

        Answer answer = new Answer();
        answer.setAnswerText(answerRequest.getAnswerText());
        answer.setIsCorrect(answerRequest.getIsCorrect());
        answer.setQuestion(question);

        return answerRepository.save(answer);
    }

    @Transactional
    public Answer updateAnswer(Long id, AnswerUpdateDTO answerUpdateDTO) {
        Answer answer = answerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("الإجابة غير موجودة"));

        answer.setAnswerText(answerUpdateDTO.getAnswerText());
        answer.setIsCorrect(answerUpdateDTO.getIsCorrect());

        return answerRepository.save(answer);
    }

    public void deleteAnswer(Long id) {
        if (!answerRepository.existsById(id)) {
            throw new RuntimeException("الإجابة غير موجودة");
        }
        answerRepository.deleteById(id);
    }

    // Utility methods
    public Long getQuestionCount(Long quizId) {
        return quizRepository.countQuestionsByQuizId(quizId);
    }

    public Integer getTotalPoints(Long quizId) {
        Integer totalPoints = questionRepository.getTotalPointsByQuizId(quizId);
        return totalPoints != null ? totalPoints : 0;
    }

    public boolean canUserAccessQuiz(Long userId, Long quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("الاختبار غير موجود"));

        return quiz.getIsActive() && quiz.getCourse().getStatus() == Course.Status.APPROVED;
    }
}
