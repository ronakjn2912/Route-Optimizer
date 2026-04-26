# 🗺️ Route Optimizer — A Spring Boot Learning Project

> This isn't a production-grade app. It's a learning journal in the form of code.

---

## 📖 What is this?

Route Optimizer is a simple backend project I built to understand **how a real Spring Boot project is structured and developed — end to end**.

The core idea is straightforward:
> Given any two hubs (start → destination), find the shortest/most efficient path automatically.

But the *real* goal was never the idea itself. It was to walk through every meaningful stage of backend development — from understanding the problem, to writing business logic, to wiring up security, to writing tests — and actually learn each layer properly.

---

## 🧠 How I approached it

Before writing a single line of code, I tried to think like a developer would on a real project:

1. **Understand the problem** — What does "route optimization" even mean? What are the inputs and outputs?
2. **Figure out the business logic** — What rules govern a valid route? What makes one path better than another?
3. **Plan the structure** — How should the folders, layers, and responsibilities be organized?
4. **Code it up** — Then, and only then, start building.

This flow made a huge difference in how intentional the code felt.

---

## 🛠️ Tech Stack

| Tool | Purpose |
|---|---|
| **Java + Spring Boot** | Core backend framework |
| **H2 (in-memory)** | Database for development & testing |
| **IntelliJ IDEA** | IDE |
| **Postman** | API testing & exploration |
| **JUnit 5** | Unit testing |
| **Mockito** | Service-level mocking |
| **Spring MockMvc** | Integration testing |

---

## 📁 Project Structure

Follows the conventional **Spring MVC layered architecture**:

```
src/
├── controller/       # Handles HTTP requests, delegates to service
├── service/          # Business logic lives here
├── repository/       # Data access layer (Spring Data JPA)
├── entity/           # JPA entities (mapped to DB tables)
├── dto/              # Data Transfer Objects (what goes in/out of APIs)
├── utils/            # Utility/helper classes
├── exception/        # Global exception handling
└── security/         # Basic security configuration
```

Each layer has one job. That separation made it much easier to reason about the code and write tests for it.

---

## 💡 Concepts I Learned & Implemented

### 🏗️ Architecture
- **Spring MVC pattern** — Controller → Service → Repository, cleanly separated
- **REST API design** — Proper use of HTTP methods, status codes, and resource-based URLs
- **DTOs vs Entities** — Keeping the API contract decoupled from the database model

### 🔒 Security & Validation
- **Basic Security Config** — Set up Spring Security with basic access control
- **Bean Validation** — Used annotations like `@NotNull`, `@Size`, etc. on entity/DTO classes to enforce input rules at the boundary

### 🧰 Best Practices
- **Constructor Injection** — Chose constructor injection over field injection for cleaner, testable, and immutable dependencies
- **SLF4J Logging** — Added structured logging across layers to trace request flows and debug easily
- **Global Exception Handling** — Used `@ControllerAdvice` + `@ExceptionHandler` to handle errors in one place and return consistent error responses

### 🧪 Testing
- **JUnit 5** — Unit tests for core logic
- **Mockito** — Mocked dependencies at the service layer to test logic in isolation
- **Spring MockMvc** — Integration tests to verify the full request-response cycle through the controller layer

---

## 🚀 Running the Project

### Prerequisites
- Java 17+
- Maven

### Steps

```bash
# Clone the repo
git clone https://github.com/your-username/route-optimizer.git
cd route-optimizer

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The app starts on `http://localhost:8080` by default.

H2 console is available at: `http://localhost:8080/h2-console`

---

## 🔗 API Overview

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/v1/hubs` | Register a new hub/location |
| `GET` | `/api/v1/hubs` | List all available hubs |
| `POST` | `/api/v1/shortest-path` | Find the optimal route between two hubs |

> Full request/response examples can be explored via the Postman collection (if shared) or directly through the H2 console.

---

## ⚡ Performance Testing

Tested API performance using **Postman Collection Runner** to simulate real load on each endpoint.

| Endpoint | Total Calls | Avg Response Time |
|---|---|---|
| `POST /api/hubs` (Hub Creation) | 500 | 209ms |
| `POST /api/routes` (Route Creation) | 800 | 172ms |
| `GET /api/routes/shortest` (Shortest Path) | 214 | 27ms |

### Observations

- The **shortest path finder** was the fastest at just **27ms avg**, which makes sense —
  it's a read-heavy operation with no writes involved.
- **Route creation** handled the highest load (800 calls) and still came in under 200ms,
  which shows the service layer logic scales reasonably well.
- **Hub creation** was the slowest at 209ms, likely due to the overhead of validation +
  write operations on each call.

> Tested locally using H2 in-memory database. Numbers will vary in a deployed environment
> with a persistent DB, but this gives a solid baseline for how each layer behaves under load.

---

## 🧪 Running Tests

```bash
mvn test
```

Tests are organized into:
- **Unit tests** — Service layer logic tested with Mockito
- **Integration tests** — Controller layer tested with Spring MockMvc

---

## 🙋 Why I built this

I didn't want to build another CRUD app and call it a Spring Boot project. I wanted to go through the *process* — the thinking, the structuring, the testing, the debugging — and document that journey in code. This repo is the result of that.

---

*Built with curiosity, not deadlines.* ☕
