package com.manager.auth.adapter.dto;

public record ChangeUserPasswordDto(String email, String oldPassword, String newPassword) {
}
