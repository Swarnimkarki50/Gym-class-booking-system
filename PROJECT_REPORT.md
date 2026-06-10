# Project Report: Gym Class Booking System

## Team Members

- Swarnim Jung Karki
- ____________________
- ____________________

## Live Deployment

- **GitHub Repository:** [https://github.com/Swarnimkarki50/Gym-class-booking-system](https://github.com/Swarnimkarki50/Gym-class-booking-system)
- **Deployed Website:** [https://gym-class-booking-system.onrender.com](https://gym-class-booking-system.onrender.com)

## Project Overview

FitReserve is a web application that allows members to discover gym classes, view class details, and reserve seats online. Admin users can manage the class schedule, upload class images, and hide or delete classes. The application is built with Spring Boot 3, secured with Spring Security, stores data in PostgreSQL, and is deployed on Render via Docker.

## Main Features

- User registration, login, and logout with role-based access control
- Role-based access for users and admins (Spring Security)
- Gym class search by name, category, instructor, or description
- Class detail pages with images, schedule, price, duration, and capacity
- Booking creation and cancellation with capacity validation
- Duplicate booking prevention
- Admin create, read, update, and delete class records
- Image upload for class photos
- PostgreSQL database with JPA repositories
- Responsive and user-friendly web design (Bootstrap)
- Seed data for demo accounts and sample classes

## Technologies Used

| Technology       | Purpose                           |
|------------------|-----------------------------------|
| Java 17          | Programming language              |
| Spring Boot 3    | Application framework             |
| Spring MVC       | Web layer (controllers)           |
| Spring Security  | Authentication & authorization    |
| Spring Data JPA  | Database access (repositories)    |
| PostgreSQL       | Relational database               |
| Thymeleaf        | Server-side HTML templates        |
| Bootstrap 5      | Frontend CSS framework            |
| HTML, CSS        | UI structure and styling          |
| Maven            | Build and dependency management   |
| Docker           | Containerization                  |
| Render           | Cloud deployment                  |

## Database Design

Three main entities with defined relationships:

### Tables

- **`users`** — stores member/admin account details, encrypted passwords, and roles
- **`gym_class`** — stores class name, instructor, category, price, capacity, start time, duration, description, image path, and active status
- **`booking`** — stores class reservations, user relationship, class relationship, total price, booking status, and creation time

### Entity Relationships

```
User (1) ──→ (many) Booking (many) ←── (1) GymClass
```

- One user can have many bookings
- One gym class can have many bookings
- Each booking belongs to exactly one user and one gym class

### Key Constraints

- A user cannot book the same active class twice (duplicate prevention)
- Bookings are blocked when the class reaches its capacity limit
- Classes can be toggled active/inactive by admin (inactive classes are hidden from users)

## Screenshots

Add screenshots after running or deploying the project:

- Home page
- Class list / search page
- Class detail / booking page
- My bookings page
- Admin class management page
- Admin class form with image upload

## Development Process

1. **Project setup** — Created the Spring Boot MVC project structure with Maven.
2. **Authentication** — Added user registration, login, and Spring Security role-based access.
3. **Database design** — Designed JPA entities for users, gym classes, and bookings with proper relationships.
4. **Data access** — Built Spring Data JPA repositories and service layer with business logic.
5. **Search & CRUD** — Implemented keyword search, admin CRUD, and class management.
6. **Booking logic** — Added reserve/cancel with capacity validation and duplicate prevention.
7. **Frontend** — Created Thymeleaf pages for public users, members, and admins with Bootstrap styling.
8. **Image upload** — Added multipart file upload and storage for class images.
9. **Testing** — Wrote service-layer tests for core booking rules (duplicate prevention, capacity).
10. **Deployment** — Configured Docker, PostgreSQL, and Render Blueprint deployment.

## Problems and Solutions

| Problem | Solution |
|---------|----------|
| Users could book the same class more than once | Added a repository check that blocks duplicate active bookings |
| Classes need limited seats | Count active bookings and block reservations when capacity is full |
| Uploaded images are required for the project | Added multipart upload support and an admin image field |
| Render free services may not keep uploaded files after redeploy | Seeded class records use stable external image URLs as fallbacks |
| Render provides `DATABASE_URL` in `postgres://` format, not `jdbc:postgresql://` | Use individual `DB_HOST`, `DB_PORT`, `DB_NAME` env vars in datasource config |

## Testing

Service-layer tests validate core business rules:

- `testBookClass_Success` — confirms a valid booking is created
- `testBookClass_DuplicateBooking` — confirms duplicate bookings are rejected
- `testBookClass_ClassAtCapacity` — confirms bookings are blocked when full

Run tests with:

```bash
mvn test
```

## Conclusion

The Gym Class Booking System meets all final project requirements:

- User authentication and role-based security
- PostgreSQL / JPA database storage with three related entities
- Full CRUD operations via admin class management
- Image upload functionality
- Keyword search across class fields
- Professional, responsive user interface
- Service-layer tests for core booking rules
- Docker + Render deployment configuration

The application is fully functional, tested, and deployed at [gym-class-booking-system.onrender.com](https://gym-class-booking-system.onrender.com).

## Submission Links

- **GitHub Repository URL:** [https://github.com/Swarnimkarki50/Gym-class-booking-system](https://github.com/Swarnimkarki50/Gym-class-booking-system)
- **Deployed Website URL:** [https://gym-class-booking-system.onrender.com](https://gym-class-booking-system.onrender.com)
