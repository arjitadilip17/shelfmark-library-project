# Task 4 — Full Stack Web Application with Spring Boot and Microservices

Bloom's Taxonomy Level: **K6 (Creation / Synthesis)**
Course Requirements: CLA-II Submission

## 1. Architectural Overview

Shelfmark Library is decomposed from the Task 3 monolithic backend into a resilient, decoupled **Microservices Ecosystem** following industry best practices:

```
                            [ React Full Stack Frontend ] (Port 3000 / 5173)
                                          │
                                          ▼
                         [ Spring Cloud API Gateway ] (Port 8080)
                             /api/books   /api/members   /api/loans
                                 │              │             │
                ┌────────────────┼──────────────┼─────────────┼────────────────┐
                │                ▼              ▼             ▼                │
     [ Eureka Registry ]   [Book Service] [Member Service] [Loan Service]      │
        (Port 8761)         (Port 8081)     (Port 8082)     (Port 8083)        │
                                 │              │             │                │
                                 ▼              ▼             ▼                │
                             [bookdb]       [memberdb]     [loandb]            │
                         (Database-per-service pattern - Isolated SQL Stores)  │
```

## 2. Microservice Decomposition

| Microservice | Port | Technology | Database | Key Responsibility |
|--------------|------|------------|----------|--------------------|
| **Eureka Server** | `8761` | Spring Cloud Netflix Eureka | None | Service Registry, heartbeat monitoring, dynamic discovery |
| **API Gateway** | `8080` | Spring Cloud Gateway | None | Single entrypoint, CORS filter, dynamic load-balanced routing |
| **Book Service** | `8081` | Spring Boot, JDBC, SQL | `bookdb` (H2/MySQL) | Book catalogue CRUD, search, category filtering, inventory |
| **Member Service** | `8082` | Spring Boot, JDBC, SQL | `memberdb` (H2/MySQL) | User profiles, student/faculty plan tiers, registration |
| **Loan Service** | `8083` | Spring Boot, REST, SQL | `loandb` (H2/MySQL) | Borrow/return circulation engine, due dates, overdue fine logic |
| **Frontend** | `5173` / `3000` | React 19, Hooks, Vite | Browser / API | Real-time interactive UI, member switcher, microservice monitor |

## 3. Inter-Service Communication Flow

When a user borrows a book:
1. Client POSTs `{ "bookId": 3, "memberId": 1 }` to the **API Gateway** (`http://localhost:8080/api/loans`).
2. API Gateway routes the request to **Loan Service** (`http://localhost:8083`).
3. Loan Service initiates an HTTP GET to **Member Service** (`http://localhost:8082/api/members/1`) to verify account status and determine loan duration (Student: 14 days, Faculty: 30 days).
4. Loan Service initiates an HTTP GET to **Book Service** (`http://localhost:8081/api/books/3`) to verify copy count > 0.
5. Loan Service calls Book Service (`PUT /api/books/3/decrement`) to atomically decrement the physical copy count.
6. Loan Service inserts a new loan record into `loandb` and returns the enriched Loan response (`201 Created`).

When a user returns a book:
1. Client POSTs to `/api/loans/{id}/return`.
2. Loan Service updates `loandb` with `returned_on = CURRENT_DATE`.
3. If overdue (past `due_date`), calculates fine at **Rs. 2 / day**.
4. Loan Service calls Book Service (`PUT /api/books/{id}/increment`) to restore available copies.

## 4. How to Run

### Option A: One-Command Docker Compose (Recommended)
```bash
cd task4-microservices-fullstack
docker-compose up --build
```
This spins up all 6 containers with automatic service discovery and health networking!

### Option B: Run Services Individually with Maven
1. Start Eureka Server:
   ```bash
   cd eureka-server && mvn spring-boot:run
   ```
2. Start Book Service:
   ```bash
   cd book-service && mvn spring-boot:run
   ```
3. Start Member Service:
   ```bash
   cd member-service && mvn spring-boot:run
   ```
4. Start Loan Service:
   ```bash
   cd loan-service && mvn spring-boot:run
   ```
5. Start API Gateway:
   ```bash
   cd api-gateway && mvn spring-boot:run
   ```
6. Start Frontend:
   ```bash
   cd ../task2-react-frontend && npm run dev
   ```

## 5. Bloom's Taxonomy Level K6 Justification

- **Creation & Design**: Synthesized a distributed microservices ecosystem replacing the monolithic architecture.
- **Architectural Patterns Applied**:
  - API Gateway Pattern
  - Service Registry & Discovery Pattern
  - Database-per-Service Pattern (Decoupled schemas)
  - Inter-Service Synchronous REST Communication with Fallback
  - Containerization via Docker & Docker Compose
