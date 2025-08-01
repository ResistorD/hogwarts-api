-- Ограничение возраста студента
ALTER TABLE student
    ADD CONSTRAINT chk_student_age CHECK (age >= 16);

-- Уникальные имена студентов и запрет NULL
ALTER TABLE student
    ALTER COLUMN name SET NOT NULL;

ALTER TABLE student
    ADD CONSTRAINT uq_student_name UNIQUE (name);

-- Уникальная пара "название факультета + цвет"
ALTER TABLE faculty
    ADD CONSTRAINT uq_faculty_name_color UNIQUE (name, color);

-- Значение по умолчанию для возраста студента
ALTER TABLE student
    ALTER COLUMN age SET DEFAULT 20;
