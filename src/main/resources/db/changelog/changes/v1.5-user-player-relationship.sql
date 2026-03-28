--liquibase formatted sql
--changeset admartnav:0005-user-player-relationship

ALTER TABLE player
    ADD COLUMN user_id UUID,
    ADD CONSTRAINT fk_player_user FOREIGN KEY (user_id) REFERENCES users (id) ON
DELETE
SET NULL;

--rollback ALTER TABLE player DROP CONSTRAINT fk_player_user;
--rollback ALTER TABLE player DROP COLUMN user_id;
