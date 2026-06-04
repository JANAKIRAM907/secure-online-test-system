package com.onlinetest.model;

import lombok.Data;

import java.util.Map;

@Data
public class SubmitRequest {
    private Long userId;
    private Map<Long, String> answers;   // questionId -> selected option ("A","B","C","D")
    private String submissionType;       // "MANUAL" or "AUTO"
    private int tabSwitchCount;
}
