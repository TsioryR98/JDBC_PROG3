-- Creating the 'sex' enum type if it doesn't exist

create database prog3_jdbc;
DO
$$
    BEGIN
        IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'sex') THEN
            CREATE TYPE sex AS ENUM ('MALE', 'FEMALE');
        END IF;
    END
$$;

DO
$$
    BEGIN
        IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'orders') THEN
            CREATE TYPE orders AS ENUM ('ASC', 'DESC');
        END IF;
    END
$$;

-- Creating the 'group' table
CREATE TABLE IF NOT EXISTS groups (
group_id INT PRIMARY KEY,
group_name VARCHAR NOT NULL,
group_year DATE NOT NULL,
student_nb INT
);

-- Creating the 'student' table with a foreign key reference to 'group'
CREATE TABLE IF NOT EXISTS student (
student_id INT PRIMARY KEY,
student_reference VARCHAR NOT NULL,
last_name VARCHAR NOT NULL,
first_name VARCHAR NOT NULL,
date_of_birth DATE NOT NULL,
sex sex NOT NULL,
group_id INT,
FOREIGN KEY (group_id) REFERENCES groups (group_id)
);


-- Insert group
INSERT INTO groups (group_id, group_name, group_year, student_nb) VALUES
(1, 'Group A', '2023-01-01', 7),
(2, 'Group B', '2023-01-01', 7),
(3, 'Group C', '2023-01-01', 6);

-- Insert des student
INSERT INTO student (student_id, student_reference, last_name, first_name, date_of_birth, sex, group_id) VALUES
(1, 'STU001', 'Rakoto', 'Jean', '2000-05-12', 'MALE', 1),
(2, 'STU002', 'Randria', 'Marie', '2001-08-23', 'FEMALE', 1),
(3, 'STU003', 'Rajaona', 'Paul', '1999-12-01', 'MALE', 1),
(4, 'STU004', 'Andriam', 'Sophie', '2000-07-15', 'FEMALE', 1),
(5, 'STU005', 'Rakotomalala', 'Luc', '2002-03-20', 'MALE', 1),
(6, 'STU006', 'Raherim', 'Emma', '2001-09-10', 'FEMALE', 1),
(7, 'STU007', 'Ravel', 'Eric', '1998-11-30', 'MALE', 1),

(8, 'STU008', 'Rahari', 'Nina', '2000-06-05', 'FEMALE', 2),
(9, 'STU009', 'Rakot', 'Julien', '2001-02-28', 'MALE', 2),
(10, 'STU010', 'Randr', 'Laura', '2002-10-11', 'FEMALE', 2),
(11, 'STU011', 'Andria', 'Kevin', '1999-09-14', 'MALE', 2),
(12, 'STU012', 'Rajo', 'Isabelle', '2000-01-17', 'FEMALE', 2),
(13, 'STU013', 'Rak', 'Vincent', '2001-05-30', 'MALE', 2),
(14, 'STU014', 'Ravon', 'Léa', '1998-12-22', 'FEMALE', 2),

(15, 'STU015', 'Randriam', 'Antoine', '2000-04-25', 'MALE', 3),
(16, 'STU016', 'Rakotoar', 'Chloé', '2001-07-18', 'FEMALE', 3),
(17, 'STU017', 'Andrian', 'Thomas', '1999-11-09', 'MALE', 3),
(18, 'STU018', 'Rasol', 'Elisa', '2002-02-03', 'FEMALE', 3),
(19, 'STU019', 'Rakotond', 'Mathieu', '2000-09-27', 'MALE', 3),
(20, 'STU020', 'Ravelo', 'Camille', '2001-12-15', 'FEMALE', 3);

SELECT * FROM student s ORDER BY s.student_id ASC LIMIT 1 OFFSET 10;
SELECT * FROM student ORDER BY last_name ASC, date_of_birth ASC;
SELECT * FROM student WHERE 1=1 AND last_name ILIKE '%ra%' and date_of_birth between '2000-01-01' and '2001-12-31';
