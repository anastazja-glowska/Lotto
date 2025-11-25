

# 🎲 Lotto Project

A **Spring Boot–based Lotto application** that allows clients to play Lotto by submitting numbers, receiving a unique ticket, and checking results against winning numbers retrieved from a remote server.  
This project demonstrates **secure API design, scheduling, caching, and robust testing practices** — built to showcase engineering maturity and production‑ready skills.

---

## ✨ Features

- **Play Lotto**: Clients submit exactly 6 unique numbers (range 1–99).  
- **Validation**: Numbers must be unique and within the allowed range.  
- **Ticket generation**: Each submission returns a **unique ticket ID** and the **draw date**.  
- **Draw schedule**: Draws occur **every Saturday at 12:00 PM** (`Spring Scheduler`).  
- **Result checking**: Clients can verify if they won and see how many numbers matched.  
- **Remote integration**: Winning numbers are fetched from a **remote HTTP server** (simulated with WireMock).  
- **Secure access**: Endpoints protected with **JWT authentication** (`Spring Security`).  
- **Caching**: Redis integration ensures efficient repeated queries.  
- **Duplicate prevention**: Offers/tickets are unique by identifier.  

---

## 🛠️ Tech Stack

- **Language & Frameworks**: Java 17, Spring Boot (Web, Data MongoDB, Security, Validation, Test)  
- **Database**: MongoDB + MongoExpress (UI)  
- **Caching**: Redis + Jedis + Redis Commander  
- **Testing**:  
  - Unit tests: JUnit 5, Mockito, AssertJ  
  - Integration tests: SpringBootTest, MockMvc, SpringSecurityTest  
  - Advanced testing: WireMock (mock external APIs), Testcontainers (real DB in Docker), Awaitility (async testing)  
- **Build & Dependency Management**: Maven  
- **Logging**: Log4j2  
- **API Documentation**: Swagger / OpenAPI  
- **DevOps & Tools**: Docker, Docker Compose, Docker Desktop, GitHub/GitLab CI/CD ready  
- **IDE**: IntelliJ IDEA Ultimate  
- **Version Control**: Git  

---

## 📐 Architecture Highlights

- **Facade pattern** for clean separation of concerns (`NumberReceiverFacade`, `NumberGeneratorFacade`, `ResultAnnouncerFacade`).  
- **Repository pattern** with in‑memory test implementations and MongoDB persistence.  
- **DTOs** for API contracts, ensuring validation and immutability.  
- **Integration tests** simulate real user flows (register → login → input numbers → check results).  
- **WireMock stubs** emulate external Lotto result providers.  
- **Testcontainers** spin up real MongoDB/Redis instances for reliable integration testing.  

---

## 🚀 Getting Started

### Prerequisites
- Java 17  
- Docker & Docker Compose  
- Maven  

### Run locally
```bash
# Build the project
mvn clean install

# Start services (MongoDB, Redis, MongoExpress, Redis Commander)
docker-compose up -d

# Run the application
mvn spring-boot:run
```

Swagger UI will be available at:  
👉 `http://localhost:8080/swagger-ui.html`

---

## 🧪 Testing

Run all tests:
```bash
mvn test
```

Highlights:
- **Unit tests** for domain logic  
- **Integration tests** with `SpringBootTest` + `MockMvc`  
- **Security tests** with `SpringSecurityTest`  
- **Async tests** with Awaitility  
- **External API stubbing** with WireMock  
- **Real DB tests** with Testcontainers  

---

## 🎯 Why This Project Matters

This project is more than a coding exercise — it demonstrates:

- Ability to design **secure, scalable APIs**  
- Hands‑on experience with **modern Java/Spring ecosystem**  
- Proficiency in **testing strategies** (unit, integration, async, external stubs, containers)  
- Comfort with **DevOps tooling** (Docker, CI/CD, GitHub/GitLab)  
- Writing **clean, maintainable code** with Lombok, DTOs, and layered architecture  

---

## 👩‍💻 Author

Developed by **Anastazja** — backend engineer passionate about **Java, Spring, and clean architecture**.  
Looking forward to building scalable systems ✨
email: anastazjaglowska12345@gmail.com
