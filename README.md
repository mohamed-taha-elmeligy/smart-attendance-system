# Smart Attendance System

A QR-based attendance system built for universities. Instead of paper sign-ins or calling out names, students scan a QR code when they walk into class — and that's it, they're marked present.

I built this because the traditional way of tracking attendance is slow, error-prone, and honestly a bit outdated. This system handles the whole flow: from creating a lecture, generating a secure QR code, to tracking who showed up and sending out notifications.

---

## What it does

- **QR Code Attendance** — Each lecture gets a unique, time-limited QR code. Students scan it, system records their attendance instantly.
- **Multi-University Support** — The system can manage more than one university, each with its own isolated data.
- **Role-Based Access** — Students, instructors, admins, and staff each have their own permissions and views.
- **Real-Time Notifications** — Instructors get notified when students mark their attendance.
- **Attendance Reports** — Track who attended, who didn't, and generate stats per student or per course.
- **Batch Processing** — Need to enroll 200 students or process attendance in bulk? Handled.

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Framework | Spring Boot 3 |
| Web | Spring WebFlux (Reactive) |
| Database | PostgreSQL + R2DBC |
| Security | JWT + RBAC |
| Cache | Caffeine |
| Migrations | Flyway |
| Mapping | MapStruct |
| Docs | Swagger / OpenAPI |
| Language | Java 21 |

---

## Why Reactive?

Universities can have thousands of students scanning QR codes at the same time — especially between classes. Spring WebFlux handles this without spinning up a thread for every request, which means the system stays fast under real load.

---

## Project Structure

```
src/main/java/
├── controllers/      # 11 REST controllers
├── services/         # Business logic
├── repositories/     # R2DBC data access
├── entities/         # Domain models
├── dtos/             # Request/Response objects + MapStruct mappers
├── security/         # JWT logic
├── processors/       # Batch operations
├── exceptions/       # Global error handling
└── validation/       # Custom validators
```

---

## API Highlights

The system exposes a full REST API. A few key endpoints:

```
POST /api/auth/login
POST /api/attendance/mark
POST /api/qr-codes/generate
GET  /api/attendance/statistics/{studentId}
POST /api/attendance/batch-mark
```

Full docs available at `/swagger-ui.html` after running the project.

---

## Running Locally

**Requirements:** Java 21, Maven, PostgreSQL

```bash
git clone https://github.com/your-repo/smart-attendance-system.git
cd smart-attendance-system
```

Set up your DB connection in `application.properties`:

```properties
spring.r2dbc.url=r2dbc:postgresql://localhost:5432/attendance_db
spring.r2dbc.username=your_username
spring.r2dbc.password=your_password
```

Then run:

```bash
mvn spring-boot:run
```

App runs on `http://localhost:8080`

---

## What I'd add next

- Email notifications with proper templates
- PDF/Excel export for attendance reports
- Rate limiting on the API
- Mobile app integration

---

Built by [Mohamed Taha](https://linkedin.com/in/mtelmeligy-backend-dev) — open to internship and junior backend opportunities.
