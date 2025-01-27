CREATE TABLE users (
                       id UUID PRIMARY KEY,
                       username VARCHAR(100) NOT NULL,
                       email VARCHAR(100) NOT NULL,
                       password VARCHAR(255) NOT NULL
);

CREATE TABLE temp_users (
                            id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                            username VARCHAR(100) NOT NULL,
                            email VARCHAR(100) NOT NULL,
                            password VARCHAR(255) NOT NULL,
                            verification_code VARCHAR(6) NOT NULL,
                            created_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE temp_logins (
                             id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                             email VARCHAR(100) NOT NULL,
                             verification_code VARCHAR(6) NOT NULL,
                             created_at TIMESTAMP DEFAULT NOW()
);