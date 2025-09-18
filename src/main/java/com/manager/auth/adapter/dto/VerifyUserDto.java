package com.manager.auth.adapter.dto;

public record VerifyUserDto(String email, String verificationCode) {
}
