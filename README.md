# Airbnb Clone Backend

[![Java Version](https://img.shields.io/badge/Java-17-blue)]()
[![Spring Boot Version](https://img.shields.io/badge/Spring_Boot-3.x-green)]()
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue)](https://www.postgresql.org/)
[![Build](https://img.shields.io/badge/Build-Maven-red)](https://maven.apache.org/)


A **Spring Boot** backend project that implements core functionality of an Airbnb-style application: property (hotel/room) management, inventory, bookings, guests, payments, etc. It’s designed to showcase clean architecture, professional version control practices, and production-ready components.

---

## Table of Contents

- [Features](#features)  
- [Architecture & Project Structure](#architecture--project-structure)  
- [Getting Started](#getting-started)  
- [Configuration & Secrets Management](#configuration--secrets-management)  
- [API Endpoints](#api-endpoints)  
- [Running Tests](#running-tests)  
- [Future Enhancements](#future-enhancements)  
- [Author](#author)

---

## Features

- User, Hotel, Room, Inventory management  
- Booking flow with status tracking  
- Payment & Guest entities  
- DTOs + ModelMapper configuration for clean data transfer  
- Global exception handling & response formatting  
- Well-structured service, repository, controller layers  
- Enums for clean domain logic (booking status, roles, payment status, etc.)

---

## Architecture & Project Structure

```
src
└── main
   ├── java
   │   └── com.triveshpatel.practiceproject.airbnb
   │       ├── advice                # Exception and response handlers (GlobalExceptionHandler, GlobalResponseHandler, etc.)
   │       ├── config                # Configuration classes (e.g. MapperConfig)
   │       ├── controller            # REST controllers / APIs
   │       ├── dto                   # Data Transfer Objects
   │       ├── entity                # JPA Entities
   │       │   └── enums             # BookingStatus, Role, etc.
   │       ├── exception             # Custom exceptions (e.g. ResourceNotFound)
   │       ├── repository            # Spring Data JPA Repos
   │       ├── service               # Business logic
   │       └── util / main app class
   └── resources
       └── application.properties    # Configs (secrets excluded or managed separately)
```

---

## Getting Started

These instructions will help you get a copy of the project running on your local machine.

### Prerequisites

- Java 17 or later  
- Maven 3.x  
- A running database (e.g. MySQL, PostgreSQL etc.)  
- (Optional) Postman / HTTP client for testing APIs  

### Setup

1. Clone this repository:  
   ```bash
   git clone https://github.com/Trivesh-Patel/Airbnb.git
   cd Airbnb
   ```

2. Create a file `application-secret.properties` in `src/main/resources/` (this file is ignored via `.gitignore`) containing your secret configuration values, for example:

   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/airbnb_db
   spring.datasource.username=YOUR_DB_USERNAME
   spring.datasource.password=YOUR_DB_PASSWORD

   jwt.secret=YOUR_REAL_JWT_SECRET
   ```

3. Ensure that `application.properties` (which is committed) imports or references this secret file. For example:

   ```properties
   spring.config.import=optional:application-secret.properties
   ```

4. Build & run the application:

   ```bash
   ./mvnw clean install
   ./mvnw spring-boot:run
   ```

   Or run via your IDE.

---

## Configuration & Secrets Management

- **`application.properties`** file contains placeholders / safe defaults. No real secrets should be committed.  
- Real credentials go into **`application-secret.properties`** (ignored by Git).  
- Environment variables can also be used in production or CI/CD rather than a properties file.  
- Always ensure `.gitignore` has entries to prevent committing IDE files, target directories, and any files with secrets or credentials.

---

## API Endpoints

Here are some of the key endpoints exposed by this backend:

| Feature           | HTTP Method | Endpoint                                  | Description |
|-------------------|-------------|---------------------------------------------|-------------|
| Hotels            | `GET`       | `/api/hotels`                             | List all hotels |
| Hotel Detail      | `GET`       | `/api/hotels/{hotelId}`                   | Get single hotel |
| Create Hotel      | `POST`      | `/api/hotels`                             | Add a new hotel |
| Rooms             | `GET`       | `/api/rooms`                              | List all rooms |
| Room Admin Ops    | `POST / PUT / DELETE` | `/api/rooms/admin/...`          | Admin operations on rooms |
| Inventory         | Various     | `/api/inventories/...`                    | Manage inventory for rooms/hotels |

---
---

## API Documentation (Swagger / OpenAPI)

This project includes **Swagger UI** for interactive API documentation.  
Once the application is running, you can access it in your browser:

- **Swagger UI**: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)  
- **OpenAPI spec (JSON)**: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

> These URLs assume the app runs locally on port **8080**. Adjust accordingly if you change the server port in `application.properties`.

Swagger helps you:
- Explore all available endpoints
- Test APIs directly from the UI
- Understand request/response schemas

---

Trivesh Patel  
[GitHub Profile](https://github.com/Trivesh-Patel)  

---

Feel free to use, modify, or give feedback. This project is evolving, and I welcome suggestions!  
