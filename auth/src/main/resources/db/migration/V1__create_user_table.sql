CREATE TABLE users (
                       id UUID PRIMARY KEY,
                       username VARCHAR(100) NOT NULL UNIQUE,
                       email VARCHAR(100) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL
);

CREATE TABLE two_factor_auth (
                                 id UUID PRIMARY KEY,
                                 email VARCHAR(100) NOT NULL,
                                 username VARCHAR(100),
                                 password VARCHAR(255),
                                 code VARCHAR(6) NOT NULL,
                                 expiry_time TIMESTAMP NOT NULL,
                                 used BOOLEAN DEFAULT FALSE,
                                 registration BOOLEAN DEFAULT FALSE
);

CREATE INDEX idx_tfa_email_code ON two_factor_auth(email, code);
CREATE INDEX idx_tfa_registration ON two_factor_auth(email) WHERE registration = true;