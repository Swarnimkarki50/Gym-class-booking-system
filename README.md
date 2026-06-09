# Gym Class Booking System

FitReserve is a Spring Boot MVC final project for booking professional gym classes.

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
- Render deployment blueprint included

## Local Setup

Requirements:

- Java 17
- Maven
- PostgreSQL

Create a local PostgreSQL database:

```sql
CREATE DATABASE gym_class_booking;
```

Run the app:

```bash
mvn spring-boot:run
```

Default local settings:

- `DATABASE_URL=jdbc:postgresql://localhost:5432/gym_class_booking`
- Or use `DB_HOST`, `DB_PORT`, and `DB_NAME` to build the JDBC URL
- `DB_USERNAME=postgres`
- `DB_PASSWORD=`

You can override these values with environment variables.

## Demo Accounts

Seed data is created automatically when the app starts.

- Admin: `admin@gym.com` / `admin123`
- User: `user@gym.com` / `user123`

## Render Deployment

This repository includes `render.yaml` with:

- A Docker web service
- A free PostgreSQL database
- Environment variables connected from the Render database

The app stores uploaded files locally in `uploads`. On Render free services, local uploads may not persist after redeploys, so seeded image URLs are included as a reliable fallback.

## Final Project Checklist

- User Authentication: complete
- PostgreSQL + JPA: complete
- CRUD Functions: complete through admin class management
- Image Upload: complete through admin class form
- Search Function: complete on class listing page
- Web Design: polished responsive UI
- Deployment: Render blueprint included
- Report: see `PROJECT_REPORT.md`
