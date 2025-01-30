# Raven

Raven is a secure chat application designed as a university project. It follows a microservices architecture to ensure modularity and security. The platform provides authentication, message handling, and real-time message relay services with a strong focus on end-to-end encryption (E2EE) and best security practices.

![image](https://github.com/user-attachments/assets/c8745b9b-bc1e-4740-87b3-24d4f4de072e)
![image](https://github.com/user-attachments/assets/72dd5926-a2f0-4551-a65f-8859b88784e6)
![image](https://github.com/user-attachments/assets/bf934fc5-fbea-411c-87bb-e9f4f0ba9d55)



## Architecture Overview

Raven consists of three main microservices:

1. **Authentication Service (Auth)**
2. **Message Service (Message)**
3. **Relay Service (Relay)**

### Authentication Service (Auth)
- Manages user authentication and registration.
- Implements JWT-based authentication.
- Uses PostgreSQL for credential storage.
- Supports Multi-Factor Authentication (MFA) via email.

### Message Service (Message)
- Handles user, message, and chat management.
- Processes and stores messages.
- Uses MongoDB for data persistence.
- Provides APIs for message operations.

### Relay Service (Relay)
- Implements a STOMP WebSocket-based real-time messaging service.
- Validates JWT tokens during handshake.
- Relays messages between users using a publish-subscribe model.

### Inter-Service Communication
- The Relay and Message services communicate via Apache Kafka for event-driven messaging.

## Security Features
- Implements End-to-End Encryption (E2EE) using Elliptic Curve Cryptography.
- Enforces HTTPS with Nginx as a reverse proxy.
- Configures security headers for enhanced protection.
- Ensures compliance with OWASP ZAP security scans.
- Limits external exposure to the Relay Service.
- Routes WebSocket traffic through Nginx.

## Frontend
- Developed using Svelte with security best practices.
- Avoids inline JavaScript and styles.

## Deployment
- Uses Nginx for service routing.
- Restricts external access to only the Relay Service.

## Setup
### Prerequisites
- Requires Docker and Docker Compose for deployment.
- Requires Node.js and npm for frontend development.

### Running the Services
- All microservices are containerized and managed through Docker Compose.
