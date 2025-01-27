DROP TABLE IF EXISTS two_factor_auth;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       username VARCHAR(100) NOT NULL UNIQUE,
                       email VARCHAR(100) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE two_factor_auth (
                                 id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                 email VARCHAR(100) NOT NULL,
                                 username VARCHAR(100),
                                 password VARCHAR(255),
                                 code VARCHAR(6) NOT NULL,
                                 expiry_time TIMESTAMP NOT NULL,
                                 used BOOLEAN DEFAULT FALSE,
                                 registration BOOLEAN NOT NULL,
                                 created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_tfa_email_code ON two_factor_auth(email, code);
CREATE INDEX idx_tfa_email_registration ON two_factor_auth(email, registration) WHERE used = FALSE;