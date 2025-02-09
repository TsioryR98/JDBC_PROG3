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
