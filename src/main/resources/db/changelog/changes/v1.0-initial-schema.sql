--liquibase formatted sql
--changeset admartnav:0001-initial-schema

CREATE TABLE users
(
    id          UUID PRIMARY KEY,
    email       VARCHAR(255) NOT NULL,
    password    VARCHAR(255),
    name    VARCHAR(255) NOT NULL,
    surname VARCHAR(255) NOT NULL,
    last_log_in TIMESTAMP WITHOUT TIME ZONE,
    role        VARCHAR(50)  NOT NULL,
    enabled     BOOLEAN      NOT NULL,
    CONSTRAINT uq_users_email UNIQUE (email)
);

CREATE TABLE user_verification
(
    id                UUID PRIMARY KEY,
    user_id           UUID         NOT NULL,
    expiration_date   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    verification_code VARCHAR(255) NOT NULL,
    CONSTRAINT fk_user_verification_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT uq_user_verification_user_id UNIQUE (user_id)
);

CREATE TABLE player
(
    id           UUID PRIMARY KEY,
    personal_id  VARCHAR(255) NOT NULL,
    name         VARCHAR(255) NOT NULL,
    surname      VARCHAR(255) NOT NULL,
    email        VARCHAR(255) NOT NULL,
    phone_number VARCHAR(255) NOT NULL,
    birth_date   DATE         NOT NULL,
    category     VARCHAR(50)  NOT NULL,
    gender       VARCHAR(50)  NOT NULL,
    status       VARCHAR(50)  NOT NULL,
    CONSTRAINT uq_player_personal_id UNIQUE (personal_id),
    CONSTRAINT uq_player_email UNIQUE (email)
);

CREATE TABLE payment
(
    id          UUID PRIMARY KEY,
    code        VARCHAR(10)    NOT NULL,
    amount      DECIMAL(19, 2) NOT NULL,
    name       VARCHAR(255) NOT NULL,
    description VARCHAR(1024),
    start_date DATE,
    end_date   DATE,
    periodicity VARCHAR(50)    NOT NULL,
    status      VARCHAR(50)    NOT NULL,
    CONSTRAINT uq_player_code UNIQUE (code)
);

CREATE TABLE player_payment_assignment
(
    id         UUID PRIMARY KEY,
    player_id  UUID NOT NULL,
    payment_id UUID NOT NULL,
    CONSTRAINT uq_player_payment UNIQUE (player_id, payment_id),
    CONSTRAINT fk_assignment_player FOREIGN KEY (player_id) REFERENCES player (id) ON DELETE CASCADE,
    CONSTRAINT fk_assignment_payment FOREIGN KEY (payment_id) REFERENCES payment (id) ON DELETE CASCADE
);

CREATE TABLE receipt
(
    id                UUID PRIMARY KEY,
    code              VARCHAR(255)   NOT NULL,
    amount            DECIMAL(12, 2) NOT NULL,
    issued_date       DATE           NOT NULL,
    payment_date      DATE,
    expiry_date DATE NOT NULL,
    period_start_date DATE,
    period_end_date   DATE,
    status            VARCHAR(50)    NOT NULL,
    player_id         UUID,
    payment_id        UUID,
    CONSTRAINT fk_receipt_player FOREIGN KEY (player_id) REFERENCES player (id) ON DELETE SET NULL,
    CONSTRAINT fk_receipt_payment FOREIGN KEY (payment_id) REFERENCES payment (id) ON DELETE SET NULL,
    CONSTRAINT uq_receipt_code UNIQUE (code)
);

--rollback DROP TABLE receipt;
--rollback DROP TABLE player_payment_assignment;
--rollback DROP TABLE payment;
--rollback DROP TABLE player;
--rollback DROP TABLE user_verification;
--rollback DROP TABLE users;
