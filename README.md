# Gym Class Booking System

FitReserve is a Spring Boot MVC final project for booking professional gym classes.

**Live Demo:** [https://gym-class-booking-system.onrender.com](https://gym-class-booking-system.onrender.com)

## Features

- Sign up, login, and logout with Spring Security
- User and admin roles
- PostgreSQL database with Spring Data JPA
- Browse and search active gym classes by keyword
- View class details with professional images
- Reserve and cancel class bookings
- Capacity validation and duplicate booking prevention
- Admin CRUD for gym classes
- Admin image upload and display
- Responsive UI with Bootstrap
- Render deployment blueprint included

## Tech Stack

| Layer       | Technology                         |
|-------------|------------------------------------|
| Backend     | Java 17, Spring Boot 3, Spring MVC |
| Security    | Spring Security                    |
| Database    | PostgreSQL, Spring Data JPA        |
| Frontend    | Thymeleaf, HTML, CSS, Bootstrap    |
| Build       | Maven                              |
| Deployment  | Docker, Render                     |

## Local Setup

### Prerequisites

- Java 17
- Maven
- PostgreSQL

### Steps

1. Create a local PostgreSQL database:

```sql
CREATE DATABASE gym_class_booking;
```

2. Run the app:

```bash
mvn spring-boot:run
```

3. Open [http://localhost:8080](http://localhost:8080) in your browser.

### Configuration

Default local settings (can be overridden with environment variables):

| Variable      | Default Value                          |
|---------------|----------------------------------------|
| `DB_HOST`     | `localhost`                            |
| `DB_PORT`     | `5432`                                 |
| `DB_NAME`     | `gym_class_booking`                    |
| `DB_USERNAME` | `postgres`                             |
| `DB_PASSWORD` | *(empty)*                              |
| `SERVER_PORT` | `8080`                                 |
| `JPA_DDL_AUTO`| `update`                               |
| `SEED_DATA`   | `true`                                 |

## Demo Accounts

Seed data is created automatically when the app starts.

| Role  | Email            | Password  |
|-------|------------------|-----------|
| Admin | admin@gym.com    | admin123  |
| User  | user@gym.com     | user123   |

## API Endpoints

| Method | Path                          | Role  | Description                |
|--------|-------------------------------|-------|----------------------------|
| GET    | `/`                           | All   | Home page                  |
| GET    | `/classes`                    | All   | Browse gym classes         |
| GET    | `/classes/{id}`               | All   | Class detail page          |
| POST   | `/classes/{id}/book`          | USER  | Book a class               |
| POST   | `/bookings/{id}/cancel`       | USER  | Cancel a booking           |
| GET    | `/my-bookings`                | USER  | View user's bookings       |
| GET    | `/admin/classes`              | ADMIN | Manage classes             |
| GET    | `/admin/classes/new`          | ADMIN | Add class form             |
| POST   | `/admin/classes`              | ADMIN | Save new class             |
| GET    | `/admin/classes/{id}/edit`    | ADMIN | Edit class form            |
| PUT    | `/admin/classes/{id}`         | ADMIN | Update class               |
| DELETE | `/admin/classes/{id}`         | ADMIN | Delete class               |
| GET    | `/signup`                     | All   | Registration form          |
| POST   | `/signup`                     | All   | Register new user          |
| GET    | `/login`                      | All   | Login page                 |

## Render Deployment

This repository is pre-configured for Render deployment:

1. Fork or push this repo to GitHub.
2. In Render dashboard, click **New > Blueprint**.
3. Connect your GitHub repository.
4. Render reads `render.yaml` and automatically provisions:
   - A Docker web service (`gym-class-booking-system`)
   - A free PostgreSQL database (`gym-class-booking-db`)
   - All required environment variables

The `render.yaml` maps database connection details (`DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USERNAME`, `DB_PASSWORD`) from the managed PostgreSQL service.

**Note:** Uploaded images are stored locally in `uploads/`. On Render's free tier, local files may not persist after redeploys. The app seeds classes with stable external image URLs as a fallback.

## Project Structure

```
src/
├── main/
│   ├── java/com/finalproject/gymclassbooking/
│   │   ├── config/          # Security & app configuration
│   │   ├── controller/      # MVC controllers
│   │   ├── model/           # JPA entities
│   │   ├── repository/      # Spring Data repositories
│   │   ├── service/         # Business logic
│   │   └── GymClassBookingApplication.java
│   └── resources/
│       ├── static/          # CSS, images, JS
│       ├── templates/       # Thymeleaf views
│       ├── application.properties
│       └── seed.sql
└── test/
    └── java/.../service/    # Service-layer tests
```

## Final Project Checklist

- User Authentication: complete
- PostgreSQL + JPA: complete
- CRUD Functions: complete through admin class management
- Image Upload: complete through admin class form
- Search Function: complete on class listing page
- Web Design: polished responsive UI
- Deployment: Render blueprint included
- Report: see `PROJECT_REPORT.md`
