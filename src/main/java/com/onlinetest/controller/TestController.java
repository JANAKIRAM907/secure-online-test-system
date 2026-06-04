package com.onlinetest.controller;

import com.onlinetest.model.SubmitRequest;
import com.onlinetest.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class TestController {

    @Autowired
    private TestService testService;

    /**
     * POST /api/submit
     * Body: {
     *   "userId": 1,
     *   "answers": { "1": "A", "2": "C", "3": "B" },
     *   "submissionType": "MANUAL",
     *   "tabSwitchCount": 0
     * }
     */
    @PostMapping("/submit")
    public ResponseEntity<?> submitTest(@RequestBody SubmitRequest submitRequest) {
        try {
            Map<String, Object> result = testService.submitTest(submitRequest);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("message", "Submission failed: " + e.getMessage()));
        }
    }

    /**
     * GET /api/result?userId=1
     * Returns the latest result for the given user
     */
    @GetMapping("/result")
    public ResponseEntity<?> getResult(@RequestParam Long userId) {
        try {
            Map<String, Object> result = testService.getResult(userId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("message", "Failed to fetch result: " + e.getMessage()));
        }
    }
}
