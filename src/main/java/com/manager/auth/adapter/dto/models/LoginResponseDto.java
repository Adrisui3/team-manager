package com.manager.auth.adapter.dto.models;

public record LoginResponseDto(String token, long expiresIn) {
}
