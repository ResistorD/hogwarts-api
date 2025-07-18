-- 1. Получить студентов от 10 до 15 лет
SELECT * FROM student WHERE age BETWEEN 10 AND 15;

-- 2. Получить факультеты по цвету (регистронезависимо)
SELECT * FROM faculty WHERE LOWER(color) = LOWER('green');

-- 3. Получить всех студентов факультета с id = 1
SELECT * FROM student WHERE faculty_id = 1;

-- 4. Добавить нового студента
INSERT INTO student (name, age, faculty_id) VALUES ('Hermione Granger', 11, 1);

-- 5. Удалить факультет по id
DELETE FROM faculty WHERE id = 3;
