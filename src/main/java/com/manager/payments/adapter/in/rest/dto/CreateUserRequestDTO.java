package com.manager.payments.adapter.in.rest.dto;

import com.manager.payments.model.users.Category;

import java.time.LocalDate;

public record CreateUserRequestDTO(String personalId, String name, String surname, String email, LocalDate birthDate,
                                   Category category) {
}
