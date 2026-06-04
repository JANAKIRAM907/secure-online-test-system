package com.onlinetest.controller;

import com.onlinetest.model.Question;
import com.onlinetest.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    /**
     * GET /api/questions
     * Returns all questions (correct answer is NOT sent to frontend)
     */
    @GetMapping("/questions")
    public ResponseEntity<?> getQuestions() {
        try {
            List<Question> questions = questionService.getAllQuestions();

            // Remove correct answers before sending to frontend (security)
            List<Map<String, Object>> safeQuestions = questions.stream().map(q -> {
                Map<String, Object> map = new java.util.LinkedHashMap<>();
                map.put("id", q.getId());
                map.put("question", q.getQuestion());
                map.put("optionA", q.getOptionA());
                map.put("optionB", q.getOptionB());
                map.put("optionC", q.getOptionC());
                map.put("optionD", q.getOptionD());
                // correctAnswer intentionally excluded
                return map;
            }).toList();

            return ResponseEntity.ok(safeQuestions);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("message", "Failed to fetch questions: " + e.getMessage()));
        }
    }

    /**
     * POST /api/admin/questions
     * Add a new question (admin only)
     */
    @PostMapping("/admin/questions")
    public ResponseEntity<?> addQuestion(@RequestBody Question question) {
        try {
            Question saved = questionService.addQuestion(question);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("message", "Failed to add question: " + e.getMessage()));
        }
    }

    /**
     * DELETE /api/admin/questions/{id}
     */
    @DeleteMapping("/admin/questions/{id}")
    public ResponseEntity<?> deleteQuestion(@PathVariable Long id) {
        try {
            questionService.deleteQuestion(id);
            return ResponseEntity.ok(Map.of("message", "Question deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("message", "Failed to delete question: " + e.getMessage()));
        }
    }
}
