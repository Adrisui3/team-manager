package com.manager.payments.adapter.in.rest.dto;

import com.manager.payments.model.players.Category;
import com.manager.payments.model.players.PlayerStatus;

import java.time.LocalDate;
import java.util.UUID;

public record PlayerMInInfoDto(UUID id, String personalId, String name, String surname, String email,
                               LocalDate birthDate,
                               Category category, PlayerStatus status) {
}
