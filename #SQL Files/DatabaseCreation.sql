CREATE DATABASE Tution_Management_System;
GO

Drop Database Tution_Management_System;
GO

use master;
GO

USE Tution_Management_System;
GO

CREATE TABLE Student (
    Stu_ID nvarchar(10) PRIMARY KEY,
    Stu_Name nvarchar(100) NOT NULL,
    Stu_Birthdate date CHECK (Stu_Birthdate <= GETDATE()),
    Stu_Gender nvarchar(10) CHECK (Stu_Gender in ('Male', 'Female', 'Other')),
    Stu_Mobile nvarchar(10) NOT NULL CHECK (Stu_Mobile LIKE '0%' and Stu_Mobile NOT LIKE '%[^0-9]%' and LEN(Stu_Mobile) = 10),
    Stu_Registration_Date date NOT NULL DEFAULT GETDATE(),
)
GO

CREATE table Staff (
    Staff_ID nvarchar(10) PRIMARY KEY,
    Staff_Name nvarchar(100) NOT NULL,
    Staff_Birthdate date CHECK (Staff_Birthdate <= GETDATE()),
    Staff_Gender nvarchar(10) CHECK (Staff_Gender in ('Male', 'Female', 'Other')),
    Staff_Mobile nvarchar(10) NOT NULL CHECK (Staff_Mobile LIKE '0%' and Staff_Mobile NOT LIKE '%[^0-9]%' and LEN(Staff_Mobile) = 10),
    Staff_Role nvarchar(50) NOT NULL CHECK (Staff_Role in ('Admin', 'Teacher', 'Employee')),
    Staff_Joining_Date date NOT NULL DEFAULT GETDATE(),
)
GO

CREATE TABLE Class (
    Class_ID nvarchar(10) PRIMARY KEY,
    Class_Name nvarchar(100) NOT NULL,
    Class_Level nvarchar(50) NOT NULL CHECK (Class_Level in ('Beginner', 'Intermediate', 'Advanced')),
    Class_Timetable nvarchar(500) NOT NULL CHECK (
        Class_Timetable NOT LIKE '%[^0-9: ,MondayTuesdayWednesdayThursdayFridaySaturdaySunday]%' 
        AND Class_Timetable LIKE '%[0-2][0-9]:[0-5][0-9] [A-Za-z]%'
    ),
    Class_Start_Date date NOT NULL,
    Class_End_Date date NOT NULL,
    Class_Capacity int NOT NULL CHECK (Class_Capacity > 0),
    Class_Fee int NOT NULL CHECK (Class_Fee >= 0),
    Staff_ID nvarchar(10) NOT NULL REFERENCES Staff(Staff_ID),
    CONSTRAINT CK_Class_End_After_Start CHECK (Class_End_Date >= Class_Start_Date)
)
GO

CREATE TABLE Enrollment (
    Enrollment_ID nvarchar(10) PRIMARY KEY,
    Stu_ID nvarchar(10) NOT NULL REFERENCES Student(Stu_ID),
    Class_ID nvarchar(10) NOT NULL REFERENCES Class(Class_ID),
    Enrollment_Date date NOT NULL,
    Enrollment_Status nvarchar(20) NOT NULL CHECK (Enrollment_Status in ('Active', 'Inactive', 'Completed')),
)

CREATE TABLE Bill (
    Bill_ID nvarchar(10) PRIMARY KEY,
    Enrollment_ID nvarchar(10) NOT NULL REFERENCES Enrollment(Enrollment_ID),
    Bill_Date date NOT NULL DEFAULT GETDATE(),
    Bill_Payment_Status nvarchar(20) NOT NULL CHECK (Bill_Payment_Status in ('Paid', 'Unpaid', 'Pending')),
    Bill_Payment_Method nvarchar(50) NOT NULL CHECK (Bill_Payment_Method in ('Cash', 'Card', 'Bank Transfer')),
    Bill_Note nvarchar(500),
)
GO

CREATE TABLE LOGIN (
    Staff_ID nvarchar(10) PRIMARY KEY REFERENCES Staff(Staff_ID),
    Login_Password nvarchar(255) NOT NULL,
)

CREATE SEQUENCE Seq_StudentID START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE Seq_StaffID START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE Seq_ClassID START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE Seq_EnrollmentID START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE Seq_BillID START WITH 1 INCREMENT BY 1;
GO

-- Trigger to automatically generate Stu_ID for new students
CREATE TRIGGER trg_StudentID_Insert
ON Student
INSTEAD OF INSERT
AS
BEGIN
    DECLARE @year CHAR(2) = RIGHT(CAST(YEAR(GETDATE()) AS CHAR(4)), 2);
    DECLARE @nextNum BIGINT = NEXT VALUE FOR Seq_StudentID;
    DECLARE @id CHAR(10) = @year + 'A' + RIGHT('0000000' + CAST(@nextNum AS VARCHAR(7)), 7);

    INSERT INTO Student (
        Stu_ID, Stu_Name, Stu_Birthdate, Stu_Gender, Stu_Mobile, Stu_Registration_Date
    )
    SELECT
        @id, Stu_Name, Stu_Birthdate, Stu_Gender, Stu_Mobile, Stu_Registration_Date
    FROM inserted;
END
GO

-- Trigger for Staff ID
CREATE TRIGGER trg_StaffID_Insert
ON Staff
INSTEAD OF INSERT
AS
BEGIN
    DECLARE @year CHAR(2) = RIGHT(CAST(YEAR(GETDATE()) AS CHAR(4)), 2);
    DECLARE @nextNum BIGINT = NEXT VALUE FOR Seq_StaffID;
    DECLARE @id CHAR(10) = @year + 'S' + RIGHT('0000000' + CAST(@nextNum AS VARCHAR(7)), 7);

    INSERT INTO Staff (
        Staff_ID, Staff_Name, Staff_Birthdate, Staff_Gender, Staff_Mobile, Staff_Role, Staff_Joining_Date
    )
    SELECT
        @id, Staff_Name, Staff_Birthdate, Staff_Gender, Staff_Mobile, Staff_Role, Staff_Joining_Date
    FROM inserted;
END
GO


CREATE TRIGGER trg_Class_Insert_OnlyTeacherAndID
ON Class
INSTEAD OF INSERT
AS
BEGIN
    -- Check if all inserted Staff_IDs are Teachers
    IF EXISTS (
        SELECT 1
        FROM inserted i
        LEFT JOIN Staff s ON i.Staff_ID = s.Staff_ID
        WHERE s.Staff_Role <> 'Teacher' OR s.Staff_ID IS NULL
    )
    BEGIN
        RAISERROR('Only staff with role Teacher can be assigned to a class.', 16, 1);
        ROLLBACK TRANSACTION;
        RETURN;
    END

    -- Generate Class_ID for each inserted row
    INSERT INTO Class (
        Class_ID, Class_Name, Class_Level, Class_Timetable, Class_Start_Date, Class_End_Date, Class_Capacity, Class_Fee, Staff_ID
    )
    SELECT
        RIGHT(CAST(YEAR(GETDATE()) AS CHAR(4)), 2) + 'C' + RIGHT('0000000' + CAST(NEXT VALUE FOR Seq_ClassID AS VARCHAR(7)), 7),
        Class_Name, Class_Level, Class_Timetable, Class_Start_Date, Class_End_Date, Class_Capacity, Class_Fee, Staff_ID
    FROM inserted;
END
GO


CREATE TRIGGER trg_Enrollment_Insert_And_Valid
ON Enrollment
INSTEAD OF INSERT
AS
BEGIN
    -- Check Enrollment_Date is within class date range
    IF EXISTS (
        SELECT 1
        FROM inserted i
        JOIN Class c ON i.Class_ID = c.Class_ID
        WHERE i.Enrollment_Date < c.Class_Start_Date OR i.Enrollment_Date > c.Class_End_Date
    )
    BEGIN
        RAISERROR('Enrollment_Date must be between Class_Start_Date and Class_End_Date.', 16, 1);
        ROLLBACK TRANSACTION;
        RETURN;
    END

    -- Generate Enrollment_ID for each inserted row
    INSERT INTO Enrollment (
        Enrollment_ID, Stu_ID, Class_ID, Enrollment_Date, Enrollment_Status
    )
    SELECT
        RIGHT(CAST(YEAR(GETDATE()) AS CHAR(4)), 2) + 'E' + RIGHT('0000000' + CAST(NEXT VALUE FOR Seq_EnrollmentID AS VARCHAR(7)), 7),
        Stu_ID, Class_ID, Enrollment_Date, Enrollment_Status
    FROM inserted;
END
GO


-- Merged trigger for Bill: ID formatting and Bill_Date validation
CREATE TRIGGER trg_Bill_Insert_And_Valid
ON Bill
INSTEAD OF INSERT
AS
BEGIN
    -- Check Bill_Date is on or after Enrollment_Date
    IF EXISTS (
        SELECT 1
        FROM inserted i
        JOIN Enrollment e ON i.Enrollment_ID = e.Enrollment_ID
        WHERE i.Bill_Date < e.Enrollment_Date
    )
    BEGIN
        RAISERROR('Bill_Date must be on or after the Enrollment_Date.', 16, 1);
        ROLLBACK TRANSACTION;
        RETURN;
    END

    -- Generate Bill_ID for each inserted row
    INSERT INTO Bill (
        Bill_ID, Enrollment_ID, Bill_Date, Bill_Payment_Status, Bill_Payment_Method, Bill_Note
    )
    SELECT
        RIGHT(CAST(YEAR(GETDATE()) AS CHAR(4)), 2) + 'B' + RIGHT('0000000' + CAST(NEXT VALUE FOR Seq_BillID AS VARCHAR(7)), 7),
        Enrollment_ID, Bill_Date, Bill_Payment_Status, Bill_Payment_Method, Bill_Note
    FROM inserted;
END
GO


-- View for All student in a class
CREATE VIEW vw_StudentsInClass AS
SELECT
    c.Class_ID,
    c.Class_Name,
    s.Stu_ID,
    s.Stu_Name,
    s.Stu_Birthdate,
    s.Stu_Gender,
    s.Stu_Mobile,
    s.Stu_Registration_Date
FROM
    Class c
    INNER JOIN Enrollment e ON c.Class_ID = e.Class_ID
    INNER JOIN Student s ON e.Stu_ID = s.Stu_ID;
GO


-- View for All classes taught by a staff member
CREATE VIEW vw_ClassesByStaff AS
SELECT
    st.Staff_ID,
    st.Staff_Name,
    c.Class_ID,
    c.Class_Name,
    c.Class_Level,
    c.Class_Timetable,
    c.Class_Start_Date,
    c.Class_End_Date,
    c.Class_Capacity,
    c.Class_Fee
FROM
    Staff st
    INNER JOIN Class c ON st.Staff_ID = c.Staff_ID;
GO




-- Sample data for testing
INSERT INTO Staff (Staff_Name, Staff_Birthdate, Staff_Gender, Staff_Mobile, Staff_Role)
VALUES
('Johnny Bravo', '1990-12-30', 'Female', '0112233445', 'Employee');

--Insert sample Students
INSERT INTO Student (Stu_Name, Stu_Birthdate, Stu_Gender, Stu_Mobile)
VALUES
('Eva Green', '2006-07-22', 'Female', '0122222222');

-- Insert sample Class (must use a Teacher's Staff_ID)
-- Get the Staff_ID for Alice Smith (Teacher)
DECLARE @TeacherID nvarchar(10);
SELECT TOP 1 @TeacherID = Staff_ID FROM Staff WHERE Staff_Name = 'Alice Smith';

INSERT INTO Class (Class_Name, Class_Level, Class_Timetable, Class_Start_Date, Class_End_Date, Class_Capacity, Class_Fee, Staff_ID)
VALUES
('Math 101', 'Beginner', '08:00 Monday, 10:00 Wednesday', '2025-07-01', '2025-08-31', 30, 500000, '25S0000002');

-- Insert sample Enrollment (get IDs dynamically)
DECLARE @StudentID1 nvarchar(10), @StudentID2 nvarchar(10), @ClassID nvarchar(10);
SELECT TOP 1 @StudentID1 = Stu_ID FROM Student WHERE Stu_Name = 'David Brown';
SELECT TOP 1 @StudentID2 = Stu_ID FROM Student WHERE Stu_Name = 'Eva Green';
SELECT TOP 1 @ClassID = Class_ID FROM Class WHERE Class_Name = 'Math 101';

INSERT INTO Enrollment (Stu_ID, Class_ID, Enrollment_Date, Enrollment_Status)
VALUES
(@StudentID1, @ClassID, '2025-07-02', 'Active'),
(@StudentID2, @ClassID, '2025-07-03', 'Active');
GO


-- Insert sample Bill (get Enrollment_ID dynamically)
DECLARE @EnrollmentID1 nvarchar(10), @EnrollmentID2 nvarchar(10), @StudentID1 nvarchar(10), @ClassID nvarchar(10);
SELECT TOP 1 @StudentID1 = Stu_ID FROM Student WHERE Stu_Name = 'Eva Green';
Select top 1 @ClassID = Class_ID FROM Class WHERE Class_Name = 'Math 101';
SELECT TOP 1 @EnrollmentID1 = Enrollment_ID FROM Enrollment WHERE Stu_ID = @StudentID1 AND Class_ID = @ClassID;

INSERT INTO Bill (Enrollment_ID, Bill_Date, Bill_Payment_Status, Bill_Payment_Method)
VALUES
(@EnrollmentID1, '2025-07-05', 'Paid', 'Cash');
--(@EnrollmentID2, '2025-07-06', 'Unpaid', 'Card', 'Pending payment');

-- Insert sample LOGIN for staff
INSERT INTO LOGIN (Staff_ID, Login_Password) VALUES ('25S0000003' , 'password123');


