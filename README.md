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

```bash
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Review case documents",
    "description": "Review evidence for case HMCTS-2024-001",
    "dueDateTime": "2024-12-25T14:30:00"
  }'
```
Update task status (mark complete):

``` bash
curl -X PATCH http://localhost:8080/api/tasks/1 \
  -H "Content-Type: application/json" \
  -d '{"status": "COMPLETED"}'
```
Get all tasks:

``` bash
curl http://localhost:8080/api/tasks
Delete a task:

curl -X DELETE http://localhost:8080/api/tasks/1
```
## Database Schema
Task Table

Column	Type	Constraints	Description

id	BIGINT	PRIMARY KEY, AUTO_INCREMENT	Unique identifier

title	VARCHAR(200)	NOT NULL	Task title

description	VARCHAR(2000)		Detailed description

status	VARCHAR(50)	NOT NULL	PENDING, IN_PROGRESS, COMPLETED

due_date_time	TIMESTAMP	NOT NULL	Deadline for task

created_at	TIMESTAMP	NOT NULL, AUTO	Creation timestamp

## Testing
Run all tests:

``` bash
mvn test
```
### Test coverage includes:

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



