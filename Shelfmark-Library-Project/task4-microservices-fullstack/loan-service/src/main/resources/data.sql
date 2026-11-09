MERGE INTO loans (id, book_id, member_id, borrowed_on, due_date, returned_on, fine_amount) KEY(id) VALUES
 (1, 2, 1, CURRENT_DATE - 5, CURRENT_DATE + 9, NULL, 0.00);

ALTER TABLE loans RESTART WITH 2;
