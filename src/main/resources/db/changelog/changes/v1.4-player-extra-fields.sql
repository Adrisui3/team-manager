--liquibase formatted sql
--changeset admartnav:0004-player-extra-fields


ALTER TABLE player
    ADD COLUMN secondary_email VARCHAR(255);

ALTER TABLE player
    ADD COLUMN secondary_phone_number VARCHAR(255);

--rollback ALTER TABLE player DROP COLUMN secondary_email;
--rollback ALTER TABLE player DROP COLUMN secondary_phone_number;