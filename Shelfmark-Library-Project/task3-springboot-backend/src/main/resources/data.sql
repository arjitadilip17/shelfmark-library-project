-- Sample rows, inserted only if the table is empty, so restarting the app doesn't duplicate data.
MERGE INTO books (id, title, author, category, call_no, copies) KEY(id) VALUES
 (1, 'Clean Code', 'Robert C. Martin', 'Computer Science', '005.1 MAR', 3),
 (2, 'Introduction to Algorithms', 'Thomas H. Cormen', 'Computer Science', '005.1 COR', 0),
 (3, 'Database System Concepts', 'Abraham Silberschatz', 'Computer Science', '005.74 SIL', 5),
 (4, 'Wings of Fire', 'A. P. J. Abdul Kalam', 'History and Biography', '920 KAL', 2),
 (5, 'The Discovery of India', 'Jawaharlal Nehru', 'History and Biography', '954 NEH', 0),
 (6, 'Head First Java', 'Kathy Sierra', 'Computer Science', '005.13 SIE', 4),
 (7, 'Artificial Intelligence: A Modern Approach', 'Stuart Russell', 'Science and Technology', '006.3 RUS', 1),
 (8, 'Malgudi Days', 'R. K. Narayan', 'Literature', '823 NAR', 6);

MERGE INTO members (id, name, email, phone, plan) KEY(id) VALUES
 (1, 'Manoj', 'manoj@example.com', '9000000001', 'Student'),
 (2, 'Divya', 'divya@example.com', '9000000002', 'Student');

MERGE INTO loans (id, book_id, member_id, borrowed_on, due_date, returned_on) KEY(id) VALUES
 (1, 2, 1, CURRENT_DATE - 5, CURRENT_DATE + 9, NULL);

ALTER TABLE books RESTART WITH 9;
ALTER TABLE members RESTART WITH 3;
ALTER TABLE loans RESTART WITH 2;
