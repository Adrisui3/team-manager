--liquibase formatted sql
--changeset admartnav:0003-initial-schema

ALTER TABLE player DROP CONSTRAINT uq_player_email;

--rollback empty;