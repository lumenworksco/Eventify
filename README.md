# Eventify

A full-stack event management platform for browsing venues, discovering events, and purchasing tickets. Built with **Spring Boot** (Java 21) and **Next.js** (React 19, TypeScript).

## Features

- Browse events and venues across multiple cities
- Filter events by city, type, and date
- User authentication with JWT (role-based: User, Organizer, Admin)
- Ticket purchasing with availability tracking
- Multi-language support (English, French, German)
- Personalized home feed based on preferred city
- Swagger/OpenAPI documentation for the REST API

## Tech Stack

| Layer    | Technology                                  |
|----------|---------------------------------------------|
| Frontend | Next.js 15, React 19, TypeScript, SWR       |
| Backend  | Spring Boot 3, Java 21, Hibernate, JWT       |
| Database | PostgreSQL 16                                |
| Docs     | SpringDoc OpenAPI / Swagger UI               |

## Project Structure

```
Eventify/
  Eventify-Back-End/    # Spring Boot REST API
  Eventify-Front-End/   # Next.js web application
  docker-compose.yml    # Full-stack local development
```

## Quick Start

### Using Docker Compose

```bash
docker compose up --build
```

This starts PostgreSQL, the backend (port 8080), and the frontend (port 3000).

### Manual Setup

#### Prerequisites

- Java 21
- Node.js 20+
- PostgreSQL 16

#### Backend

```bash
cd Eventify-Back-End

# Create a local PostgreSQL database named 'eventify'
# Then run:
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080/api` and Swagger UI at `http://localhost:8080/swagger-ui.html`.

See [`Eventify-Back-End/.env.example`](Eventify-Back-End/.env.example) for environment variables.

#### Frontend

```bash
cd Eventify-Front-End
npm install
npm run dev
```

The app will be available at `http://localhost:3000`.

See [`Eventify-Front-End/.env.example`](Eventify-Front-End/.env.example) for environment variables.

## Environment Variables

### Backend

| Variable     | Description           | Default     |
|--------------|-----------------------|-------------|
| `PGHOST`     | PostgreSQL host       | `localhost` |
| `PGPORT`     | PostgreSQL port       | `5432`      |
| `PGDATABASE` | Database name         | `eventify`  |
| `PGUSER`     | Database user         | `postgres`  |
| `PGPASSWORD` | Database password     | `postgres`  |
| `JWT_SECRET` | JWT signing secret    | (dev default) |
| `PORT`       | Server port           | `8080`      |

### Frontend

| Variable              | Description      | Default                                        |
|-----------------------|------------------|------------------------------------------------|
| `NEXT_PUBLIC_API_URL` | Backend API URL  | `https://eventify-back-end.onrender.com/api`   |

## API Documentation

When the backend is running, visit `/swagger-ui.html` for interactive API docs.

## License

[MIT](LICENSE)
