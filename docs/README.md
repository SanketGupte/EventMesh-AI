# EventMesh AI - Microservices Event Processing Platform

A production-grade, event-driven microservices platform built with **Spring Boot 3.2.5** and **Apache Kafka 7.6.0**. This system provides scalable event ingestion, routing, and processing capabilities with features like retry policies, idempotency, dead-letter queues, and comprehensive event logging.

---

## 📋 Table of Contents

- [Project Overview](#project-overview)
- [Architecture](#architecture)
- [Technology Stack](#technology-stack)
- [Project Structure](#project-structure)
- [Microservices](#microservices)
- [Getting Started](#getting-started)
- [API Documentation](#api-documentation)
- [Configuration](#configuration)
- [Development](#development)
- [Building & Deployment](#building--deployment)
- [Contributing](#contributing)

---

## 🎯 Project Overview

**EventMesh AI** is an enterprise-grade event mesh platform designed to handle high-throughput event ingestion, intelligent routing, and reliable delivery. It follows event-driven architecture principles and provides a complete solution for organizations that need to process and route events across distributed systems.

### Key Features

✅ **Event Ingestion** - Accept events via REST APIs  
✅ **Intelligent Routing** - Route events based on configurable rules  
✅ **Kafka Integration** - Event streaming with Kafka  
✅ **Idempotency** - Prevent duplicate event processing  
✅ **Retry Policies** - Automatic retry with backoff strategies  
✅ **Dead Letter Queue** - Undeliverable events handling  
✅ **Event Logging** - Comprehensive event lifecycle tracking  
✅ **API Key Authentication** - Secure API access control  
✅ **Role-Based Access** - Admin and Viewer roles  
✅ **Multi-Environment Support** - Dev, SIT, UAT, Prod configurations  
✅ **Correlation Tracking** - Request correlation IDs for tracing  

---

## 🏗️ Architecture

### System Architecture Diagram

```
┌─────────────────────────────────────────────────────────┐
│                   Event Producers                       │
│              (External Systems/Applications)            │
└────────────────────┬────────────────────────────────────┘
                     │ HTTP REST APIs
                     ▼
┌─────────────────────────────────────────────────────────┐
│            INGESTION SERVICE (Port 8080)                │
│  ┌──────────────────────────────────────────────────┐  │
│  │ Event Controller: /api/v1/events                │  │
│  │ API Key Controller: /api/v1/api-keys            │  │
│  │ Event Validation & Kafka Producer               │  │
│  └──────────────────────────────────────────────────┘  │
└────────────────────┬────────────────────────────────────┘
                     │ Kafka Topics
                     ▼
        ┌────────────────────────────┐
        │   Apache Kafka (7.6.0)     │
        │  ┌──────────────────────┐  │
        │  │ raw-events-topic     │  │
        │  │ dlq-topic            │  │
        │  │ [custom topics]      │  │
        │  └──────────────────────┘  │
        └────────────────────────────┘
                     │ Kafka Consumer
                     ▼
┌─────────────────────────────────────────────────────────┐
│            ROUTING SERVICE (Port 8080)                  │
│  ┌──────────────────────────────────────────────────┐  │
│  │ Routing Controller: /api/v1/routing-rules        │  │
│  │ Event Log Controller: /api/v1/event-logs         │  │
│  │ Routing Rules Engine                             │  │
│  │ Event Processing & PostgreSQL Persistence        │  │
│  └──────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────┘
                     │
                     ▼
        ┌────────────────────────────┐
        │   PostgreSQL Database      │
        │  (Event Logs & Rules)      │
        └────────────────────────────┘
```

### Event Flow

```
1. Event Ingestion
   Client → REST API → Validation → Kafka Producer → raw-events-topic

2. Event Routing
   Kafka Consumer → Apply Routing Rules → Event Processing → DB Storage

3. Error Handling
   Processing Error → Retry Policy → Max Retries Exceeded → Dead Letter Queue

4. Event Tracking
   All events logged in EventLog entity with status tracking
```

---

## 💻 Technology Stack

| Component | Technology | Version |
|-----------|-----------|---------|
| **Language** | Java | 21 |
| **Framework** | Spring Boot | 3.2.5 |
| **Message Broker** | Apache Kafka | 7.6.0 |
| **Database** | PostgreSQL | Latest |
| **Build Tool** | Maven | 3.x |
| **Project Lombok** | Lombok | 1.18.44 |
| **Validation** | Jakarta Validation API | 3.1.1 |
| **Testing** | JUnit 4 | 4.13.2 |
| **Container** | Docker Compose | Latest |

### Dependencies

#### Core Dependencies
- `spring-boot-starter-web` - REST API support
- `spring-boot-starter-kafka` - Kafka integration
- `spring-boot-starter-data-jpa` - Database ORM
- `spring-boot-starter-validation` - Input validation
- `postgresql` - Database driver
- `jackson-databind` - JSON serialization
- `lombok` - Code generation

#### DevOps
- Docker & Docker Compose for containerization
- Maven for build automation

---

## 📁 Project Structure

```
EventMesh_AI/                          # Root Maven Multi-Module Project
│
├── pom.xml                            # Parent POM (Java 21, Spring Boot 3.2.5)
├── docker-compose.yml                 # Kafka container setup
├── README.md                          # This file
├── copilot-prompt.md                  # Project guidelines
├── HELP.md                            # Initial setup help
│
├── common-lib/                        # Shared Library Module
│   ├── pom.xml
│   └── src/main/java/com/eventmesh/common/
│       ├── ApiResponse.java           # Generic REST response wrapper
│       ├── constants/
│       │   └── KafkaTopics.java       # Kafka topic constants
│       ├── dto/
│       │   ├── EventDTO.java          # Event data transfer object
│       │   └── RoutingRule.java       # Routing rule DTO
│       ├── exception/
│       │   └── ErrorCode.java         # Error code enumeration
│       ├── filter/
│       │   └── CorrelationIdFilter.java # Request correlation tracking
│       ├── response/
│       │   └── ErrorResponse.java     # Error response structure
│       └── security/
│           └── AuthEntryPoint.java    # Security entry point
│
├── ingestion-service/                 # Event Ingestion Microservice
│   ├── pom.xml
│   ├── src/main/
│   │   ├── java/com/eventmesh/ingestion/
│   │   │   ├── IngestionServiceApplication.java  # Spring Boot main class
│   │   │   ├── auth/
│   │   │   │   ├── controller/
│   │   │   │   │   └── ApiKeyController.java     # API key management endpoints
│   │   │   │   └── service/
│   │   │   │       └── ApiKeyService.java        # API key business logic
│   │   │   ├── config/
│   │   │   │   ├── KafkaProducerConfig.java      # Kafka producer configuration
│   │   │   │   └── SecurityConfig.java           # Spring Security setup
│   │   │   ├── controller/
│   │   │   │   └── EventController.java          # Event ingestion REST endpoints
│   │   │   ├── exception/
│   │   │   │   └── GlobalExceptionHandler.java   # Centralized error handling
│   │   │   ├── producer/
│   │   │   │   └── EventProducer.java            # Kafka event producer
│   │   │   └── consumer/
│   │   │       └── EventConsumer.java            # Event consumption logic
│   │   └── resources/
│   │       ├── application.yaml                  # Default configuration
│   │       ├── application-dev.yml               # Development profile
│   │       ├── application-sit.yml               # SIT profile
│   │       ├── application-uat.yml               # UAT profile
│   │       └── application-prod.yml              # Production profile
│   └── src/test/
│       └── java/com/eventmesh/ingestionservice/
│           └── IngestionServiceApplicationTests.java
│
├── routing-service/                   # Event Routing Microservice
│   ├── pom.xml
│   ├── src/main/
│   │   ├── java/com/eventmesh/routing/
│   │   │   ├── RoutingServiceApplication.java    # Spring Boot main class
│   │   │   ├── config/
│   │   │   │   ├── KafkaConsumerConfig.java      # Kafka consumer configuration
│   │   │   │   └── DatabaseConfig.java           # Database configuration
│   │   │   ├── controller/
│   │   │   │   ├── RoutingController.java        # Routing rules REST endpoints
│   │   │   │   └── EventLogController.java       # Event log retrieval endpoints
│   │   │   ├── entity/
│   │   │   │   ├── EventLog.java                 # Event log JPA entity
│   │   │   │   └── RoutingRuleEntity.java        # Routing rule JPA entity
│   │   │   ├── enums/
│   │   │   │   └── EventStatus.java              # Event status enumeration
│   │   │   ├── exception/
│   │   │   │   ├── GlobalExceptionHandler.java   # Centralized error handling
│   │   │   │   └── DuplicateEventException.java  # Duplicate event exception
│   │   │   ├── service/
│   │   │   │   ├── RoutingService.java           # Core routing logic
│   │   │   │   ├── RoutingRuleService.java       # Routing rule management
│   │   │   │   ├── EventLogService.java          # Event log queries
│   │   │   │   ├── IdempotencyService.java       # Duplicate detection
│   │   │   │   ├── RetryPolicyService.java       # Retry handling
│   │   │   │   └── BackoffStrategy.java          # Exponential backoff
│   │   │   ├── consumer/
│   │   │   │   └── EventConsumer.java            # Kafka event consumer
│   │   │   ├── producer/
│   │   │   │   └── DeadLetterProducer.java       # DLQ producer
│   │   │   ├── repository/
│   │   │   │   ├── EventLogRepository.java       # Event log persistence
│   │   │   │   └── RoutingRuleRepository.java    # Routing rule persistence
│   │   └── resources/
│   │       ├── application.yaml                  # Default configuration
│   │       ├── application-dev.yml               # Development profile
│   │       ├── application-sit.yml               # SIT profile
│   │       ├── application-uat.yml               # UAT profile
│   │       ├── application-prod.yml              # Production profile
│   │       ├── static/                           # Static web resources
│   │       └── templates/                        # Thymeleaf templates
│   └── src/test/
│       └── java/com/eventmesh/routingservice/
│           └── RoutingServiceApplicationTests.java
│
└── target/                            # Build output directory
```

---

## 🚀 Microservices

### 1. Ingestion Service

**Purpose:** Accept events from external systems and publish them to Kafka

**Port:** 8080 (configurable via application.yaml)

**Key Responsibilities:**
- Validate incoming events
- Authenticate requests using API keys
- Serialize events to JSON
- Publish events to raw-events-topic
- Handle API key management (Admin operations)

**Technologies:** Spring Boot Web, Kafka Producer, Spring Security, JPA

### 2. Routing Service

**Purpose:** Consume events from Kafka, apply routing rules, and persist events

**Port:** 8080 (configurable via application.yaml)

**Key Responsibilities:**
- Consume events from raw-events-topic
- Apply routing rules based on event type
- Implement idempotency to prevent duplicates
- Retry failed events with backoff strategy
- Send undeliverable events to DLQ
- Log all events to PostgreSQL
- Expose REST APIs for rule management and event logs

**Technologies:** Spring Boot Web, Kafka Consumer, Spring Data JPA, PostgreSQL

---

## 🛠️ Getting Started

### Prerequisites

- **Java 21** or higher
- **Maven 3.6+**
- **Docker & Docker Compose** (for Kafka)
- **PostgreSQL 12+** (for routing-service persistence)
- **IDE:** IntelliJ IDEA, VS Code, or Eclipse

### Installation Steps

#### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/eventmesh-ai.git
cd EventMesh_AI
```

#### 2. Start Kafka using Docker Compose

```bash
docker-compose up -d
```

This will start Kafka in KRaft mode on port 9092.

#### 3. Set up PostgreSQL

Create a PostgreSQL database for the routing-service:

```sql
CREATE DATABASE eventmesh_db;
CREATE USER eventmesh_user WITH PASSWORD 'eventmesh_password';
ALTER ROLE eventmesh_user WITH CREATEDB;
GRANT ALL PRIVILEGES ON DATABASE eventmesh_db TO eventmesh_user;
```

#### 4. Configure Environment Profiles

Edit the `application-dev.yml` files in both services to match your database and Kafka configurations:

**ingestion-service/src/main/resources/application-dev.yml:**
```yaml
spring:
  kafka:
    bootstrap-servers: localhost:9092
  datasource:
    url: jdbc:postgresql://localhost:5432/eventmesh_db
    username: eventmesh_user
    password: eventmesh_password
```

**routing-service/src/main/resources/application-dev.yml:**
```yaml
spring:
  kafka:
    bootstrap-servers: localhost:9092
  datasource:
    url: jdbc:postgresql://localhost:5432/eventmesh_db
    username: eventmesh_user
    password: eventmesh_password
  jpa:
    hibernate:
      ddl-auto: update
```

#### 5. Build the Project

```bash
# Build all modules
mvn clean install

# Or build specific modules
mvn clean install -pl common-lib
mvn clean install -pl ingestion-service
mvn clean install -pl routing-service
```

#### 6. Run Services

**Terminal 1 - Ingestion Service:**
```bash
cd ingestion-service
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

**Terminal 2 - Routing Service:**
```bash
cd routing-service
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

#### 7. Verify Services are Running

- Ingestion Service: http://localhost:8080/api/v1/events
- Routing Service: http://localhost:8080/api/v1/routing-rules

---

## 📡 API Documentation

### Ingestion Service APIs

#### 1. Publish Event

**Endpoint:** `POST /api/v1/events`

**Headers:**
```
Content-Type: application/json
X-API-Key: your-api-key (if authentication enabled)
```

**Request Body:**
```json
{
  "eventId": "evt-12345",
  "eventType": "ORDER_CREATED",
  "source": "order-service",
  "timestamp": "2024-05-09T10:30:00Z",
  "payload": {
    "orderId": "ORD-001",
    "customerId": "CUST-123",
    "amount": 500.00,
    "currency": "USD"
  },
  "metadata": {
    "correlationId": "corr-abc123",
    "userId": "user-456"
  }
}
```

**Response (Success):**
```json
{
  "status": "SUCCESS",
  "message": "Event published successfully",
  "data": {
    "eventId": "evt-12345",
    "eventType": "ORDER_CREATED",
    ...
  },
  "timestamp": "2024-05-09T10:30:05Z"
}
```

**Response (Error):**
```json
{
  "status": "ERROR",
  "message": "Event validation failed: eventId is required",
  "data": null,
  "timestamp": "2024-05-09T10:30:05Z"
}
```

#### 2. Create API Key (Admin Only)

**Endpoint:** `POST /api/v1/api-keys`

**Parameters:**
- `clientName` (String): Name of the client application
- `role` (String): Role (ADMIN, VIEWER)

**Response:**
```json
{
  "status": "SUCCESS",
  "message": "API Key created successfully",
  "data": {
    "apiKey": "ak_xxxxxxxxxxxx",
    "clientName": "my-app",
    "role": "VIEWER",
    "createdAt": "2024-05-09T10:30:00Z"
  },
  "timestamp": "2024-05-09T10:30:05Z"
}
```

#### 3. Deactivate API Key (Admin Only)

**Endpoint:** `DELETE /api/v1/api-keys/{apiKey}`

**Response:**
```json
{
  "status": "SUCCESS",
  "message": "API Key deactivated successfully",
  "data": "ak_xxxxxxxxxxxx",
  "timestamp": "2024-05-09T10:30:05Z"
}
```

#### 4. Get All API Keys

**Endpoint:** `GET /api/v1/api-keys`

**Response:**
```json
{
  "status": "SUCCESS",
  "message": "API Keys retrieved successfully",
  "data": [
    {
      "apiKey": "ak_xxxxxxxxxxxx",
      "clientName": "my-app",
      "role": "VIEWER",
      "active": true,
      "createdAt": "2024-05-09T10:30:00Z"
    }
  ],
  "timestamp": "2024-05-09T10:30:05Z"
}
```

### Routing Service APIs

#### 1. Create Routing Rule

**Endpoint:** `POST /api/v1/routing-rules`

**Request Body:**
```json
{
  "eventType": "ORDER_CREATED",
  "destination": "warehouse-service",
  "active": true,
  "priority": 1,
  "conditions": {
    "minAmount": 100,
    "maxAmount": 10000
  }
}
```

**Response:**
```json
{
  "status": "SUCCESS",
  "message": "Routing rule created successfully",
  "data": {
    "id": 1,
    "eventType": "ORDER_CREATED",
    "destination": "warehouse-service",
    ...
  },
  "timestamp": "2024-05-09T10:30:05Z"
}
```

#### 2. Get All Routing Rules

**Endpoint:** `GET /api/v1/routing-rules`

**Response:**
```json
{
  "status": "SUCCESS",
  "message": "Routing rules retrieved successfully",
  "data": [
    {
      "id": 1,
      "eventType": "ORDER_CREATED",
      "destination": "warehouse-service",
      "active": true,
      "priority": 1
    }
  ],
  "timestamp": "2024-05-09T10:30:05Z"
}
```

#### 3. Delete Routing Rule by ID

**Endpoint:** `DELETE /api/v1/routing-rules/{id}`

**Response:**
```json
{
  "status": "SUCCESS",
  "message": "Routing rule deleted successfully",
  "data": "1",
  "timestamp": "2024-05-09T10:30:05Z"
}
```

#### 4. Delete Rules by Event Type

**Endpoint:** `DELETE /api/v1/routing-rules/event/{eventType}`

**Response:**
```json
{
  "status": "SUCCESS",
  "message": "Routing rules deleted for event type: ORDER_CREATED",
  "data": "ORDER_CREATED",
  "timestamp": "2024-05-09T10:30:05Z"
}
```

#### 5. Get All Event Logs

**Endpoint:** `GET /api/v1/event-logs`

**Response:**
```json
{
  "status": "SUCCESS",
  "message": "Event logs fetched successfully",
  "data": [
    {
      "id": 1,
      "eventId": "evt-12345",
      "eventType": "ORDER_CREATED",
      "status": "DELIVERED",
      "processedAt": "2024-05-09T10:30:05Z"
    }
  ],
  "timestamp": "2024-05-09T10:30:05Z"
}
```

#### 6. Get Event Logs by Status

**Endpoint:** `GET /api/v1/event-logs/status/{status}`

**Status Values:** PENDING, PROCESSING, DELIVERED, FAILED, DLQ

**Response:**
```json
{
  "status": "SUCCESS",
  "message": "Event logs fetched successfully",
  "data": [
    {
      "id": 1,
      "eventId": "evt-12345",
      "status": "FAILED",
      "failureReason": "Database connection timeout",
      "retryCount": 3,
      "processedAt": "2024-05-09T10:30:05Z"
    }
  ],
  "timestamp": "2024-05-09T10:30:05Z"
}
```

#### 7. Get Event Logs by Event Type

**Endpoint:** `GET /api/v1/event-logs/event/{eventType}`

**Response:** Same as Get All Event Logs, filtered by eventType

---

## ⚙️ Configuration

### Application Profiles

The project supports multiple environment profiles:

- **dev** - Development environment (local Kafka & PostgreSQL)
- **sit** - System Integration Testing
- **uat** - User Acceptance Testing
- **prod** - Production environment

### Kafka Configuration

**Topics:**
- `raw-events-topic` - Main event ingestion topic
- `dlq-topic` - Dead Letter Queue for failed events
- Custom topics can be created as needed

**KRaft Mode:**
The Docker Compose configuration uses KRaft mode (Kafka Raft) for standalone Kafka without Zookeeper.

### Database Configuration

**PostgreSQL:**
- Host: localhost
- Port: 5432 (configurable)
- Database: eventmesh_db
- User: eventmesh_user

**JPA Configuration:**
- DDL Auto: update (creates/updates tables automatically)
- Dialect: PostgreSQL10Dialect
- Show SQL: false (set to true for debugging)

### Spring Boot Properties

**Ingestion Service:**
```yaml
spring:
  application:
    name: eventmesh-ai
  profiles:
    active: dev
  kafka:
    bootstrap-servers: localhost:9092
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.springframework.kafka.support.serializer.JsonSerializer

server:
  port: 8080

logging:
  level:
    root: INFO
```

**Routing Service:**
```yaml
spring:
  application:
    name: eventmesh-ai
  profiles:
    active: dev
  kafka:
    bootstrap-servers: localhost:9092
    consumer:
      group-id: routing-service-group
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.springframework.kafka.support.serializer.JsonDeserializer
  jpa:
    hibernate:
      ddl-auto: update

server:
  port: 8080

logging:
  level:
    root: INFO
```

---

## 👨‍💻 Development

### Code Standards

Following the **copilot-prompt.md** guidelines:

- **Clean Code:** Write readable, maintainable code
- **Spring Boot Annotations:** Use appropriate annotations (@RestController, @Service, etc.)
- **Java Best Practices:** Follow SOLID principles and design patterns
- **Kafka Patterns:** Use KafkaTemplate for producers, @KafkaListener for consumers
- **DTOs:** Use Data Transfer Objects for API contracts
- **Exception Handling:** Centralized exception handling with GlobalExceptionHandler

### Project Guidelines

1. **Entity Design**
   - Use JPA annotations
   - Implement Lombok annotations for getters/setters
   - Include timestamp fields for audit trails

2. **Service Layer**
   - Business logic in service classes
   - Use Spring's dependency injection
   - Implement validation in services

3. **Controller Design**
   - REST conventions in API paths
   - Consistent response format using ApiResponse
   - Proper HTTP status codes

4. **Error Handling**
   - Custom exceptions for business logic errors
   - GlobalExceptionHandler for centralized handling
   - Meaningful error messages in responses

5. **Configuration**
   - Use @Configuration classes for bean definitions
   - Externalize properties in application.yaml
   - Environment-specific profiles

### Building the Project

```bash
# Clean and install all modules
mvn clean install

# Build without running tests
mvn clean install -DskipTests

# Build specific module
mvn clean install -pl routing-service

# Build with specific profile
mvn clean install -Dspring.profiles.active=prod
```

### Running Tests

```bash
# Run all tests
mvn test

# Run tests for specific module
mvn test -pl routing-service

# Run specific test class
mvn test -Dtest=RoutingServiceApplicationTests
```

---

## 🚢 Building & Deployment

### Creating Docker Images

```bash
# Build executable JAR
mvn clean package -DskipTests

# Build Docker image for Ingestion Service
cd ingestion-service
docker build -t eventmesh/ingestion-service:0.0.1 .

# Build Docker image for Routing Service
cd routing-service
docker build -t eventmesh/routing-service:0.0.1 .
```

### Docker Deployment

**docker-compose.yml for all services:**

```yaml
version: '3.8'
services:
  kafka:
    image: confluentinc/cp-kafka:7.6.0
    container_name: eventmesh-kafka
    ports:
      - "9092:9092"
    environment:
      KAFKA_NODE_ID: 1
      KAFKA_PROCESS_ROLES: broker,controller
      KAFKA_CONTROLLER_QUORUM_VOTERS: 1@kafka:9093
      KAFKA_LISTENERS: PLAINTEXT://:9092,CONTROLLER://:9093
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://kafka:9092
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: CONTROLLER:PLAINTEXT,PLAINTEXT:PLAINTEXT
      KAFKA_CONTROLLER_LISTENER_NAMES: CONTROLLER
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
      KAFKA_GROUP_INITIAL_REBALANCE_DELAY_MS: 0
      KAFKA_AUTO_CREATE_TOPICS_ENABLE: "true"
      CLUSTER_ID: "MkU3OEVBNTcwNTJENDM2Qk"

  postgres:
    image: postgres:15-alpine
    container_name: eventmesh-postgres
    ports:
      - "5432:5432"
    environment:
      POSTGRES_DB: eventmesh_db
      POSTGRES_USER: eventmesh_user
      POSTGRES_PASSWORD: eventmesh_password
    volumes:
      - postgres_data:/var/lib/postgresql/data

  ingestion-service:
    build: ./ingestion-service
    container_name: eventmesh-ingestion
    ports:
      - "8080:8080"
    environment:
      SPRING_PROFILES_ACTIVE: prod
      SPRING_KAFKA_BOOTSTRAP_SERVERS: kafka:9092
    depends_on:
      - kafka
    networks:
      - eventmesh-network

  routing-service:
    build: ./routing-service
    container_name: eventmesh-routing
    ports:
      - "8081:8080"
    environment:
      SPRING_PROFILES_ACTIVE: prod
      SPRING_KAFKA_BOOTSTRAP_SERVERS: kafka:9092
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/eventmesh_db
      SPRING_DATASOURCE_USERNAME: eventmesh_user
      SPRING_DATASOURCE_PASSWORD: eventmesh_password
    depends_on:
      - kafka
      - postgres
    networks:
      - eventmesh-network

volumes:
  postgres_data:

networks:
  eventmesh-network:
    driver: bridge
```

### Kubernetes Deployment

For Kubernetes deployments, create appropriate manifests:

- ConfigMap for application properties
- Secret for sensitive data (API keys, passwords)
- Deployment manifests for each service
- Service manifests for network exposure
- PersistentVolume for PostgreSQL storage
- StatefulSet for Kafka (if applicable)

---

## 📋 Environment Variables

### Ingestion Service

```bash
SPRING_PROFILES_ACTIVE=dev
SPRING_APPLICATION_NAME=eventmesh-ai
SPRING_KAFKA_BOOTSTRAP_SERVERS=localhost:9092
SERVER_PORT=8080
LOGGING_LEVEL_ROOT=INFO
```

### Routing Service

```bash
SPRING_PROFILES_ACTIVE=dev
SPRING_APPLICATION_NAME=eventmesh-ai
SPRING_KAFKA_BOOTSTRAP_SERVERS=localhost:9092
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/eventmesh_db
SPRING_DATASOURCE_USERNAME=eventmesh_user
SPRING_DATASOURCE_PASSWORD=eventmesh_password
SPRING_JPA_HIBERNATE_DDL_AUTO=update
SERVER_PORT=8080
LOGGING_LEVEL_ROOT=INFO
```

---

## 🤝 Contributing

### Guidelines

1. **Code Review:** All changes require code review
2. **Testing:** Write unit tests for new features
3. **Documentation:** Update README for API changes
4. **Commits:** Use meaningful commit messages
5. **Branches:** Create feature branches from develop

### Process

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

---

## 📝 License

This project is licensed under the MIT License - see LICENSE file for details.

---

## 🔗 Useful Links

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Kafka Documentation](https://kafka.apache.org/documentation/)
- [Spring Kafka Guide](https://spring.io/projects/spring-kafka)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [Docker Documentation](https://docs.docker.com/)
- [Maven Documentation](https://maven.apache.org/)

---

## ❓ FAQ

### Q: How to add a new Kafka topic?

A: Topics are auto-created by Kafka (KAFKA_AUTO_CREATE_TOPICS_ENABLE: "true"). You can create them manually:

```bash
docker exec eventmesh-kafka kafka-topics --bootstrap-server localhost:9092 --create --topic my-topic
```

### Q: How to view Kafka topics and messages?

A: Use Kafka CLI tools or UI tools like Kafdrop:

```bash
docker run -d -p 9000:9000 -e KAFKA_BROKERCONNECT=kafka:9092 obsidiandynamics/kafdrop
```

### Q: How to reset event logs?

A: Connect to PostgreSQL and truncate the event_log table:

```sql
TRUNCATE TABLE event_log CASCADE;
```

### Q: How to enable debug logging?

A: Update application-dev.yml:

```yaml
logging:
  level:
    root: DEBUG
    com.eventmesh: DEBUG
```

### Q: How to change port numbers?

A: Update server.port in respective application.yaml files.

---

## 📞 Support

For issues, questions, or suggestions:
- Open an issue on GitHub
- Check existing documentation
- Contact the development team

---

**Last Updated:** May 9, 2026  
**Project Version:** 0.0.1-SNAPSHOT  
**Java Version:** 21  
**Spring Boot Version:** 3.2.5  
**Kafka Version:** 7.6.0

