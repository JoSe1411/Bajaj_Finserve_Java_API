# Bajaj Finserv Health - Qualifier 1 (JAVA)

## 📋 Overview

This Spring Boot application automatically solves the Bajaj Finserv Health Qualifier 1 assignment. It runs the complete flow on startup without any manual controller invocation.

## 🚀 Features

- **Automatic Execution**: Runs on application startup
- **Webhook Generation**: Sends POST request to generate webhook
- **JWT Authentication**: Uses access token for API authorization
- **SQL Solution**: Solves Question 2 (for even registration numbers)

## 📁 Project Structure

```
bajaj-finserv-qualifier/
├── pom.xml
├── README.md
├── src/
│   ├── main/
│   │   ├── java/com/bajaj/qualifier/
│   │   │   ├── BajajQualifierApplication.java    # Main class
│   │   │   ├── config/
│   │   │   │   └── WebClientConfig.java          # WebClient configuration
│   │   │   ├── dto/
│   │   │   │   ├── WebhookRequest.java           # Request DTO
│   │   │   │   ├── WebhookResponse.java          # Response DTO
│   │   │   │   └── SqlSubmissionRequest.java     # Submission DTO
│   │   │   ├── service/
│   │   │   │   └── WebhookService.java           # Core business logic
│   │   │   └── runner/
│   │   │       └── StartupRunner.java            # Startup execution
│   │   └── resources/
│   │       └── application.properties
│   └── test/
└── target/
    └── bajaj-qualifier-1.0.0.jar                 # Executable JAR
```

## ⚙️ Setup Instructions

### Prerequisites

- Java 17 or higher
- Maven 3.6+

### Configuration

**IMPORTANT**: Before running, update `application.properties` with your details:

```properties
# User Details - UPDATE THESE WITH YOUR ACTUAL DETAILS
app.user.name=Your Full Name
app.user.regNo=YourRegistrationNumber
app.user.email=your.email@example.com
```

### Build Commands

```bash
# Navigate to project directory
cd bajaj-finserv-qualifier

# Clean and build
mvn clean package -DskipTests

# The JAR will be created at: target/bajaj-qualifier-1.0.0.jar
```

### Run the Application

```bash
# Run using Maven
mvn spring-boot:run

# OR run the JAR directly
java -jar target/bajaj-qualifier-1.0.0.jar
```

## 📊 SQL Query Solution

### Question 2 Problem Statement

For every department, calculate the average age of individuals with salaries exceeding ₹70,000, and produce a concatenated string containing at most 10 of their names.

### Final SQL Query

```sql
SELECT 
    d.DEPARTMENT_NAME, 
    AVG(TIMESTAMPDIFF(YEAR, e.DOB, CURDATE())) AS AVERAGE_AGE, 
    SUBSTRING_INDEX(
        GROUP_CONCAT(
            CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) 
            ORDER BY e.EMP_ID 
            SEPARATOR ', '
        ), 
        ', ', 
        10
    ) AS EMPLOYEE_LIST 
FROM EMPLOYEE e 
JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID 
JOIN PAYMENTS p ON e.EMP_ID = p.EMP_ID 
WHERE p.AMOUNT > 70000 
GROUP BY d.DEPARTMENT_ID, d.DEPARTMENT_NAME 
ORDER BY d.DEPARTMENT_ID DESC
```

### Output Columns

| Column | Description |
|--------|-------------|
| `DEPARTMENT_NAME` | Name of the department |
| `AVERAGE_AGE` | Average age of employees earning > ₹70,000 |
| `EMPLOYEE_LIST` | Comma-separated list of up to 10 employee names |

## 🔄 Application Flow

```
┌─────────────────────────────────────────────────────┐
│              APPLICATION STARTUP                     │
└─────────────────────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────┐
│  1. StartupRunner.run() executes automatically       │
└─────────────────────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────┐
│  2. POST /hiring/generateWebhook/JAVA                │
│     Body: { name, regNo, email }                     │
└─────────────────────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────┐
│  3. Receive: { webhook, accessToken }                │
└─────────────────────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────┐
│  4. POST to webhook URL                              │
│     Headers: Authorization: <accessToken>            │
│     Body: { finalQuery: "SQL_QUERY" }                │
└─────────────────────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────┐
│              SUCCESS / COMPLETION                    │
└─────────────────────────────────────────────────────┘
```

## 📦 JAR Download

The compiled JAR file is available at:
- **Path**: `target/bajaj-qualifier-1.0.0.jar`
- **GitHub Raw Link**: `https://github.com/YOUR_USERNAME/YOUR_REPO/raw/main/target/bajaj-qualifier-1.0.0.jar`

## 🛠️ Technologies Used

- **Spring Boot 3.2.0**
- **Java 17**
- **WebClient** (Reactive HTTP Client)
- **Lombok** (Boilerplate reduction)
- **Maven** (Build tool)

## 📝 Submission Checklist

- [x] Spring Boot application with automatic startup execution
- [x] POST request to generateWebhook on startup
- [x] JWT token used in Authorization header
- [x] SQL query for Question 2 implemented
- [x] No manual controller invocation required
- [x] Executable JAR file included
- [x] README documentation

## 👤 Author

Bajaj Finserv Health - Qualifier 1 Submission

## 📄 License

This project is created for the Bajaj Finserv Health Qualifier 1 assessment.

