package com.onlinetest.service;

import com.onlinetest.model.Question;
import com.onlinetest.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    /**
     * Get all questions for the test.
     */
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    /**
     * Get random set of questions (for randomized tests).
     */
    public List<Question> getRandomQuestions(int count) {
        return questionRepository.findRandomQuestions(count);
    }

    /**
     * Add a new question (admin use).
     */
    public Question addQuestion(Question question) {
        return questionRepository.save(question);
    }

    /**
     * Delete a question by ID.
     */
    public void deleteQuestion(Long id) {
        questionRepository.deleteById(id);
    }
}
