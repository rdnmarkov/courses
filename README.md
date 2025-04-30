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
course_id INT,
message_id integer[] NOT NULL,
order_number INT NOT NULL,
FOREIGN KEY (course_id) REFERENCES Courses(course_id)
);

CREATE TABLE UserProgress (
user_id INT,
course_id INT,
lesson_id INT,
completed BOOLEAN DEFAULT FALSE,
PRIMARY KEY (user_id, course_id, lesson_id),
FOREIGN KEY (course_id) REFERENCES Courses(course_id),
FOREIGN KEY (lesson_id) REFERENCES Lessons(lesson_id)
);