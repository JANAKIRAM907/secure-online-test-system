package com.onlinetest.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "results")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private int score;

    @Column(name = "total_questions")
    private int totalQuestions;

    @Column(name = "submitted_time")
    private LocalDateTime submittedTime;

    @Column(name = "submission_type")
    private String submissionType;  // "MANUAL" or "AUTO" (tab switch detected)

    @Column(name = "tab_switch_count")
    private int tabSwitchCount = 0;

    @PrePersist
    protected void onCreate() {
        submittedTime = LocalDateTime.now();
    }
}
