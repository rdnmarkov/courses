курсы-программирование

- Создать приложение
- подключить бд

docker run --name postgres-courses -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=courses -p 5432:5432 -d postgres:latest

- научить пересылать сообщения из другого чата
- создать таблицы в бд

CREATE TABLE Courses (
course_id INT PRIMARY KEY,
title VARCHAR(255) NOT NULL,
visibility BOOLEAN NOT NULL
);

CREATE TABLE Lessons (
lesson_id INT PRIMARY KEY,
title VARCHAR(255) NOT NULL,
course_id INT,
message_id integer[] NOT NULL,
order_number INT NOT NULL,
FOREIGN KEY (course_id) REFERENCES Courses(course_id)
);