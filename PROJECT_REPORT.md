# Project Report: Gym Class Booking System

## Team Members

- Student 1: ____________________
- Student 2: ____________________
- Student 3: ____________________

## Project Overview

FitReserve is a web application that allows members to discover gym classes, view class details, and reserve seats online. Admin users can manage the class schedule, upload class images, and hide or delete classes.

## Main Features

- User registration, login, and logout
- Role-based access for users and admins
- Gym class search by name, category, instructor, or description
- Class detail pages with images, schedule, price, duration, and capacity
- Booking creation and cancellation
- Admin create, read, update, and delete class records
- Image upload for class photos
- PostgreSQL database with JPA repositories
- Responsive and user-friendly web design

## Technologies Used

- Java 17
- Spring Boot 3
- Spring MVC
- Spring Security
- Spring Data JPA
- PostgreSQL
- Thymeleaf
- HTML, CSS
- Maven
- Docker
- Render

## Database Design

- `users`: stores member/admin account details, encrypted passwords, and roles
- `gym_class`: stores class name, instructor, category, price, capacity, start time, duration, description, image path, and active status
- `booking`: stores class reservations, user relationship, class relationship, total price, booking status, and creation time

Relationships:

- One user can have many bookings
- One gym class can have many bookings
- Each booking belongs to one user and one gym class

## Screenshots

Add screenshots after running or deploying the project:

- Home page
- Class list/search page
- Class detail/booking page
- My bookings page
- Admin class management page
- Admin class form with image upload

## Development Process

1. Created the Spring Boot MVC project structure.
2. Added user authentication and role-based security.
3. Designed JPA entities for users, gym classes, and bookings.
4. Built repositories and service logic for search, CRUD, and bookings.
5. Created Thymeleaf pages for public users, members, and admins.
6. Added professional styling and seeded demo class images.
7. Configured PostgreSQL and Render deployment.
8. Added service tests for core booking rules.

## Problems and Solutions

- Problem: Users could book the same class more than once.
  Solution: Added a repository check that blocks duplicate active bookings.

- Problem: Classes need limited seats.
  Solution: Count active bookings and block reservations when capacity is full.

- Problem: Uploaded images are required for the project.
  Solution: Added multipart upload support and an admin image field.

- Problem: Render free services may not keep uploaded files after redeploy.
  Solution: Seeded class records use stable external image URLs as fallbacks.

## Conclusion

The Gym Class Booking System meets the final project requirements with authentication, PostgreSQL/JPA database storage, CRUD operations, image upload, keyword search, a professional UI, tests, and Render deployment configuration.

## Submission Links

- GitHub Repository URL: ____________________
- Deployed Website URL: ____________________
