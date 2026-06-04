package com.onlinetest.config;

import com.onlinetest.model.Question;
import com.onlinetest.model.User;
import com.onlinetest.repository.QuestionRepository;
import com.onlinetest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) {

        // Seed sample questions only if table is empty
        if (questionRepository.count() == 0) {
            List<Question> questions = List.of(
                new Question(null, "What is the default port of a Spring Boot application?",
                    "8000", "8080", "9090", "80", "B", 1),
                new Question(null, "Which annotation is used to mark a class as a REST controller?",
                    "@Controller", "@Service", "@RestController", "@Repository", "C", 1),
                new Question(null, "What does JPA stand for?",
                    "Java Persistence API", "Java Programming API", "Java Protocol Application", "None", "A", 1),
                new Question(null, "Which HTTP method is used to create a resource in REST?",
                    "GET", "DELETE", "POST", "PUT", "C", 1),
                new Question(null, "What is MySQL?",
                    "A NoSQL Database", "A Relational Database", "A File System", "An OS", "B", 1),
                new Question(null, "What does CORS stand for?",
                    "Cross-Origin Resource Sharing", "Cross-Object Request Service", "Common Object REST Security", "None", "A", 1),
                new Question(null, "Which annotation is used for dependency injection in Spring?",
                    "@Inject", "@Component", "@Autowired", "@Bean", "C", 1),
                new Question(null, "What is the role of @Entity in Spring Boot?",
                    "Marks a REST Controller", "Marks a Database Table class", "Creates a REST API", "Starts the server", "B", 1),
                new Question(null, "Which tool is used to test REST APIs?",
                    "Eclipse", "Postman", "MySQL Workbench", "Git", "B", 1),
                new Question(null, "What is Spring Security used for?",
                    "Database connection", "API testing", "Authentication and Authorization", "Frontend rendering", "C", 1)
            );
            questionRepository.saveAll(questions);
            System.out.println("✅ Sample questions loaded.");
        }

        // Seed sample users only if table is empty
        if (userRepository.count() == 0) {
            List<User> users = List.of(
                new User(null, "Alice Johnson", "CS001", "password123", true),
                new User(null, "Bob Smith", "CS002", "password123", true),
                new User(null, "Charlie Kumar", "CS003", "password123", true)
            );
            userRepository.saveAll(users);
            System.out.println("✅ Sample users loaded.");
        }
    }
}
