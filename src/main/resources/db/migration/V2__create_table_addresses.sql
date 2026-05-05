CREATE TABLE IF NOT EXISTS addresses (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id             BIGINT          NOT NULL,
    zip_code            VARCHAR(20)     NOT NULL,
    country_code        VARCHAR(2)      NOT NULL,
    state_code          VARCHAR(10)     NOT NULL,
    city                VARCHAR(100)    NOT NULL,
    street              VARCHAR(255)    NOT NULL,
    number              VARCHAR(20)     NULL,
    complement          VARCHAR(255)    NULL,
    created_at          DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified_at    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_addresses_user_id FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);
