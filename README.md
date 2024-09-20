# Microservices Exploration with Spring Boot

This project is a hands-on exploration of microservices architecture using Java Spring Boot. The goal of the project is to understand the core concepts of microservices, implement inter-service communication, and manage distributed systems using various tools and techniques provided by the Spring ecosystem.

## Table of Contents

- [About the Project](#about-the-project)
- [Technologies Used](#technologies-used)
- [Architecture](#architecture)
- [Services](#services)
- [Communication Between Services](#communication-between-services)
- [Security](#security)
- [License](#license)

## About the Project

This project demonstrates a microservices architecture using Java Spring Boot. The architecture is composed of independent services that handle different business logic, such as user management, baggage management, flight management, ticketing, and a gateway for routing and authentication. The project explores how to structure, develop, and scale microservices while ensuring inter-service communication and security.

## Technologies Used

- **Java 17**
- **Spring Boot** - Core framework for microservices
- **Spring Cloud Gateway** - API Gateway for routing and authentication
- **Spring Security** - For securing services using JWT-based authentication
- **OpenFeign** - For declarative REST clients between microservices
- **Eureka** - Service discovery and registration
- **MySQL** - Relational database for data persistence
- **Postman** - For API testing

## Architecture

The architecture follows a distributed design where each microservice is responsible for a specific domain. Communication between services happens through REST APIs and, in some cases, through asynchronous messaging.

- **Gateway**: Acts as the entry point, routing traffic to the appropriate services and handling JWT authentication.
- **User Service**: Manages user registration, login, and role-based authorization.
- **Baggage Service**: Tracks and manages baggage information, with operations for updating status and location.
- **Flight Service**: Handles flight scheduling, status updates, and flight-related queries.
- **Ticket Service**: Manages ticket bookings, cancellations, and user ticket history.

## Services

### 1. **User Service**

- Responsible for user authentication, registration, and authorization.
- Integrates JWT for secure communication.
- Role-based access control (e.g., `ROLE_USER`, `ROLE_ADMIN`).

### 2. **Baggage Service**

- Manages the baggage lifecycle: creation, updates, and tracking.
- Communication with the User Service for authorization using OpenFeign.

### 3. **Flight Service**

- Manages flights, including schedule, updates, and flight status.
- Communicates with the Ticket Service to ensure flight availability.

### 4. **Ticket Service**

- Manages ticket bookings and cancellations.
- Communicates with the Flight and User services to verify user and flight status.

### 5. **Gateway**

- API gateway to route requests to various services.
- Secures endpoints using JWT tokens validated via the User Service.

## Communication Between Services

- **Synchronous**: Most services communicate via REST using **OpenFeign**, allowing easy inter-service communication.

## Security

The security is implemented using Spring Security and JWT (JSON Web Token). The authentication happens via the User Service, which issues tokens upon successful login. The Gateway verifies these tokens before forwarding requests to other microservices. Role-based authorization is enforced at both the Gateway and the service level.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
