# Eventify Frontend

Next.js web application for the Eventify event management platform.

## Tech

- Next.js 15 (App Router), React 19, TypeScript
- SWR for data fetching, dayjs for dates
- Multi-language support (EN, FR, DE)
- Custom CSS (no UI framework)

## Running Locally

1. Copy `.env.example` to `.env.local` and adjust the API URL if needed
2. Install and run:

```bash
npm install
npm run dev
```

App: `http://localhost:3000`

## Pages

| Route           | Description                      |
|-----------------|----------------------------------|
| `/`             | Home with personalized feed      |
| `/login`        | User login                       |
| `/register`     | User registration                |
| `/profile`      | User preferences & city selection|
| `/events`       | Events overview with filters     |
| `/events/new`   | Create new event                 |
| `/cities`       | Venues grouped by city           |
| `/venue/[id]`   | Venue details & schedule         |
| `/venue/new`    | Create new venue (admin)         |

## Docker

```bash
docker build -t eventify-frontend .
docker run -p 3000:3000 eventify-frontend
```
