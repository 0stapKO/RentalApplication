# Rental Application API

### Description
This is a RESTful backend web application for a rental management system (books, laptops, tools, etc.). It allows users to browse items, rent them, and track their rentals, while administrators can manage the item catalog. Built with Java and Spring Boot.

### Tech Stack
* **Java 21**
* **Spring Boot 4.1** (Web, Data JPA, Validation)
* **Spring Security + JWT** for Authentication & Authorization
* **PostgreSQL** - database
* **JUnit 5 & Mockito** for unit and integration testing
* **Swagger / OpenAPI** for API documentation

### Prerequisites
* Java 21 or higher
* Maven 3.6+
* PostgreSQL

### How to run
1. Clone the repository:
   ```bash
   git clone https://github.com/0stapKO/RentalApplication.git
   ```
   
2. Create **application.properties** file at **src/main/resources/** with next properties:
- spring.application.name=Rental
- spring.datasource.url = \<postgresql database link>
- spring.datasource.username=\<your db user>
- spring.datasource.password=\<your db password>
- spring.datasource.driver-class-name=org.postgresql.Driver
- spring.jpa.hibernate.ddl-auto=update
- spring.jpa.show-sql=true
- jwt.secret=\<secret key for jwt generation>

3. Navigate to the project folder and run the application:
    ```bash
    mvn spring-boot:run
   ```
   
### API Documentation
Once the application is running, the interactive Swagger UI documentation is available at:
http://localhost:8080/swagger-ui.html

### Authorization and Roles
The application uses JWT Bearer tokens for security.
- ROLE_USER: Assigned by default to all new users. 
- ROLE_ADMIN: To create a user with an admin role, register a new user with the firstName set to "admin" (this is a development backdoor for testing purposes).

### Running Tests
To execute the test suite (Service and Controller layers), run:
```bash
mvn clean test
```