CREATE TABLE IF NOT EXISTS loans (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    book_id     BIGINT NOT NULL,
    member_id   BIGINT NOT NULL,
    borrowed_on DATE NOT NULL DEFAULT CURRENT_DATE,
    due_date    DATE NOT NULL,
    returned_on DATE,
    fine_amount DECIMAL(10,2) DEFAULT 0.00
);
