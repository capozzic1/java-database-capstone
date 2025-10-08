
🏥 Hospital Management System (Spring Boot + MVC)
Overview

The Hospital Management System (HMS) is a Spring Boot–based web application built using the Spring MVC architecture.
It provides an interface for Admins, Doctors, and Patients to manage hospital operations such as appointments, doctor schedules, and patient records — all within a single, server-rendered application.

This version uses Spring Boot, Spring MVC, Spring Security, and Spring Data JPA, backed by 2  databases (MySQL/MongoDB);

⚙️ Tech Stack
Backend

Java 21

Spring Boot 3

Spring MVC for controllers and server-rendered views

Vanilla javaScript for client-side interactivity

Spring Data JPA + Hibernate for ORM and persistence

Spring Security with JWT or session-based authentication

Thymeleaf (or JSP) for rendering HTML views

Maven for dependency management and build

Profiles for local and remote (Docker/AWS) environments

Database

MySQL / PostgreSQL

JPA entity mappings for Doctor, Patient, and Appointment

Schema auto-generated on startup (via spring.jpa.hibernate.ddl-auto)

hms/
├── src/main/java/com/hms/
│    ├── controller/        # MVC controllers (DoctorController, PatientController, etc.)
│    ├── service/           # Business logic layer
│    ├── repository/        # Data access layer using Spring Data JPA
│    ├── model/             # JPA entities (Doctor, Patient, Appointment)
│    ├── security/          # JWT or Spring Security configuration
│    └── HospitalManagementApplication.java
│
├── src/main/resources/
│    ├── templates/         # Thymeleaf HTML templates
│    ├── static/            # CSS, JS, and images
│    ├── application.yml
│    └── application-local.yml
│
├── pom.xml
└── README.md


🔐 Authentication & Authorization

Spring Security handles login, logout, and route protection.

Role-based permissions:

Admin → manage doctors, patients, and appointments.

Doctor → view assigned patients and appointments.

Patient → book and view personal appointments.

Supports JWT tokens or session-based login, depending on configuration.

🧠 Key Features

Add / update / delete doctors and patients.

Schedule and view appointments.

Secure login and registration for all roles.

Server-rendered pages using Thymeleaf templates.

Database integration via Spring Data JPA.

Environment-based configuration (local vs remote).

🚀 Getting Started
1️⃣ Clone and Build
git clone https://github.com/yourusername/hospital-management-system.git
cd hospital-management-system
mvn clean install

2️⃣ Run the Application
mvn spring-boot:run -Dspring-boot.run.profiles=local

Example Endpoints
GET /patients
Retrieves a list of all patients.
POST /patients
Registers a new patient.
GET /patients/{id}
Retrieves details for a specific patient.
PUT /patients/{id}
Updates information for a specific patient.
DELETE /patients/{id}
Deletes a patient record.
GET /doctors
Lists all doctors.
POST /appointments
Schedules a new appointment.
GET /appointments?doctorId={doctorId}&date={date}
Retrieves appointments for a specific doctor on a given date.
GET /medical-records/{patientId}
Fetches medical records for a patient.
POST /billing
Creates a new billing entry for a patient.

Future Enhancements
Migrating Spring MVC to an Angular frontend 