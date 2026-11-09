CREATE TABLE IF NOT EXISTS books (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    title      VARCHAR(200) NOT NULL,
    author     VARCHAR(150) NOT NULL,
    category   VARCHAR(100) NOT NULL,
    call_no    VARCHAR(40)  NOT NULL,
    copies     INT NOT NULL DEFAULT 0
);
