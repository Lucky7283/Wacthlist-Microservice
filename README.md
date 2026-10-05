# Watchlist Microservice

A backend microservice for managing user watchlists, item statuses, and comments.

## Tech Stack

- **Java 21**
- **Spring Boot 4** (Web, Data JPA, Security)
- **PostgreSQL**
- **JWT & OAuth2**
- **Lombok** & **MapStruct**

## Features

- **User Management**: Authentication and role-based access control.
- **Watchlist Operations**: Add, update, view, and track media/items with status management.
- **Comments**: User reviews and comments on watchlist items.
- **Security**: Stateless JWT-based authentication filter.

## Relations
- User(1)-[n]Watchlist
- User(1)-[n]Comment

## Getting Started

### Prerequisites

- JDK 21+
- PostgreSQL
- Maven

### Run Locally

1. Configure your database settings in `src/main/resources/application.properties`.
2. Build and run the service:

```bash
./mvnw clean spring-boot:run
```
