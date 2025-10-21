# Copilot Instructions for Comitte Service

## Project Overview
This is a Spring Boot 3.5.6 committee management REST API using PostgreSQL, JWT authentication, and MapStruct mapping. It manages rotating savings groups (committees/chit funds) where members contribute monthly and bid for payouts.

## Core Architecture

### Domain Model
- **Members**: User accounts with authentication, stored in `members` table
- **Committees (Comitte)**: Savings groups with owner, amount, member count, payment terms
- **ComitteMemberMap**: Join table tracking member participation and share counts
- **Bids**: Member bids for committee payouts with amounts and status

### Package Structure
```
com.ls/
├── auth/           # Authentication & user management
│   ├── controller/ # AuthController, PasswordResetController, UserController
│   ├── model/      # DTOs (request/response), entities (Member, Role, Authority)
│   ├── security/   # SecurityConfig with JWT & CORS
│   └── service/    # AuthService, ForgetPasswordService
└── comitte/        # Core business domain
    ├── controller/ # REST endpoints for committees, members, bids
    ├── model/      # Business entities and DTOs
    ├── service/    # Business logic layer
    └── util/       # ResponseMapper (MapStruct interface)
```

## Development Patterns

### Entity Mapping
- Use `ResponseMapper.INSTANCE` (MapStruct) for entity ↔ DTO conversion
- All entities extend base audit fields: `createdTimestamp`, `updatedTimestamp`
- Enable JPA auditing with `@EnableJpaAuditing` in main class

### API Design
- REST endpoints follow `/api/{domain}` pattern
- Controllers have comprehensive JavaDoc with security notes and best practices
- Use `@Valid` for request validation, delegate business logic to services
- Standard HTTP status codes: 201 for creation, 200 for success, 4xx for client errors

### Security Configuration
- JWT-based stateless authentication with CORS for frontend integration
- Public endpoints: `/swagger-ui/**`, `/api-docs/**`, `/api/auth/**`, `/api/password/**`
- All committee/member endpoints currently permitAll() - implement authorization as needed
- Admin endpoints require ROLE_ADMIN: `/api/admin/**`

### Exception Handling
- Global `@ControllerAdvice` in `ApiExceptionHandler`
- All errors include unique `errorId` UUID for correlation
- Use `ResponseStatusException` for controlled HTTP status responses
- Security: Never expose stack traces or sensitive data to clients

## Key Development Commands

```bash
# Run application (port 8082)
mvn spring-boot:run

# Database reset (creates fresh schema with sample data)
# Set spring.jpa.hibernate.ddl-auto: create-drop in application.yml

# Access Swagger UI
http://localhost:8082/swagger-ui.html
```

## Configuration Essentials

### Database (`application.yml`)
- PostgreSQL connection: `localhost:5432/comittedb`
- Sample data loaded from `src/main/resources/data.sql`
- DDL mode: `create-drop` (dev) vs `validate` (prod)

### Authentication
- JWT secret configured in application.yml
- Token expiration: 100 hours (360000000ms)
- BCrypt password encoding
- Email service configured for password reset

### Testing Credentials
All test users have password: `test123` (BCrypt: `$2a$10$ix4H8Tvaga./6cYdkpZCxuzzXU3I62ahG2tBPI04PUkfW7qOzSWim`)
- `bippan.khichra` / `bippan.k@example.com`
- `vikas.rajaura` / `vikas.r@example.com`
- Additional test users in `data.sql`

## Code Conventions

### Annotations
- Lombok: `@Data`, `@Builder`, `@RequiredArgsConstructor` for entities/DTOs  
- Validation: `@Valid` on controller parameters, Jakarta validation on fields
- JPA: `@Entity`, `@Table(name="...")`, `@JoinTable` for relationships
- Logging: `@Slf4j` for consistent logging across controllers/services

### Error Handling
- Use `ResponseStatusException` for business errors with specific HTTP codes
- Log errors with errorId: `log.error("Error ID: {}, Message: {}", errorId, ex.getMessage(), ex)`
- Validation errors automatically handled by Spring Boot validation

### Security Notes
- Never log passwords or sensitive data
- Use HTTPS in production
- Implement `@PreAuthorize` for method-level security
- Consider rate limiting for auth endpoints

When working with this codebase, follow the established patterns for consistency and leverage the comprehensive JavaDoc comments in controllers for implementation guidance.