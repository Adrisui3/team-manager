--liquibase formatted sql
--changeset admartnav:0002-created-updated-fields

ALTER TABLE player
    ADD COLUMN created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE player
    ADD COLUMN updated_at TIMESTAMP;

--rollback ALTER TABLE player DROP COLUMN created_at;
--rollback ALTER TABLE player DROP COLUMN updated_at;

ALTER TABLE users
    ADD COLUMN created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE users
    ADD COLUMN updated_at TIMESTAMP;

--rollback ALTER TABLE users DROP COLUMN created_at;
--rollback ALTER TABLE users DROP COLUMN updated_at;

ALTER TABLE payment
    ADD COLUMN created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE payment
    ADD COLUMN updated_at TIMESTAMP;

--rollback ALTER TABLE payment DROP COLUMN created_at;
--rollback ALTER TABLE payment DROP COLUMN updated_at;

ALTER TABLE receipt
    ADD COLUMN created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE receipt
    ADD COLUMN updated_at TIMESTAMP;

--rollback ALTER TABLE receipt DROP COLUMN created_at;
--rollback ALTER TABLE receipt DROP COLUMN updated_at;
