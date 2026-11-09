# Shelfmark Library — Online Library Management System

A comprehensive, full-stack, enterprise-grade course submission spanning four progressive engineering milestones, from responsive semantic markup to cloud-native microservices:

```
Shelfmark-Library-Project/
├── task1-html-css/                 Task 1 — Responsive Web Page Design (HTML5 & CSS3) [Bloom's K2, K3]
├── task2-react-frontend/           Task 2 — Interactive JavaScript & ReactJS Application [Bloom's K3, K4]
├── task3-springboot-backend/       Task 3 — Database Connectivity via SQL, JDBC & Spring Boot [Bloom's K3, K4]
└── task4-microservices-fullstack/  Task 4 — Full Stack Web Application with Spring Boot & Microservices [Bloom's K6]
```

---

## Task Breakdown & Academic Mapping

| Task | Bloom's Level | Focus Area | Technologies | Key Deliverables |
| :--- | :--- | :--- | :--- | :--- |
| **Task 1** | K2, K3 | Responsive Web Design | HTML5 semantic tags, CSS3 Grid/Flexbox, Custom Properties | `index.html`, `style.css` (No JS needed, 100% accessible) |
| **Task 2** | K3, K4 | Interactive Client App | React 19, Hooks (`useState`, `useMemo`), controlled forms, REST | `App.jsx`, `BookGrid.jsx`, `MyLoans.jsx`, `RegisterForm.jsx`, `api.js` |
| **Task 3** | K3, K4 | Monolithic Backend & SQL | Spring Boot 3, JDBC (`JdbcTemplate`), H2/MySQL DDL/DML, REST | `schema.sql`, `data.sql`, `BookRepository.java`, `LoanService.java` |
| **Task 4** | K6 | Cloud-Native Microservices | Eureka, Spring Cloud Gateway, Docker Compose, 3x Microservices | `docker-compose.yml`, Eureka (8761), Gateway (8080), Book/Member/Loan Services |

---

## How to Run Each Task

### Task 1: Responsive Web Page (Standalone)
Simply open `task1-html-css/index.html` in any web browser. Zero build steps or dependencies required.

### Task 2: React Interactive Frontend
```bash
cd task2-react-frontend
npm install
npm run dev
```
Open `http://localhost:5173`. Works in standalone demo mode with responsive fallback data, or automatically syncs with the live Spring Boot API on port 8080.

### Task 3: Spring Boot Monolithic Backend (SQL + JDBC)
```bash
cd task3-springboot-backend
./mvnw spring-boot:run   # or mvn spring-boot:run
```
- API Base URL: `http://localhost:8080/api`
- Embedded SQL H2 Console: `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:file:./data/librarydb`, user: `sa`, password: blank)

### Task 4: Full Stack Microservices Ecosystem (Docker Compose or Maven)

#### Option A: One-Command Docker Compose (Recommended)
```bash
cd task4-microservices-fullstack
docker-compose up --build
```
Spins up the entire ecosystem:
- **Service Registry (Eureka)**: `http://localhost:8761`
- **API Gateway**: `http://localhost:8080` (Single Entrypoint)
- **Book Microservice**: `http://localhost:8081` (Port 8081, isolated `bookdb`)
- **Member Microservice**: `http://localhost:8082` (Port 8082, isolated `memberdb`)
- **Loan Microservice**: `http://localhost:8083` (Port 8083, isolated `loandb`)
- **Full-Stack Frontend**: `http://localhost:3000`

#### Option B: Standalone Terminal Execution
Run each service in order:
1. `cd task4-microservices-fullstack/eureka-server && mvn spring-boot:run`
2. `cd task4-microservices-fullstack/book-service && mvn spring-boot:run`
3. `cd task4-microservices-fullstack/member-service && mvn spring-boot:run`
4. `cd task4-microservices-fullstack/loan-service && mvn spring-boot:run`
5. `cd task4-microservices-fullstack/api-gateway && mvn spring-boot:run`
6. `cd task2-react-frontend && npm run dev`

---

## Microservices Architecture & Design Patterns (Task 4)

1. **API Gateway Pattern**: Single reverse-proxy entrypoint at port 8080 shielding internal microservices from external clients.
2. **Service Discovery Pattern**: Dynamic registration with Netflix Eureka on port 8761.
3. **Database-Per-Service Pattern**: Complete schema isolation (`bookdb`, `memberdb`, `loandb`) preventing distributed coupling.
4. **Inter-Service REST Contracts**: Synchronous REST calls between `loan-service` and `book-service`/`member-service` for atomic stock decrement, member validation, and return reconciliations.
5. **Overdue Fine Engine**: Real-time calculation of late return penalties (Rs. 2/day past the due date).
