-- Creating the 'sex' enum type if it doesn't exist

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
                                       groupId INT PRIMARY KEY,
                                       groupName VARCHAR NOT NULL,
                                       groupYear TIMESTAMP NOT NULL,
                                       studentNb INT
);

-- Creating the 'student' table with a foreign key reference to 'group'
CREATE TABLE IF NOT EXISTS student (
                                       studentId INT PRIMARY KEY,
                                       studentReference VARCHAR NOT NULL,
                                       lastName VARCHAR NOT NULL,
                                       firstName VARCHAR NOT NULL,
                                       dateOfBirth TIMESTAMP NOT NULL,
                                       sex sex NOT NULL,
                                       groupId INT,
                                       FOREIGN KEY (groupId) REFERENCES groups (groupId)
);


-- Insertion des groupes
INSERT INTO groups (groupId, groupName, groupYear, studentNb) VALUES
(1, 'Groupe A', '2023-01-01 00:00:00', 7),
(2, 'Groupe B', '2023-01-01 00:00:00', 7),
(3, 'Groupe C', '2023-01-01 00:00:00', 6);

-- Insertion des étudiants
INSERT INTO student (studentId, studentReference, lastName, firstName, dateOfBirth, sex, groupId) VALUES
(1, 'STU001', 'Rakoto', 'Jean', '2000-05-12 00:00:00', 'MALE', 1),
(2, 'STU002', 'Randria', 'Marie', '2001-08-23 00:00:00', 'FEMALE', 1),
(3, 'STU003', 'Rajaona', 'Paul', '1999-12-01 00:00:00', 'MALE', 1),
(4, 'STU004', 'Andriam', 'Sophie', '2000-07-15 00:00:00', 'FEMALE', 1),
(5, 'STU005', 'Rakotomalala', 'Luc', '2002-03-20 00:00:00', 'MALE', 1),
(6, 'STU006', 'Raherim', 'Emma', '2001-09-10 00:00:00', 'FEMALE', 1),
(7, 'STU007', 'Ravel', 'Eric', '1998-11-30 00:00:00', 'MALE', 1),

(8, 'STU008', 'Rahari', 'Nina', '2000-06-05 00:00:00', 'FEMALE', 2),
(9, 'STU009', 'Rakot', 'Julien', '2001-02-28 00:00:00', 'MALE', 2),
(10, 'STU010', 'Randr', 'Laura', '2002-10-11 00:00:00', 'FEMALE', 2),
(11, 'STU011', 'Andria', 'Kevin', '1999-09-14 00:00:00', 'MALE', 2),
(12, 'STU012', 'Rajo', 'Isabelle', '2000-01-17 00:00:00', 'FEMALE', 2),
(13, 'STU013', 'Rak', 'Vincent', '2001-05-30 00:00:00', 'MALE', 2),
(14, 'STU014', 'Ravon', 'Léa', '1998-12-22 00:00:00', 'FEMALE', 2),

(15, 'STU015', 'Randriam', 'Antoine', '2000-04-25 00:00:00', 'MALE', 3),
(16, 'STU016', 'Rakotoar', 'Chloé', '2001-07-18 00:00:00', 'FEMALE', 3),
(17, 'STU017', 'Andrian', 'Thomas', '1999-11-09 00:00:00', 'MALE', 3),
(18, 'STU018', 'Rasol', 'Elisa', '2002-02-03 00:00:00', 'FEMALE', 3),
(19, 'STU019', 'Rakotond', 'Mathieu', '2000-09-27 00:00:00', 'MALE', 3),
(20, 'STU020', 'Ravelo', 'Camille', '2001-12-15 00:00:00', 'FEMALE', 3);