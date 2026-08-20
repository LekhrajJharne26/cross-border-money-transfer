# CrossBorderMoneyTransfer

Initial Spring Boot foundation with a complete authentication module for a cross-border money transfer platform. Beneficiary, transaction, and all other business modules are intentionally excluded.

## Requirements and run

Use Java 17, Maven 3.9+, and MySQL 8+. Configure the environment and start:

```powershell
$env:DB_USERNAME = "root"
$env:DB_PASSWORD = "your-password"
$env:JWT_SECRET = "<Base64-encoded-secret-of-at-least-32-bytes>"
mvn spring-boot:run
```

The default database is `cross_border_money_transfer`. Hibernate uses `ddl-auto: validate`, so it neither creates nor alters schema. Open Swagger at `http://localhost:8080/api/swagger-ui.html`.

Flyway applies the initial `users` table migration automatically at startup. The authentication endpoints are public:

- `POST /api/v1/auth/register`
- `POST /api/v1/auth/login`

Beneficiary endpoints require `Authorization: Bearer <access-token>`:

- `POST /api/v1/beneficiaries`
- `GET /api/v1/beneficiaries`
- `GET /api/v1/beneficiaries/{beneficiaryId}`
- `PUT /api/v1/beneficiaries/{beneficiaryId}`
- `DELETE /api/v1/beneficiaries/{beneficiaryId}`

## File guide

| File | Purpose |
| --- | --- |
| `pom.xml` | Maven build and Java 17, Spring Boot, JPA, Security, Validation, MySQL, Lombok, JWT, and OpenAPI dependencies. |
| `CrossBorderMoneyTransferApplication.java` | Spring Boot entry point and JWT property binding registration. |
| `config/JwtProperties.java` | Validated external JWT configuration. |
| `config/SecurityConfig.java` | Stateless security configuration: Swagger and health are public; APIs require authentication and unauthenticated access returns 401. |
| `config/OpenApiConfig.java` | Swagger metadata and Bearer-JWT security scheme. |
| `security/JwtService.java` | Signs JWTs and verifies/parses their claims. |
| `security/JwtAuthenticationFilter.java` | Reads valid Bearer tokens and establishes the request principal. |
| `dto/ApiResponse.java` | Generic response envelope for successful and failed API responses. |
| `exception/GlobalExceptionHandler.java` | Central validation, malformed-request, and unexpected-error handling. |
| `application.yml` | Environment-driven MySQL, Hibernate, server, JWT, and Swagger configuration. |
| `db/migration/V1__create_users.sql` | Flyway migration that creates the authentication users table. |
| `auth/controller/AuthController.java` | Swagger-documented registration and login HTTP endpoints. |
| `auth/dto/*.java` | Validated input DTOs and the token response DTO; no entity is exposed over HTTP. |
| `auth/service/*.java` | Authentication use-case contract and implementation. |
| `user/entity/User.java` | JPA user entity and Spring Security principal implementation. |
| `user/model/Role.java` | `ADMIN` and `USER` access-level enum. |
| `user/repository/UserRepository.java` | JPA persistence gateway for users. |
| `user/service/*.java` | User lookup and persistence abstraction/implementation. |
| `exception/DuplicateResourceException.java` | Signals a uniqueness conflict, such as a duplicate registration email. |
| `exception/InvalidCredentialsException.java` | Signals a generic login failure without exposing whether an email exists. |
| `exception/ResourceNotFoundException.java` | Signals a missing persisted resource for internal service lookups. |
| `beneficiary/entity/Beneficiary.java` | Owner-linked JPA entity, recipient fields, and automatic creation/update timestamps. |
| `beneficiary/repository/BeneficiaryRepository.java` | Owner-scoped JPA queries that prevent cross-user record access. |
| `beneficiary/dto/BeneficiaryRequest.java` | Validated create/update request payload. |
| `beneficiary/dto/BeneficiaryResponse.java` | Beneficiary data returned to the authenticated owner. |
| `beneficiary/service/BeneficiaryService.java` | Beneficiary use-case contract. |
| `beneficiary/service/impl/BeneficiaryServiceImpl.java` | Transactional CRUD and owner authorization implementation. |
| `beneficiary/controller/BeneficiaryController.java` | Secured, Swagger-documented beneficiary API endpoints. |
| `db/migration/V2__create_beneficiaries.sql` | Flyway migration for beneficiary storage and its user foreign key. |

## Package layout

```text
com.crossborder.moneytransfer
├── config       # cross-cutting configuration
├── dto          # API data-transfer objects
├── exception    # centralized error handling
└── security     # JWT infrastructure
```
