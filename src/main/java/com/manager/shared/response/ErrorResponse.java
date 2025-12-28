package com.manager.shared.response;

import com.manager.shared.ErrorCode;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ErrorResponse(LocalDateTime timestamp, ErrorCode code, String message) {
    public ErrorResponse(ErrorCode code, String message) {
        this(LocalDateTime.now(), code, message);
    }
}
