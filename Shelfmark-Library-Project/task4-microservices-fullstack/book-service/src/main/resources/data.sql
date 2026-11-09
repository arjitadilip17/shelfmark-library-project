MERGE INTO books (id, title, author, category, call_no, copies) KEY(id) VALUES
 (1, 'Clean Code', 'Robert C. Martin', 'Computer Science', '005.1 MAR', 3),
 (2, 'Introduction to Algorithms', 'Thomas H. Cormen', 'Computer Science', '005.1 COR', 0),
 (3, 'Database System Concepts', 'Abraham Silberschatz', 'Computer Science', '005.74 SIL', 5),
 (4, 'Wings of Fire', 'A. P. J. Abdul Kalam', 'History and Biography', '920 KAL', 2),
 (5, 'The Discovery of India', 'Jawaharlal Nehru', 'History and Biography', '954 NEH', 0),
 (6, 'Head First Java', 'Kathy Sierra', 'Computer Science', '005.13 SIE', 4),
 (7, 'Artificial Intelligence: A Modern Approach', 'Stuart Russell', 'Science and Technology', '006.3 RUS', 1),
 (8, 'Malgudi Days', 'R. K. Narayan', 'Literature', '823 NAR', 6);

ALTER TABLE books RESTART WITH 9;
