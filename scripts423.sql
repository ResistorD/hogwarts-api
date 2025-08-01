-- 1. Имена и возраст студентов + названия факультетов
SELECT s.name AS student_name,
       s.age AS student_age,
       f.name AS faculty_name
FROM student s
INNER JOIN faculty f ON s.faculty_id = f.id;

-- 2. Студенты, у которых есть аватарки
SELECT s.name AS student_name
FROM student s
INNER JOIN avatar a ON s.id = a.student_id;
