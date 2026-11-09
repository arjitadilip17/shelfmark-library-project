MERGE INTO members (id, name, email, phone, plan) KEY(id) VALUES
 (1, 'Manoj', 'manoj@example.com', '9000000001', 'Student'),
 (2, 'Divya', 'divya@example.com', '9000000002', 'Student');

ALTER TABLE members RESTART WITH 3;
