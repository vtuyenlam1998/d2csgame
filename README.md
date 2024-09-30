# d2csgame

Created by:  
- **Vòng Tuyền Lâm**  
- **Trần Tấn Trung Hiếu**  
- **Nguyễn Văn Khánh**

## Introduction

d2csgame is a web-based application built for managing and interacting with various features related to the Dota 2 community. The project is designed with modern web technologies and microservices architecture to ensure scalability and maintainability.

## Technologies Used

The following technologies are integrated into this project:

- **Java (Spring Boot)**: Backend framework used for building RESTful APIs and handling the business logic.
- **Docker**: Containerization tool used for creating isolated environments for deployment.
- **Redis**: In-memory data structure store, used for caching and improving performance.
- **MySQL**: Relational database management system used for persistent data storage.
- **Kafka**: Message broker used for event-driven architecture and asynchronous communication.
- **OpenTelemetry**: Used for distributed tracing and monitoring of the application's performance.
- **Elasticsearch & Kibana**: Integrated for logging, searching, and visualizing logs and metrics.
- **Next.js**: Used as the frontend framework for building an interactive and efficient UI.
- **WebSocket**: Implemented for real-time notifications and updates.

## Prerequisites

Before starting the application, ensure that you have the following installed on your system:

- Docker Desktop: [Install Docker](https://docs.docker.com/get-docker/)
- Java 17 or above
- Node.js (for frontend, if applicable)

## How to Start the Application

### 1. Clone the Repository

Clone the repository to your local machine:

```bash
git clone https://github.com/yourusername/d2csgame.git
cd d2csgame
```

### 2. Build and Start Services with Docker

The application is containerized with Docker. To start the backend services along with the database and cache, run the following commands:

```bash
# Ensure Docker is running
docker-compose up --build
```

This will start up the necessary services including:

- Spring Boot backend running on `http://localhost:8080`
- Redis cache service
- MySQL database
- Kafka broker

### 3. Access the Application

Once the services are up and running, you can access the backend API or web service at:

```bash
http://localhost:8080
```

To check if Redis or MySQL is running correctly, you can access their respective ports or use Docker's built-in exec command to connect to them directly.

### 4. Stopping the Application

To stop the application and all the running containers, you can use:

```bash
docker-compose down
```

## API Documentation

The application provides a set of RESTful APIs that can be accessed through `http://localhost:8080/api/`. More detailed API documentation can be found in the Swagger UI:

```bash
http://localhost:8080/swagger-ui/
```

## Real-time Notification System

The application also integrates WebSocket for real-time notifications. Once a user is connected, they will receive updates such as game notifications, events, and messages in real-time.

## Contribution

If you wish to contribute to this project, feel free to open a pull request or create issues in the GitHub repository.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

### Key Sections:
1. **Introduction**: Provides an overview of the project.
2. **Technologies Used**: Lists the technologies and tools integrated.
3. **How to Start the Application**: Step-by-step instructions on cloning, building, and starting the project.
4. **Access the Application**: Information on how to access the web application.
5. **API Documentation**: Refers to the Swagger UI for API details.
6. **Real-time Notification System**: Describes the WebSocket integration.
7. **Contribution**: Guidelines for contributing to the project.
8. **License**: Placeholder for license information.