package com.manager.payments.adapter.in.rest.dto;

import com.manager.payments.model.users.Category;

public record CreateUserRequestDTO(String name, String surname, String email, String birthDate, Category category) {
}
