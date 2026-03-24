--liquibase formatted sql
--changeset admartnav:0005-user-player-relationship

ALTER TABLE users
    ADD COLUMN player_id UUID UNIQUE,
    ADD CONSTRAINT fk_users_player FOREIGN KEY (player_id) REFERENCES player (id) ON
DELETE
SET NULL;

--rollback ALTER TABLE users DROP CONSTRAINT fk_users_player;
--rollback ALTER TABLE users DROP COLUMN player_id;
