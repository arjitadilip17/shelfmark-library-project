-- Task 3: Database Connectivity using SQL, JDBC and Spring Boot
-- These statements run automatically on application startup (see application.properties).

CREATE TABLE IF NOT EXISTS books (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    title      VARCHAR(200) NOT NULL,
    author     VARCHAR(150) NOT NULL,
    category   VARCHAR(100) NOT NULL,
    call_no    VARCHAR(40)  NOT NULL,
    copies     INT NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS members (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(150) NOT NULL,
    email      VARCHAR(150) NOT NULL UNIQUE,
    phone      VARCHAR(15)  NOT NULL,
    plan       VARCHAR(20)  NOT NULL,
    joined_on  DATE NOT NULL DEFAULT CURRENT_DATE
);

CREATE TABLE IF NOT EXISTS loans (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    book_id     BIGINT NOT NULL,
    member_id   BIGINT NOT NULL,
    borrowed_on DATE NOT NULL DEFAULT CURRENT_DATE,
    due_date    DATE NOT NULL,
    returned_on DATE,
    CONSTRAINT fk_loan_book   FOREIGN KEY (book_id)   REFERENCES books(id),
    CONSTRAINT fk_loan_member FOREIGN KEY (member_id) REFERENCES members(id)
);
