USE hay_data;
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);

INSERT INTO users (name, email, password, role) VALUES
('Alice Smith', 'alice.smith@example.com', 'password123', 'user'),
('Bob Johnson', 'bob.johnson@example.com', 'password456', 'admin'),
('Carol Williams', 'carol.williams@example.com', 'password789', 'user'),
('David Brown', 'david.brown@example.com', 'password101', 'user'),
('Eva Davis', 'eva.davis@example.com', 'password202', 'admin'),
('Frank Miller', 'frank.miller@example.com', 'password303', 'user'),
('Grace Wilson', 'grace.wilson@example.com', 'password404', 'user'),
('Hank Moore', 'hank.moore@example.com', 'password505', 'admin'),
('Ivy Taylor', 'ivy.taylor@example.com', 'password606', 'user'),
('Jack Anderson', 'jack.anderson@example.com', 'password707', 'user');

-- Create the Users table
CREATE TABLE Users (
    ID BIGINT PRIMARY KEY NOT NULL,
    Email VARCHAR(50) UNIQUE,
    Password VARCHAR(100),
    Phone VARCHAR(15) UNIQUE,
    Fullname VARCHAR(50),
    Birthday DATE,
    Gender BOOLEAN,
    Address VARCHAR(100),
    Create_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    Update_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    Error_count INT DEFAULT 0,
    Banned_time DATETIME DEFAULT NULL,
    Active BOOLEAN DEFAULT TRUE
);

INSERT INTO Users (ID, Email, Password, Phone, Fullname, Birthday, Gender, Address)
VALUES(1234567890,'haycaibat123@gmail.com', '$2a$10$iQ6XSyr0UgpUoG.qGVA8WuqVflCj6losf4cF1mJV92f4DLunbAcCy', '0868916170', 'Dư Trường Hây', '2003-04-21', TRUE, 'Cà Mau'),
(2345673452,'thinhnh123@gmail.com', '$2a$10$FNe3WDYVwz00tA822QDUXe6YiQ1VJxyLemyWMfykagHDwBV80hCaq', '0941599055', 'Nguyễn Hưng Thịnh', '2003-06-21', TRUE, 'Cà Mau');

-- Create the Roles table
CREATE TABLE Roles (
    Id VARCHAR(10) PRIMARY KEY NOT NULL,
    Name VARCHAR(50)
);

INSERT INTO Roles
VALUES('ADMIN', 'Administor'),
('USER', 'User default'),
('EMPLOYEE', 'Employee'),
( 'ADMIN_API', 'Admin API');

-- Create the Authorities table
CREATE TABLE Authorities (
    Id BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    UserID BIGINT,
    RoleId VARCHAR(10),
    FOREIGN KEY (UserID) REFERENCES Users(Id),
    FOREIGN KEY (RoleId) REFERENCES Roles(Id)
);

INSERT INTO Authorities (UserID, RoleId)
VALUES(1234567890, 'ADMIN'),
(1234567890, 'USER'),
(1234567890, 'EMPLOYEE');
