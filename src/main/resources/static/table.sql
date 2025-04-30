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