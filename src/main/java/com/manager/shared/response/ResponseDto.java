package com.manager.shared.response;

import java.time.LocalDateTime;

public record ResponseDto<T>(LocalDateTime timestamp, T data) {

    public ResponseDto(T data) {
        this(LocalDateTime.now(), data);
    }

}
