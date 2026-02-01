--liquibase formatted sql
--changeset admartnav:0003-drop-unique-email-constraint

ALTER TABLE player DROP CONSTRAINT uq_player_email;

--rollback empty;