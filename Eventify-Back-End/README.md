# Eventify Backend

Spring Boot REST API for the Eventify event management platform.

## Tech

- Java 21, Spring Boot 3, Hibernate/JPA
- PostgreSQL, JWT authentication, SpringDoc OpenAPI

## Running Locally

1. Ensure PostgreSQL is running with a database named `eventify`
2. Copy `.env.example` values or set environment variables
3. Run:

```bash
./mvnw spring-boot:run
```

API: `http://localhost:8080/api`
Swagger UI: `http://localhost:8080/swagger-ui.html`

## API Endpoints

| Resource            | Endpoints                          |
|---------------------|------------------------------------|
| Users / Auth        | `POST /api/users/register`, `POST /api/users/login`, `GET /api/users/{id}` |
| Events              | `GET/POST/PUT/DELETE /api/events`  |
| Venues              | `GET/POST/PUT/DELETE /api/venues`  |
| Cities              | `GET/POST/PUT/DELETE /api/cities`  |
| Tickets             | `POST /api/tickets/purchase`, `DELETE /api/tickets/{id}` |
| Event Descriptions  | `GET/POST/PUT/DELETE /api/event-descriptions` |

## Docker

```bash
docker build -t eventify-backend .
docker run -p 8080:8080 --env-file .env eventify-backend
```
