# 🌟 EventSphere - Campus Event Management System

**EventSphere** is a full-stack web application designed for colleges and universities to streamline campus event creation, discovery, team participation, and registration management. Built with **Spring Boot**, **Spring Data JPA**, **MySQL**, and **Thymeleaf**, it features a modern, responsive dark glassmorphic user interface.

---

## 🚀 Features

### 👨‍🎓 Student Portal
- **Account Registration & Login**: Sign up with student name, college email, **Register / Roll No.**, and department. Passwords are securely hashed with BCrypt.
- **Personalized Student Dashboard**: Displays student profile details, roll number, and quick shortcuts.
- **Discover Campus Events**: Browse active hackathons, workshops, guest lectures, and cultural events.
- **Keyword Search & Filter**: Search events in real-time by event title, category, or venue with instant clear options.
- **Solo & Team Registrations**:
  - **Solo Events**: One-click instant registration.
  - **Team Events**: Specify a **Team Name**, auto-assign the logged-in student as **Team Leader**, and dynamically add teammates (**Register / Roll No.**, **Full Name**, and **Department**) up to the event's max team capacity.
- **My Registrations**: Track registration statuses, event dates, venues, team names, and teammate rosters, with the option to cancel registrations.

### 🛡️ Admin Portal
- **Dedicated Admin Login**: Pre-configured admin portal for event coordinators and organizers.
- **Event Management (CRUD)**:
  - **Create Event**: Configure event title, description, category, venue, date, capacity, and set participation as **Solo** or **Team Event (with Max Team Capacity)**.
  - **Edit & Update**: Modify existing event details, capacities, schedules, or statuses (`OPEN`, `CLOSED`, `COMPLETED`).
  - **Delete Event**: Safely remove events and associated registrations.
- **Attendee & Registration Oversight**:
  - Search all campus registrations by student name, roll number, department, event, or team name.
  - View event-specific attendee rosters and inspect complete team compositions (Leader + Teammates).

---

## 🛠️ Tech Stack

- **Backend**: Java 17, Spring Boot 4.x (Spring Web MVC, Spring Data JPA, Spring Security)
- **Database**: MySQL 8.x
- **Template Engine**: Thymeleaf
- **Frontend**: Vanilla HTML5, CSS3 (Modern Glassmorphism, CSS Grid & Flexbox), Responsive JavaScript
- **Security**: BCryptPasswordEncoder, Session-based Role Authentication
- **Build Tool**: Apache Maven

---

## 📁 Project Structure

```text
eventsphere/
├── src/
│   ├── main/
│   │   ├── java/com/example/eventsphere/
│   │   │   ├── config/             # Security & Admin initializer configs
│   │   │   ├── controller/         # Auth, Admin, and Student Controllers
│   │   │   ├── model/              # User, Event, and Registration Entities & Enums
│   │   │   ├── repository/         # Spring Data JPA Repositories with JPQL Search
│   │   │   ├── service/            # Business logic services
│   │   │   └── EventsphereApplication.java
│   │   └── resources/
│   │       ├── static/css/         # Glassmorphic stylesheet (style.css)
│   │       ├── templates/          # Thymeleaf HTML views
│   │       └── application.properties
│   └── test/
└── pom.xml
```

---

## ⚙️ Getting Started

### Prerequisites
- **Java Development Kit (JDK)**: Version 17 or higher
- **Apache Maven**: Version 3.8+
- **MySQL Server**: Version 8.0+

### 1. Clone the Repository
```bash
git clone https://github.com/your-username/eventsphere.git
cd eventsphere
```

### 2. Configure MySQL Database
Create a new MySQL database:
```sql
CREATE DATABASE eventsphere_db;
```

Open `src/main/resources/application.properties` and update your MySQL credentials:
```properties
spring.application.name=eventsphere

spring.datasource.url=jdbc:mysql://localhost:3306/eventsphere_db
spring.datasource.username=your_mysql_username
spring.datasource.password=your_mysql_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

server.port=8080
```

### 3. Build & Run
Run the application using Maven:
```bash
mvn clean spring-boot:run
```
Alternatively, build the JAR file and execute it:
```bash
mvn clean package
java -jar target/eventsphere-0.0.1-SNAPSHOT.jar
```

Access the application in your browser at:
```
http://localhost:8080
```

---

## 🔑 Default Credentials

### Admin Account
When the application starts for the first time, a default administrator account is automatically initialized:
- **Email**: `admin@eventsphere.com`
- **Password**: `admin123`
- **Portal URL**: `http://localhost:8080/admin/login`

### Student Account
- Create a student account by navigating to **Sign Up** (`http://localhost:8080/register`).
- Log in via the **Student Login** page (`http://localhost:8080/login`).

---

## 📸 Key Endpoints & Routes

| URL Path | Access | Description |
|---|---|---|
| `/` | Public | Landing / Home page |
| `/login` | Public | Student sign-in |
| `/register` | Public | Student sign-up with Roll No. & Department |
| `/admin/login` | Public | Administrator login portal |
| `/student/dashboard` | Student | Student welcome dashboard & profile card |
| `/student/events` | Student | Browse events with search & registration buttons |
| `/student/events/{id}/register` | Student | Solo confirmation / Team registration with teammate roster |
| `/student/registrations` | Student | View registered events, teams, and cancel option |
| `/admin/dashboard` | Admin | Administrative overview & quick actions |
| `/admin/events` | Admin | Manage, search, edit, or delete events |
| `/admin/events/new` | Admin | Create new solo or team event with capacity |
| `/admin/registrations` | Admin | View and search all registrations & team rosters |
| `/admin/events/{id}/registrations` | Admin | View registered participants for a specific event |
| `/logout` | Authenticated | Session invalidation and redirect to Home |

---

## Author
Mowlishwar T
