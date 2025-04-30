-- Очистка таблиц (опционально)
TRUNCATE TABLE lessons CASCADE;
TRUNCATE TABLE courses CASCADE;

-- Вставка курсов
INSERT INTO courses (course_id, title, visibility) VALUES
(1, 'Java Basics', true),
(2, 'Spring Boot Advanced', false),
(3, 'PostgreSQL for Developers', true);

-- Вставка уроков
INSERT INTO lessons (lesson_id, title, course_id, message_id, order_number) VALUES
-- Уроки для курса 1 (Java Basics)
(101, 'Introduction to Java', 1, ARRAY[1001, 1002], 1),
(102, 'OOP Principles', 1, ARRAY[1003], 2),
-- Уроки для курса 2 (Spring Boot Advanced)
(201, 'Spring Security', 2, ARRAY[2001, 2002, 2003], 1),
-- Уроки для курса 3 (PostgreSQL)
(301, 'SQL Queries', 3, ARRAY[3001], 1),
(302, 'Indexes and Performance', 3, ARRAY[3002, 3003], 2);