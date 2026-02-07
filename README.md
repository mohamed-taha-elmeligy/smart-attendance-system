🎓 Smart Attendance System

Reactive Backend – Java / Spring Boot

A production-ready, reactive backend system built with Spring Boot WebFlux to manage student attendance efficiently.
The project focuses on scalability, performance, and clean architecture, and is designed as a backend service suitable for enterprise or academic environments.

🎯 Purpose

This project demonstrates:

Designing a reactive backend using non-blocking I/O

Applying clean architecture principles

Implementing secure authentication & authorization

Making intentional technical trade-offs instead of default choices

🧠 Key Engineering Decisions
Why Spring WebFlux (Reactive)?

Handles high concurrency with fewer resources

Non-blocking request processing

Better scalability under load compared to traditional MVC

Why PostgreSQL + R2DBC?

Strong relational consistency for attendance data

Reactive database access without blocking threads

Clear schema & data integrity (unlike schema-less approaches)

Why JWT Authentication?

Stateless authentication

Easy horizontal scaling

Suitable for APIs and microservice-style architectures

✨ Core Features

Student Management

Attendance Tracking (Present / Absent / Late)

Attendance Reports

JWT-based Authentication

Role-Based Access Control (Admin / Teacher / Student)

Email Notifications

Caching for Performance Optimization

Reactive, Non-blocking API Design

🏗️ Architecture Overview
Client
  ↓
Controller (Reactive REST API)
  ↓
Service (Business Logic)
  ↓
Repository (R2DBC)
  ↓
PostgreSQL

Responsibilities

Controller: Reactive endpoints & request validation

Service: Business rules and orchestration

Repository: Reactive database access

Security: JWT filter chain & role enforcement

🛠️ Tech Stack
Category	Technology
Language	Java 21
Framework	Spring Boot 3
Web	Spring WebFlux
Database	PostgreSQL
Reactive DB	Spring Data R2DBC
Security	Spring Security + JWT
Migration	Flyway
Caching	Caffeine
API Docs	Swagger / OpenAPI
Build Tool	Maven
🔐 Security Highlights

BCrypt password hashing

JWT token validation

Role-based authorization

Secure headers & CORS configuration

🗄️ Database Management

Flyway for versioned schema migrations

Clear separation between schema evolution and application logic

Automatic migration execution on startup

⚡ Performance Considerations

Non-blocking request handling

Local caching with Caffeine for frequently accessed data

Reduced thread usage under high load

🧪 Testing

Unit tests for services

Integration tests for API endpoints

Reactive testing using WebTestClient

mvn test

📚 API Documentation

Once running:

Swagger UI

http://localhost:8080/api/v1/swagger-ui.html

🚀 Running the Application
mvn spring-boot:run


Application base URL:

http://localhost:8080/api/v1

📌 What This Project Demonstrates

Strong understanding of backend architecture

Practical use of reactive programming

Conscious technology trade-offs

Clean, maintainable, and scalable codebase

📄 License

MIT License
