# 🔒 Secure Online Test System

A full-stack web-based examination platform built with **Java Spring Boot** and **React.js**, featuring anti-cheating detection, auto-submission on tab switching, and real-time score calculation.

---

## 📌 Features

- 🔐 **User Login** — Name + Roll Number authentication
- 📋 **MCQ Test** — Dynamic question loading from MySQL
- 🚨 **Tab-Switch Detection** — Auto-submits test after 3 suspicious tab switches
- ⏱️ **Countdown Timer** — Auto-submits when time runs out
- 📊 **Instant Score** — Score calculated and displayed immediately
- 🗄️ **MySQL Storage** — All users, questions, and results stored securely

---

## 🛠️ Tech Stack

| Layer    | Technology                            |
|----------|---------------------------------------|
| Backend  | Java 17, Spring Boot 3, Spring Security, Spring Data JPA |
| Database | MySQL 8                               |
| Frontend | React.js 18, React Router, Axios      |
| Tools    | Maven, Postman, Git, STS              |

---

## 📁 Project Structure

```
secure-online-test-system/
├── src/
│   └── main/
│       └── java/com/onlinetest/
│           ├── SecureOnlineTestSystemApplication.java
│           ├── controller/
│           │   ├── AuthController.java
│           │   ├── QuestionController.java
│           │   └── TestController.java
│           ├── service/
│           │   ├── AuthService.java
│           │   ├── QuestionService.java
│           │   └── TestService.java
│           ├── repository/
│           │   ├── UserRepository.java
│           │   ├── QuestionRepository.java
│           │   └── ResultRepository.java
│           ├── model/
│           │   ├── User.java
│           │   ├── Question.java
│           │   ├── Result.java
│           │   ├── LoginRequest.java
│           │   └── SubmitRequest.java
│           └── config/
│               ├── SecurityConfig.java
│               └── DataInitializer.java
│   └── resources/
│       └── application.properties
├── frontend/
│   ├── src/
│   │   ├── pages/
│   │   │   ├── LoginPage.js
│   │   │   ├── TestPage.js
│   │   │   └── ResultPage.js
│   │   ├── services/
│   │   │   └── api.js
│   │   ├── App.js
│   │   └── index.js
│   └── package.json
├── pom.xml
└── README.md
```

---

## ⚙️ Setup & Run Locally

### Prerequisites
- Java 17+
- Maven 3.8+
- MySQL 8+
- Node.js 18+
- Spring Tool Suite (STS) or IntelliJ

### 1. Clone the repo
```bash
git clone https://github.com/YOUR_USERNAME/secure-online-test-system.git
cd secure-online-test-system
```

### 2. Configure MySQL
Create database (auto-created on first run):
```sql
CREATE DATABASE online_test_db;
```
Update `src/main/resources/application.properties`:
```properties
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
```

### 3. Run Spring Boot Backend
```bash
mvn spring-boot:run
```
Backend starts at: `http://localhost:8080`

### 4. Run React Frontend
```bash
cd frontend
npm install
npm start
```
Frontend starts at: `http://localhost:3000`

---

## 🔌 REST API Endpoints

| Method | Endpoint              | Description                  |
|--------|-----------------------|------------------------------|
| POST   | `/api/login`          | User login                   |
| GET    | `/api/questions`      | Fetch all questions           |
| POST   | `/api/submit`         | Submit test answers           |
| GET    | `/api/result?userId=` | Get result for user           |
| GET    | `/api/health`         | Health check                  |

---

## 🧪 Test with Postman

### Login
```json
POST http://localhost:8080/api/login
{
  "name": "Alice Johnson",
  "rollNumber": "CS001"
}
```

### Get Questions
```
GET http://localhost:8080/api/questions
```

### Submit Test
```json
POST http://localhost:8080/api/submit
{
  "userId": 1,
  "answers": { "1": "B", "2": "C", "3": "A" },
  "submissionType": "MANUAL",
  "tabSwitchCount": 0
}
```

### Get Result
```
GET http://localhost:8080/api/result?userId=1
```

---

## 🗄️ Database Schema

```sql
-- Users Table
CREATE TABLE users (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  roll_number VARCHAR(50) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  is_active BOOLEAN DEFAULT TRUE
);

-- Questions Table
CREATE TABLE questions (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  question TEXT NOT NULL,
  option_a VARCHAR(255),
  option_b VARCHAR(255),
  option_c VARCHAR(255),
  option_d VARCHAR(255),
  correct_answer VARCHAR(1),
  marks INT DEFAULT 1
);

-- Results Table
CREATE TABLE results (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT,
  score INT,
  total_questions INT,
  submitted_time DATETIME,
  submission_type VARCHAR(20),
  tab_switch_count INT DEFAULT 0,
  FOREIGN KEY (user_id) REFERENCES users(id)
);
```

---

## 🌿 Git Branches

| Branch      | Purpose                          |
|-------------|----------------------------------|
| `main`      | Stable production-ready code     |
| `develop`   | Active development branch        |
| `feature/*` | Individual feature branches      |

---

## 🚀 Deployment (Render / Railway)

### Backend (Spring Boot JAR)
```bash
mvn clean package
# Deploy target/secure-online-test-system-1.0.0.jar
```

### Frontend (React Build)
```bash
cd frontend
npm run build
# Deploy the build/ folder to Netlify or Vercel
```

---

## 👨‍💻 Developer

Built by: **[Your Name]**  
Tech Stack: Java | Spring Boot | React.js | MySQL | Spring Security

---

## 📜 License
MIT License
