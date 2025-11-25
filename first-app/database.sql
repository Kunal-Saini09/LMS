-- Create and setup Library Management System Database
CREATE DATABASE IF NOT EXISTS Library;
USE Library;

CREATE TABLE Student (
    studentId VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    branch VARCHAR(100) NOT NULL
);

CREATE TABLE Book (
    bookId VARCHAR(20) PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    publisher VARCHAR(100) NOT NULL,
    quantity INT DEFAULT 0
);

CREATE TABLE Librarian (
    librarianId VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL
);

CREATE TABLE IssueReturn (
    transactionId INT AUTO_INCREMENT PRIMARY KEY,
    studentId VARCHAR(20),
    bookId VARCHAR(20),
    issueDate DATE NOT NULL,
    returnDate DATE,
    FOREIGN KEY (studentId) REFERENCES Student(studentId),
    FOREIGN KEY (bookId) REFERENCES Book(bookId)
);

-- Optional: Insert sample data
INSERT INTO Librarian (librarianId, name, password) VALUES 
('lib001', 'Mrs. Pooja', 'pass123'),
('lib002', 'Mr. Manoj', 'pass456');

INSERT INTO Book (bookId, title, publisher, quantity) VALUES 
('b001', 'Java Basics', 'Oracle', 5),
('b002', 'Data Structures', 'McGraw Hill', 3);
