# HMCTS Task Manager

A full-stack task management application for HMCTS caseworkers, built with Spring Boot and H2 database.

## Set up

### Prerequisites
- Java 21
- Maven 3.8+
- Spring Boot 4.0.6

### Running the Application

```bash
# Clone and navigate to project
cd hmcts-task-manager

# Run the application
mvn spring-boot:run
```

## Access Points
Component	URL
Frontend UI	http://localhost:8080
API Base	http://localhost:8080/api/tasks
H2 Database Console	http://localhost:8080/h2-console
H2 Console Login:

JDBC URL: jdbc:h2:file:./data/taskdb

Username: sa

Password: (leave empty)

## API Endpoints
Method	Endpoint	Description
POST	/api/tasks	Create a new task
GET	/api/tasks	Get all tasks
GET	/api/tasks/{id}	Get task by ID
PATCH	/api/tasks/{id}	Update specific fields
DELETE	/api/tasks/{id}	Delete a task

## Example Requests
Create a task:

bash
```
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Review case documents",
    "description": "Review evidence for case HMCTS-2024-001",
    "dueDateTime": "2024-12-25T14:30:00"
  }'
```
Update task status (mark complete):

bash
```
curl -X PATCH http://localhost:8080/api/tasks/1 \
  -H "Content-Type: application/json" \
  -d '{"status": "COMPLETED"}'
```
Get all tasks:

bash
curl http://localhost:8080/api/tasks
Delete a task:

bash
curl -X DELETE http://localhost:8080/api/tasks/1

Database Schema
Task Table
Column	Type	Constraints	Description
id	BIGINT	PRIMARY KEY, AUTO_INCREMENT	Unique identifier
title	VARCHAR(200)	NOT NULL	Task title
description	VARCHAR(2000)		Detailed description
status	VARCHAR(50)	NOT NULL	PENDING, IN_PROGRESS, COMPLETED
due_date_time	TIMESTAMP	NOT NULL	Deadline for task
created_at	TIMESTAMP	NOT NULL, AUTO	Creation timestamp
Status Flow
text
PENDING → IN_PROGRESS → COMPLETED

Testing
Run all tests:

bash
mvn test
Test coverage includes:

Service layer unit tests (Mockito)

Controller integration tests (@WebMvcTest)

Repository tests (@DataJpaTest)

## Tech Stack
Layer	Technology
Backend	Spring Boot 3.2.5
Database	H2 (file-based)
ORM	Spring Data JPA
Frontend	HTML/CSS/JavaScript
Build Tool	Maven
Java Version	21

## Configuration
Key settings in application.properties:

properties
# Server
server.port=8080

# Database (H2)
spring.datasource.url=jdbc:h2:file:./data/taskdb
spring.datasource.username=sa
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
## Validation Rules
Field	Rule
title	Required, max 200 characters
dueDateTime	Required, cannot be in the past
status	Must be PENDING, IN_PROGRESS, or COMPLETED
description	Optional, max 2000 characters

## Troubleshooting
H2 Console shows 404:

Verify spring.h2.console.enabled=true in properties

Access exact URL: /h2-console (not /h2)

Database connection fails:

Use JDBC URL: jdbc:h2:file:./data/taskdb

Delete ./data folder and restart app

Port 8080 already in use:

properties
# Change in application.properties
server.port=8081
📝 Notes for Assessors
No external dependencies - H2 database is file-based, no setup needed

RESTful API - Follows standard HTTP methods and status codes

Partial updates - PATCH endpoint allows updating specific fields

Data persistence - Tasks survive application restarts (file-based H2)

Frontend - Static files served directly by Spring Boot

👨‍💻 Author
HMCTS Technical Test Submission

📄 License
This project is for assessment purposes only.

text

## How to Use:

1. Create a new file in your project root called `README.md`
2. Copy ALL of the above text into it
3. Save the file

## Optional: Even Shorter Version

If you want a more concise README, use this instead:

```markdown
# HMCTS Task Manager

Spring Boot task management app for HMCTS caseworkers.

## Quick Start

```bash
mvn spring-boot:run
Then visit: http://localhost:8080

API Endpoints
Method	Endpoint	Description
POST	/api/tasks	Create task
GET	/api/tasks	List all tasks
GET	/api/tasks/{id}	Get one task
PATCH	/api/tasks/{id}	Update task fields
DELETE	/api/tasks/{id}	Delete task
Example: Mark task complete
bash
curl -X PATCH http://localhost:8080/api/tasks/1 \
  -H "Content-Type: application/json" \
  -d '{"status":"COMPLETED"}'
Database Schema
Task table:

id (BIGINT, PK) - Auto-generated

title (VARCHAR(200), NOT NULL)

description (VARCHAR(2000))

status (VARCHAR(50), NOT NULL) - PENDING/IN_PROGRESS/COMPLETED

due_date_time (TIMESTAMP, NOT NULL)

created_at (TIMESTAMP, NOT NULL)

H2 Console: http://localhost:8080/h2-console

JDBC URL: jdbc:h2:file:./data/taskdb

Username: sa, Password: (empty)

Tech Stack
Java 21 / Spring Boot 3.2.5

H2 Database (file-based persistence)

Spring Data JPA

HTML/CSS/JavaScript frontend

Maven

Testing
bash
mvn test
Configuration
Data is stored in ./data/taskdb.mv.db - survives app restarts. No external database setup required.
