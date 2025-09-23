package com.manager.auth.adapter.dto;

public record LoginResponseDto(String token, long expiresIn) {
}
