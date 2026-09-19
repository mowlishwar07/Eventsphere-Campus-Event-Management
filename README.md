# EventSphere – Campus Event Management System

EventSphere is a role-based campus event registration and management system built using **Spring Boot**, **MySQL**, **Thymeleaf**, **HTML**, and **CSS**.

The system provides separate modules for **Students** and **Administrators**. Administrators can create and manage campus events, while students can create accounts, browse available events, register for events, view their registrations, and cancel registrations.

---

## Features

### Student Module

* Student account registration
* Separate student login
* Student dashboard
* View available campus events
* Register for events
* View registered events
* Cancel event registration
* Logout

### Admin Module

* Separate admin login
* Admin dashboard
* Create new events
* View all events
* Update event information
* Delete events
* Open or close event registrations
* View all student registrations
* View students registered for a specific event
* Logout

---

## CRUD Operations

EventSphere demonstrates the basic CRUD operations using Spring Boot and MySQL.

| Operation | Function                                                |
| --------- | ------------------------------------------------------- |
| Create    | Admin creates events and students register for events   |
| Read      | View events, users and registrations                    |
| Update    | Admin updates event information and registration status |
| Delete    | Admin deletes events and students cancel registrations  |

---

## Technologies Used

* Java
* Spring Boot
* Spring MVC
* Spring Data JPA
* Spring Security
* Hibernate
* MySQL
* Thymeleaf
* HTML5
* CSS3
* Maven

---

## Project Structure

```text
eventsphere
│
├── src/main/java/com/example/eventsphere
│   │
│   ├── EventsphereApplication.java
│   │
│   ├── config
│   │   ├── SecurityConfig.java
│   │   └── AdminDataInitializer.java
│   │
│   ├── controller
│   │   ├── AuthController.java
│   │   ├── AdminController.java
│   │   └── StudentController.java
│   │
│   ├── model
│   │   ├── User.java
│   │   ├── Role.java
│   │   ├── Event.java
│   │   ├── EventStatus.java
│   │   ├── Registration.java
│   │   └── RegistrationStatus.java
│   │
│   ├── repository
│   │   ├── UserRepository.java
│   │   ├── EventRepository.java
│   │   └── RegistrationRepository.java
│   │
│   └── service
│       ├── UserService.java
│       ├── EventService.java
│       └── RegistrationService.java
│
├── src/main/resources
│   │
│   ├── templates
│   │   ├── index.html
│   │   ├── login.html
│   │   ├── admin-login.html
│   │   ├── register.html
│   │   ├── student-dashboard.html
│   │   ├── student-events.html
│   │   ├── student-registrations.html
│   │   ├── admin-dashboard.html
│   │   ├── admin-events.html
│   │   ├── event-form.html
│   │   ├── admin-registrations.html
│   │   └── event-registrations.html
│   │
│   ├── static/css
│   │   └── style.css
│   │
│   └── application.properties
│
└── pom.xml
```

---

## Database Design

The project uses three main MySQL tables.

### Users

```text
id
name
email
password
department
role
```

Roles:

```text
ADMIN
STUDENT
```

### Events

```text
id
event_name
description
category
venue
event_date
max_participants
status
```

Event status:

```text
OPEN
CLOSED
COMPLETED
```

### Registrations

```text
id
user_id
event_id
registration_date
status
```

Registration status:

```text
REGISTERED
CANCELLED
```

---

## Database Setup

Create the MySQL database:

```sql
CREATE DATABASE eventsphere_db;
```

Configure your database connection inside:

```text
src/main/resources/application.properties
```

Example:

```properties
spring.application.name=eventsphere

spring.datasource.url=jdbc:mysql://localhost:3306/eventsphere_db

spring.datasource.username=root

spring.datasource.password=YOUR_MYSQL_PASSWORD

spring.jpa.hibernate.ddl-auto=update

spring.jpa.show-sql=true

spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

server.port=8080
```

Replace:

```text
YOUR_MYSQL_PASSWORD
```

with your local MySQL password.

Do not upload your real database password to a public GitHub repository.

---

## Running the Project

Clone the repository:

```bash
git clone https://github.com/mowlishwar07/Eventsphere-Campus-Event-Management.git
```

Open the project directory:

```bash
cd Eventsphere-Campus-Event-Management
```

Run the Spring Boot application:

### Windows

```bash
mvnw.cmd spring-boot:run
```

or:

```bash
mvn spring-boot:run
```

Then open:

```text
http://localhost:8080
```

---

## Application URLs

### Home

```text
http://localhost:8080
```

### Student Login

```text
http://localhost:8080/login
```

### Student Registration

```text
http://localhost:8080/register
```

### Admin Login

```text
http://localhost:8080/admin/login
```

---

## Default Admin Account

For local demonstration:

```text
Email: admin@eventsphere.com
Password: admin123
```

The admin account is automatically created when the application starts if it does not already exist.

For a production deployment, the default password should be changed and should not be hardcoded.

---

## Event Registration Rules

* Only registered students can register for events.
* A student cannot register for the same event twice.
* Students cannot register for closed events.
* Registration is prevented when an event reaches its maximum participant capacity.
* Students can cancel their registrations.
* Administrators can view all registrations.
* Administrators can view registrations for individual events.

---

## Workflow

```text
                    EventSphere
                         |
             -------------------------
             |                       |
          Student                   Admin
             |                       |
       Student Login             Admin Login
             |                       |
      Student Dashboard         Admin Dashboard
             |                       |
      Browse Events             Create Event
             |                       |
      Register Event            Manage Events
             |                       |
     My Registrations        View Registrations
             |
    Cancel Registration
```

---

## Objective

The objective of EventSphere is to provide a simple and centralized platform for managing campus events.

The project also demonstrates the implementation of:

* Spring Boot MVC architecture
* MySQL database integration
* JPA and Hibernate
* Role-based login
* CRUD operations
* Entity relationships
* Student-event registration
* Thymeleaf frontend integration

---

## Future Enhancements

Possible future improvements include:

* Event search and filtering
* Email notifications
* QR-based event check-in
* Event posters and image uploads
* Registration approval system
* Student profile management
* Event certificates
* Attendance tracking
* Dashboard statistics
* Forgot password functionality
* REST API support

---

## Author

Mowlishwar T

Developed as a Java Spring Boot and MySQL project for campus event registration and management.

## Project

**EventSphere – Role-Based Campus Event Registration and Management System**
