You are working on my existing Spring Boot Gym Class Booking System project.

Your job is to modify the existing project so it fully meets these required criteria:

1. User Authentication
   - Implement Sign up
   - Implement Login
   - Implement Logout
   - Use Spring Security
   - Passwords must be encrypted using BCrypt
   - Public pages: /login, /signup, static CSS/images
   - Protected pages: all gym class booking system features

2. Database
   - Use PostgreSQL
   - Use Spring Data JPA / Hibernate
   - Configure database using environment variables:
     DB_URL
     DB_USERNAME
     DB_PASSWORD
   - Use application.properties or application.yml
   - Do not hardcode production database credentials

3. CRUD Functions
   - Implement full Create, Read, Update, Delete for gym classes
   - Entity should include at minimum:
     id
     title/name
     trainer/instructor
     schedule/date/time
     capacity
     description
     imagePath
   - Add controller routes, repository, entity, and Thymeleaf pages if missing

4. Image Upload
   - Allow image upload when creating or editing a gym class
   - Save image files to an uploads directory
   - Store image path in PostgreSQL
   - Display uploaded images on the class listing/details page
   - Configure Spring static resource handler for /uploads/**

5. Search Function
   - Add keyword search for gym classes
   - Search should match class title/name, trainer/instructor, and description
   - Add search input to the UI

6. Web Design
   - Create a clear, user-friendly UI
   - Use Thymeleaf templates
   - Add CSS styling
   - Pages needed:
     login page
     signup page
     gym class list page
     create/edit form page
   - UI must have navigation, buttons, form validation basics, and clear messages

7. Deployment
   - Add Dockerfile suitable for Render deployment
   - Add render.yaml if possible
   - App must use PORT environment variable:
     server.port=${PORT:8080}
   - Add Render-compatible PostgreSQL configuration
   - Add .gitignore entries for:
     target/
     uploads/
     .env
   - Ensure project can be pushed to GitHub and deployed on Render

Important implementation rules:
- Do not delete existing working features unless necessary.
- Reuse existing package names and project structure.
- If the project already has similar classes, update them instead of creating duplicates.
- Make the project compile successfully with Maven.
- Use Java 17-compatible code.
- Use Thymeleaf and Spring Boot conventions.
- Keep the code simple and easy to explain for a student project.

After implementation, run these checks:
1. mvn clean package
2. Verify app starts with:
   mvn spring-boot:run
3. Verify these pages/routes exist:
   /signup
   /login
   /logout
   /classes
   /classes/new
   /classes/{id}/edit
4. Verify PostgreSQL is used, not H2.
5. Verify signup creates a user in PostgreSQL.
6. Verify login/logout works.
7. Verify gym class Create, Read, Update, Delete works.
8. Verify image upload saves and displays image.
9. Verify keyword search works.
10. Verify Dockerfile exists.
11. Verify render.yaml exists or Render deployment instructions are included.

When finished:
- Show me a summary of every file changed.
- Show me the commands to run locally.
- Show me the GitHub push commands.
- Show me the Render deployment steps.
- Confirm clearly that each of the 7 criteria is satisfied.
