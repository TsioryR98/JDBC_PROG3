-- Creating the 'sex' enum type if it doesn't exist

DO
$$
    BEGIN
        IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'sex') THEN
            CREATE TYPE sex AS ENUM ('MALE', 'FEMALE');
        END IF;
    END
$$;

-- Creating the 'group' table
CREATE TABLE IF NOT EXISTS "group" (
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
                                       FOREIGN KEY (groupId) REFERENCES "group"(groupId)
);

/*academicYear VARCHAR(10) CHECK (academicYear = 'L1' OR academicYear = 'L2' OR academicYear = 'L3') NOT NULL*/
