CREATE TABLE IF NOT EXISTS users (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    type                ENUM('RESTAURANT_OWNER', 'CUSTOMER')    NOT NULL,
    name                VARCHAR(150)                            NOT NULL,
    email               VARCHAR(255)                            NOT NULL UNIQUE,
    login               VARCHAR(100)                            NOT NULL UNIQUE,
    password            VARCHAR(255)                            NOT NULL,
    last_login_at       DATETIME                                NULL,
    created_at          DATETIME                                NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified_at    DATETIME                                NOT NULL DEFAULT CURRENT_TIMESTAMP
);
