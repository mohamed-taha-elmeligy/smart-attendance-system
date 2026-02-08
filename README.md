# 🎓 Smart Attendance System

A modern, reactive attendance management system built with **Spring Boot 3.5.6**, **WebFlux**, and **R2DBC** for academic institutions. This system uses QR codes for efficient attendance tracking, provides real-time notifications, and supports multiple universities with comprehensive reporting capabilities.

---

## 🌟 Key Features

### Core Functionality
- **QR Code-Based Attendance**: Generate dynamic QR codes for lectures with time-limited tokens for enhanced security
- **Real-Time Notifications**: Instant notifications to students and instructors about attendance events
- **Multi-University Support**: Manage multiple universities and academic institutions with isolated data
- **JWT Authentication**: Secure API endpoints with JSON Web Token-based authentication
- **Reactive Architecture**: Non-blocking, asynchronous processing for high-performance scalability

### Academic Management
- **Academic Members Management**: Handle students, instructors, and administrators with role-based access
- **Course Management**: Create and manage courses with instructor assignments
- **Lecture Scheduling**: Schedule lectures with room assignments and timing
- **Student Enrollment**: Manage student enrollment in courses
- **Attendance Tracking**: Real-time attendance marking with comprehensive audit trails

### Advanced Capabilities
- **Batch Processing**: Efficient bulk operations for attendance and enrollment
- **Caching Layer**: Caffeine cache for optimized database query performance
- **Database Migration**: Flyway-based schema versioning with support for PostgreSQL and H2
- **Comprehensive Validation**: Input validation for data integrity
- **Global Exception Handling**: Centralized error handling with meaningful error responses
- **Swagger/OpenAPI Documentation**: Interactive API documentation with SpringDoc OpenAPI

---

## 🏗️ Architecture

### Technology Stack

| Component | Technology | Version |
|-----------|-----------|---------|
| **Framework** | Spring Boot | 3.5.6 |
| **Web Layer** | Spring WebFlux | 3.5.6 |
| **Database** | Spring Data R2DBC | 3.5.6 |
| **RDBMS** | PostgreSQL (Primary) | 15+ |
| **Cache** | Caffeine | Latest |
| **Security** | JWT (JJWT) | 0.12.6 |
| **Validation** | Hibernate Validator | Latest |
| **Mapping** | MapStruct | 1.5.5 |
| **Database Migration** | Flyway | 10.18.0 |
| **Documentation** | SpringDoc OpenAPI | 2.7.0 |
| **Java Version** | Java | 21 |

### Project Structure

```
smart-attendance-system/
├── src/main/java/com/emts/smart_attendance_system/
│   ├── auditing/                 # JPA auditing configuration
│   ├── config/                   # Spring configurations
│   │   ├── R2dbcConfig.java
│   │   ├── SecurityConfig.java
│   │   ├── SwaggerConfig.java
│   │   └── RetryConfig.java
│   ├── controllers/              # REST API endpoints
│   │   ├── AuthController
│   │   ├── AttendanceController
│   │   ├── AcademicMemberController
│   │   └── ...
│   ├── services/                 # Business logic layer
│   ├── repositories/             # Data access layer (R2DBC)
│   ├── entities/                 # Domain models
│   ├── dtos/                      # Data Transfer Objects
│   │   ├── requests/
│   │   ├── responses/
│   │   └── mappers/              # MapStruct mappers
│   ├── security/                 # JWT & authentication
│   │   └── jwt/
│   ├── processors/               # Batch & data processors
│   ├── seeder/                   # Database seeders
│   ├── exceptions/               # Custom exceptions
│   ├── validation/               # Custom validators
│   └── utils/                    # Utility classes
│
├── src/main/resources/
│   ├── db/migration/
│   │   ├── postgre/              # PostgreSQL migration scripts
│   │   └── h2/                   # H2 migration scripts
│   ├── application.properties
│   ├── application-prod.properties
│   └── application-local.properties
│
└── pom.xml                       # Maven configuration
```

### Core Modules

**Controllers** (11 REST Controllers)
- AuthController - Authentication and login
- AttendanceController - Attendance marking and reporting
- AcademicMemberController - User management
- CourseController - Course management
- LectureController - Lecture scheduling
- QrCodeController - QR code generation
- EnrollmentController - Student enrollment
- NotificationController - Notification management
- AcademicYearController - Academic year configuration
- UniversityController - University management
- RoleController - Role-based access control

**Services** (11 Business Logic Services)
- AttendanceService - Attendance processing
- AcademicMemberService - User operations
- CourseService - Course operations
- LectureService - Lecture management
- QrCodeService - QR token generation
- AuthService - Authentication logic
- EnrollmentService - Enrollment management
- NotificationService - Notification dispatch
- And more...

**Data Layer** (R2DBC Repositories)
- Reactive data access using Spring Data R2DBC
- Custom query methods for complex operations
- Support for PostgreSQL and H2 databases

---

## 🚀 Getting Started

### Prerequisites
- Java 21 or higher
- Maven 3.8.1+
- PostgreSQL 13+ (or H2 for development)
- Git

### Installation & Setup

1. **Clone the Repository**
   ```bash
   git clone https://github.com/your-org/smart-attendance-system.git
   cd smart-attendance-system
   ```

2. **Configure Database**

   **For PostgreSQL (Production):**
   ```properties
   spring.r2dbc.url=r2dbc:postgresql://localhost:5432/attendance_db
   spring.r2dbc.username=postgres
   spring.r2dbc.password=your_password
   spring.flyway.enabled=true
   ```

   **For H2 (Development):**
   ```properties
   spring.r2dbc.url=r2dbc:h2:mem:testdb
   spring.r2dbc.driver-class-name=org.h2.driver.H2Driver
   ```

3. **Build the Project**
   ```bash
   mvn clean install
   ```

4. **Run the Application**
   ```bash
   # Development profile (default)
   mvn spring-boot:run

   # Production profile
   mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=production"
   ```

5. **Access the Application**
   - API Base URL: `http://localhost:8080`
   - Swagger UI: `http://localhost:8080/swagger-ui.html`
   - API Docs: `http://localhost:8080/v3/api-docs`

---

## 🔐 Authentication

### JWT Token Flow
1. **Login Endpoint**: `POST /api/auth/login`
   ```json
   {
     "username": "user@example.com",
     "password": "password123"
   }
   ```

2. **Response**
   ```json
   {
     "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
     "userInfo": {
       "memberId": 1,
       "username": "user@example.com",
       "roles": ["ROLE_STUDENT"]
     }
   }
   ```

3. **Secure Requests**: Include token in Authorization header
   ```
   Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
   ```

---

## 📊 Key API Endpoints

### Authentication
- `POST /api/auth/login` - User login

### Attendance
- `POST /api/attendance/mark` - Mark attendance for a lecture
- `GET /api/attendance/lectures/{lectureId}` - Get attendance list
- `GET /api/attendance/statistics/{studentId}` - Attendance statistics
- `POST /api/attendance/batch-mark` - Batch mark attendance

### QR Codes
- `POST /api/qr-codes/generate` - Generate QR code for lecture
- `GET /api/qr-codes/{qrCodeId}/validate` - Validate QR code token

### Lectures
- `POST /api/lectures` - Create lecture
- `GET /api/lectures/{id}` - Get lecture details
- `GET /api/lectures/course/{courseId}` - Get course lectures
- `PUT /api/lectures/{id}` - Update lecture

### Courses
- `POST /api/courses` - Create course
- `GET /api/courses` - List all courses
- `GET /api/courses/{id}` - Get course details

### Academic Members
- `POST /api/academic-members` - Create member
- `GET /api/academic-members/{id}` - Get member details
- `GET /api/academic-members/email/{email}` - Find by email

### Notifications
- `GET /api/notifications/member/{memberId}` - Get member notifications
- `GET /api/notifications/{id}` - Get notification details

---

## 🗄️ Database Schema

### Main Entities

**Universities** - Academic institution records
**Academic Years** - Semester/year definitions
**Roles** - User roles (ADMIN, INSTRUCTOR, STUDENT, STAFF)
**Academic Members** - Users (students, instructors, admin)
**Courses** - Course definitions
**Course Instructors** - Instructor assignments to courses
**Enrollments** - Student course enrollments
**Lectures** - Individual lecture sessions
**QR Codes** - QR tokens for attendance marking
**Attendance** - Attendance records
**Notifications** - System notifications

### Database Features
- Audit columns (createdAt, updatedAt, createdBy, updatedBy)
- Soft deletes support (where applicable)
- Referential integrity constraints
- Optimized indexes for query performance
- PostgreSQL-specific features (triggers, sequences)

---

## 🔄 Batch Processing

### Attendance Batch Processing
- Process multiple attendance records in a single request
- Returns detailed success/failure statistics
- Transaction rollback on critical errors

### Student Enrollment Batch Processing
- Bulk enroll students in courses
- Validate enrollment constraints
- Generate enrollment confirmations

---

## 🎯 Attendance Workflow

1. **Lecture Creation** - Instructor creates lecture with time & room
2. **QR Code Generation** - System generates secure QR code
3. **Attendance Marking** - Student scans QR code to mark attendance
4. **Validation** - System validates attendance eligibility
5. **Notification** - Real-time notification to instructor
6. **Reporting** - Comprehensive attendance reports

---

## 📈 Caching Strategy

**Caffeine Cache Implementation**
- User roles and permissions
- Academic year configurations
- Course information
- Lecture schedules
- QR code validations

**Cache Invalidation**
- Time-based expiration
- Manual invalidation on data updates
- Distributed cache considerations for clustering

---

## ✅ Validation & Error Handling

### Built-in Validators
- Attendance window validation
- Enrollment constraints
- Role-based access validation
- QR code token expiration

### Error Responses
All errors follow a consistent format:
```json
{
  "status": 400,
  "message": "Validation failed",
  "errors": {
    "email": "Invalid email format"
  },
  "timestamp": "2024-01-15T10:30:00Z"
}
```

---

## 🧪 Testing

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=AttendanceServiceTest

# Skip tests during build
mvn clean install -DskipTests
```

---

## 📝 Configuration Profiles

### Development Profile (Default)
```properties
spring.profiles.active=local
spring.r2dbc.url=r2dbc:h2:mem:testdb
logging.level.root=INFO
logging.level.com.emts=DEBUG
```

### Production Profile
```properties
spring.profiles.active=prod
spring.r2dbc.url=r2dbc:postgresql://prod-db:5432/attendance
logging.level.root=WARN
```

---

## 🔧 Maven Build Profiles

```bash
# Development build (default)
mvn clean install -Pdevelopment

# Production build (skip tests)
mvn clean install -Pproduction
```

---

## 📚 Database Migrations

**Flyway Migrations** are automatically applied on startup:
- Tables created with proper relationships
- Indexes added for optimization
- PostgreSQL-specific features (triggers for audit columns)
- Idempotent migrations for safe re-runs

**Location**: `src/main/resources/db/migration/`
- `postgre/` - PostgreSQL scripts
- `h2/` - H2 database scripts

---

## 🌐 Reactive Programming Benefits

- **Non-blocking I/O**: Efficient resource utilization
- **High Throughput**: Handle thousands of concurrent users
- **Scalability**: Minimal thread consumption
- **Back-pressure Handling**: Automatic flow control
- **Real-time Features**: WebSocket support for notifications

---

## 🔒 Security Features

- **JWT Authentication** - Stateless, scalable authentication
- **Role-Based Access Control (RBAC)** - Fine-grained permissions
- **Password Encoding** - BCrypt hashing
- **CORS Configuration** - API cross-origin access
- **Security Headers** - HTTPS, X-Frame-Options, CSP
- **Input Validation** - Protection against injection attacks

---

## 📊 Performance Optimizations

- **Query Optimization**: Indexed database queries
- **Caching Layer**: Caffeine cache reduces DB hits
- **Batch Operations**: Process multiple records efficiently
- **Connection Pooling**: R2DBC connection management
- **Pagination**: Large result set handling

---

## 🚨 Logging & Monitoring

**Structured Logging**
- Request/response logging
- Audit trail for attendance marking
- Exception stack traces in development

**Metrics to Monitor**
- Attendance marking latency
- QR code validation success rate
- Notification delivery rate
- Database connection pool usage

---

## 📱 Future Enhancements

- [ ] Mobile app integration (iOS/Android)
- [ ] Biometric attendance support
- [ ] Facial recognition for verification
- [ ] Advanced analytics and dashboards
- [ ] Email notifications with templates
- [ ] Calendar integration (Google, Outlook)
- [ ] REST API rate limiting
- [ ] Data export (PDF, Excel reports)
- [ ] Webhook support for integrations
- [ ] Multi-language support (i18n)

---

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

---

## 💬 Support & Contact

- **Issues**: Report bugs via GitHub Issues
- **Discussions**: Use GitHub Discussions for feature requests

---

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- R2DBC for reactive database access
- MapStruct for entity mapping
- Flyway for database migrations
- All contributors and users

---

**Built with ❤️ for modern education management**
