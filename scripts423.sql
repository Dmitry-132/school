SELECT s.name,
       s.age,
       f.name
FROM student s
         JOIN faculty f ON s.faculty_id = f.id; --JOIN - соединяет таблицы
-- краснота из-за отсутствия привязки к БД, но оно и не надо, пока проверяю через pgAdmin
SELECT s.id,
       s.name,
       s.age
FROM student s
         JOIN avatar a ON s.id = a.student_id;
