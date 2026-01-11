package com.manager.auth.adapter.in.rest.dto.models;

public record LoginResponseDto(String token, long expiresIn) {
}
