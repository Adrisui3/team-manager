--liquibase formatted sql
--changeset admartnav:0004-email-outbox

CREATE TABLE email_outbox
(
    id         UUID PRIMARY KEY,
    to_email   VARCHAR(255) NOT NULL,
    subject    VARCHAR(255) NOT NULL,
    body       TEXT         NOT NULL,
    created_at TIMESTAMP    NOT NULL,
    sent_at    TIMESTAMP,
    status     VARCHAR(50)  NOT NULL
);

CREATE INDEX idx_email_outbox_status_created_at ON email_outbox (status, created_at);

--rollback DROP TABLE email_outbox;