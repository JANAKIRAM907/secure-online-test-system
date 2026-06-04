package com.onlinetest.service;

import com.onlinetest.model.Question;
import com.onlinetest.model.Result;
import com.onlinetest.model.SubmitRequest;
import com.onlinetest.model.User;
import com.onlinetest.repository.QuestionRepository;
import com.onlinetest.repository.ResultRepository;
import com.onlinetest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TestService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Core scoring logic:
     * - Fetch all questions
     * - Compare user answers with correct answers
     * - Calculate score
     * - Save result to database
     */
    public Map<String, Object> submitTest(SubmitRequest submitRequest) {

        // 1. Get user
        Optional<User> userOpt = userRepository.findById(submitRequest.getUserId());
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found with ID: " + submitRequest.getUserId());
        }
        User user = userOpt.get();

        // 2. Get all questions
        List<Question> allQuestions = questionRepository.findAll();

        // 3. Calculate score
        int score = 0;
        Map<Long, String> userAnswers = submitRequest.getAnswers();

        for (Question q : allQuestions) {
            String userAnswer = userAnswers.get(q.getId());
            if (userAnswer != null && userAnswer.equalsIgnoreCase(q.getCorrectAnswer())) {
                score += q.getMarks();
            }
        }

        // 4. Save result
        Result result = new Result();
        result.setUser(user);
        result.setScore(score);
        result.setTotalQuestions(allQuestions.size());
        result.setSubmissionType(submitRequest.getSubmissionType() != null
                ? submitRequest.getSubmissionType() : "MANUAL");
        result.setTabSwitchCount(submitRequest.getTabSwitchCount());
        resultRepository.save(result);

        // 5. Return result map
        Map<String, Object> response = new HashMap<>();
        response.put("userId", user.getId());
        response.put("name", user.getName());
        response.put("score", score);
        response.put("totalQuestions", allQuestions.size());
        response.put("submissionType", result.getSubmissionType());
        response.put("tabSwitchCount", result.getTabSwitchCount());
        response.put("submittedTime", result.getSubmittedTime());
        response.put("message", "Test submitted successfully");

        return response;
    }

    /**
     * Get the latest result for a user.
     */
    public Map<String, Object> getResult(Long userId) {
        Optional<Result> resultOpt = resultRepository.findTopByUserIdOrderBySubmittedTimeDesc(userId);

        if (resultOpt.isEmpty()) {
            throw new RuntimeException("No result found for user ID: " + userId);
        }

        Result result = resultOpt.get();
        Map<String, Object> response = new HashMap<>();
        response.put("name", result.getUser().getName());
        response.put("rollNumber", result.getUser().getRollNumber());
        response.put("score", result.getScore());
        response.put("totalQuestions", result.getTotalQuestions());
        response.put("submittedTime", result.getSubmittedTime());
        response.put("submissionType", result.getSubmissionType());
        response.put("tabSwitchCount", result.getTabSwitchCount());

        return response;
    }
}
