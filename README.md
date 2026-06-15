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
- Admin dashboard with class, booking, and user management
- Admin booking review and cancellation
- Admin image upload and display
- Responsive UI with custom CSS
- Render deployment blueprint included

## Tech Stack

| Layer       | Technology                         |
|-------------|------------------------------------|
| Backend     | Java 17, Spring Boot 3, Spring MVC |
| Security    | Spring Security                    |
| Database    | PostgreSQL, Spring Data JPA        |
| Frontend    | Thymeleaf, HTML, custom CSS        |
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

2. Configure the PostgreSQL connection and run the app:

```zsh
export DB_URL="jdbc:postgresql://localhost:5432/gym_class_booking"
export DB_USERNAME="postgres"
export DB_PASSWORD="your_postgres_password"
./mvnw spring-boot:run
```

3. Open [http://localhost:8080](http://localhost:8080) in your browser.

### Configuration

Default local settings (can be overridden with environment variables):

| Variable       | Default Value                                             |
|----------------|-----------------------------------------------------------|
| `DB_URL`       | `jdbc:postgresql://localhost:5432/gym_class_booking`       |
| `DB_USERNAME`  | `postgres`                                                |
| `DB_PASSWORD`  | *(empty)*                                                 |
| `PORT`         | `8080`                                                    |
| `JPA_DDL_AUTO` | `update`                                                  |
| `SEED_DATA`    | `true`                                                    |
| `UPLOAD_DIR`   | `uploads`                                                 |

Render may provide `DB_URL` as a `postgresql://...` connection string. The application converts that value into the JDBC format required by Spring Boot. Legacy `DATABASE_URL` and the previous `DB_HOST`/`DB_PORT`/`DB_NAME` variables are also accepted during deployment migration.

## Demo Accounts

Seed data is created automatically when the app starts.

| Role  | Email            | Password  |
|-------|------------------|-----------|
| Admin | admin@gym.com    | admin123  |
| User  | user@gym.com     | user123   |

## API Endpoints

| Method | Path                          | Role          | Description                    |
|--------|-------------------------------|---------------|--------------------------------|
| GET    | `/login`                      | Public        | Login form                     |
| GET    | `/signup`                     | Public        | Registration form              |
| POST   | `/signup`                     | Public        | Register a BCrypt user         |
| POST   | `/logout`                     | Authenticated | Log out with CSRF protection   |
| GET    | `/`                           | Authenticated | Home page                      |
| GET    | `/classes`                    | Authenticated | Browse and search gym classes  |
| GET    | `/classes/{id}`               | Authenticated | Class detail page              |
| GET    | `/classes/new`                | ADMIN         | Add class form                 |
| POST   | `/classes`                    | ADMIN         | Create or update a class       |
| GET    | `/classes/{id}/edit`          | ADMIN         | Edit class form                |
| POST   | `/classes/{id}/delete`        | ADMIN         | Delete or hide a class         |
| GET    | `/bookings`                   | USER/ADMIN    | View user's bookings           |
| POST   | `/bookings`                   | USER/ADMIN    | Book a class                   |
| POST   | `/bookings/{id}/cancel`       | USER/ADMIN    | Cancel a booking               |
| GET    | `/admin`                      | ADMIN         | Admin dashboard                |
| GET    | `/admin/classes`              | ADMIN         | Manage classes                 |
| GET    | `/admin/bookings`             | ADMIN         | Manage all bookings            |
| GET    | `/admin/users`                | ADMIN         | Manage users                   |

The original `/admin/classes/new`, `/admin/classes/{id}/edit`, `/admin/classes`, and `/admin/classes/{id}/delete` routes remain available for compatibility.

## Render Deployment

This repository is pre-configured for Render deployment:

1. Fork or push this repo to GitHub.
2. In Render dashboard, click **New > Blueprint**.
3. Connect your GitHub repository.
4. Render reads `render.yaml` and automatically provisions:
   - A Docker web service (`gym-class-booking-system`)
   - A free PostgreSQL database (`gym-class-booking-db`)
   - All required environment variables

The `render.yaml` maps `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD` from the managed PostgreSQL service. The web service also receives `PORT` automatically from Render.

**Note:** Uploaded images are stored locally in `uploads/`. On Render's free tier, local files may not persist after redeploys. The app seeds classes with stable external image URLs as a fallback.

### GitHub Push

```zsh
git add .
git commit -m "Meet full gym booking project criteria"
git push origin main
```

### Render Deployment

1. Push the repository to GitHub.
2. Open [Render](https://dashboard.render.com/) and choose **New > Blueprint**.
3. Select this GitHub repository. Render reads `render.yaml`.
4. Apply the Blueprint to create the Docker web service and PostgreSQL database.
5. Wait for the deploy to become **Live**, then open the service URL.
6. Confirm `/login` loads, sign in, and verify `/classes`.

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
│       └── application.properties
└── test/
    └── java/.../service/    # Service-layer tests
```

## Seven-Criteria Checklist

1. **User Authentication:** Spring Security signup/login/logout with BCrypt passwords and role-based access.
2. **Database:** PostgreSQL runtime database with Spring Data JPA/Hibernate and environment-based credentials.
3. **CRUD Functions:** Admin create, read, update, delete-or-hide operations for gym classes.
4. **Image Upload:** Validated image uploads saved under `uploads/`, with paths stored in PostgreSQL and displayed in Thymeleaf.
5. **Search Function:** Keyword search matches class name, instructor, category, and description.
6. **Web Design:** Responsive Thymeleaf pages with navigation, forms, validation, status messages, and custom CSS.
7. **Deployment:** Java 17 Dockerfile, Render Blueprint, managed PostgreSQL, `PORT` support, and GitHub-ready `.gitignore`.

Run the complete verification build with:

```zsh
./mvnw clean package
```

See `PROJECT_REPORT.md` for the project report and `AGENT_TASK.md` for the assignment checklist.
