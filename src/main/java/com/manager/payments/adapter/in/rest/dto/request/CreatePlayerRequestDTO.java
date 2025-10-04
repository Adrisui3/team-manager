package com.manager.payments.adapter.in.rest.dto.request;

import com.manager.payments.model.players.Category;

import java.time.LocalDate;

public record CreatePlayerRequestDTO(String personalId, String name, String surname, String email, LocalDate birthDate,
                                     Category category) {
}
