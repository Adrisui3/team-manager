package com.manager.auth.adapter.dto;

public record SetUserPasswordDto(String email, String verificationCode, String password) {
}
