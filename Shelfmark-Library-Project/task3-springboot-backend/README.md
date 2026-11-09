# Task 3 — Database Connectivity using SQL, JDBC and Spring Boot

A REST API backed by a real SQL database, accessed through plain JDBC
(`JdbcTemplate`) rather than JPA/Hibernate, since the task specifically
names JDBC.

## Run it

```
mvn spring-boot:run
```

The app starts on `http://localhost:8080`. On first run it creates an H2
SQL database file under `./data/librarydb.mv.db`, runs `schema.sql` to
create the tables, and `data.sql` to insert sample rows.

**View the database directly:** go to `http://localhost:8080/h2-console`.
JDBC URL: `jdbc:h2:file:./data/librarydb`, username `sa`, password blank.
From there you can run SQL queries against `books`, `members`, and `loans`
and see them update as you use the React app.

## Database design

```
books   (id, title, author, category, call_no, copies)
members (id, name, email, phone, plan, joined_on)
loans   (id, book_id, member_id, borrowed_on, due_date, returned_on)
            -> book_id   references books(id)
            -> member_id references members(id)
```

`schema.sql` and `data.sql` in `src/main/resources/` hold the exact SQL.

## API endpoints

| Method | Path | Purpose |
|--------|------|---------|
| GET | `/api/books` | list all books |
| GET | `/api/books?q=clean&category=Computer Science` | search |
| GET | `/api/books/{id}` | one book |
| POST | `/api/books` | add a book |
| PUT | `/api/books/{id}` | edit a book |
| DELETE | `/api/books/{id}` | remove a book |
| GET | `/api/members` | list members |
| POST | `/api/members` | register a member |
| GET | `/api/loans` | all currently borrowed books |
| GET | `/api/loans/member/{memberId}` | one member's loans |
| POST | `/api/loans` | borrow a book: `{ "bookId": 1, "memberId": 1 }` |
| POST | `/api/loans/{loanId}/return` | return a book: `{ "bookId": 1 }` |

## How the JDBC layer works

`BookRepository`, `MemberRepository`, and `LoanRepository` in
`src/main/java/.../repository/` each hold hand-written SQL, run through
Spring's `JdbcTemplate`. A `RowMapper` turns each result row into a Java
object. `LoanService` wraps "borrow" and "return" in `@Transactional`
methods, so a copy count and a loan row are always updated together.

## Switching from H2 to MySQL

Install MySQL, create a database (e.g. `library_db`), then in
`application.properties` replace the four `spring.datasource.*` lines with:
```
spring.datasource.url=jdbc:mysql://localhost:3306/library_db
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.username=root
spring.datasource.password=yourpassword
```
and add the MySQL driver dependency to `pom.xml`:
```xml
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <scope>runtime</scope>
</dependency>
```
Everything else (schema.sql, data.sql, the repositories) stays the same.
