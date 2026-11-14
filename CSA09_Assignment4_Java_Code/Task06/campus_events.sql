CREATE DATABASE campus_events;
USE campus_events;

CREATE TABLE events (
    event_id INT PRIMARY KEY AUTO_INCREMENT,
    event_name VARCHAR(100) NOT NULL
);

CREATE TABLE registrations (
    registration_id INT PRIMARY KEY AUTO_INCREMENT,
    student_id VARCHAR(30) NOT NULL,
    student_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    event_id INT NOT NULL,
    registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_event FOREIGN KEY (event_id)
        REFERENCES events(event_id),
    CONSTRAINT unique_student_event UNIQUE (student_id, event_id)
);

INSERT INTO events (event_name) VALUES
('Technical Symposium'),
('Cultural Festival'),
('Sports Meet'),
('Coding Competition');
