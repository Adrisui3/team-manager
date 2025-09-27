package com.manager.shared.response;

import java.time.LocalDateTime;

public record ResponseDto<T>(LocalDateTime timestamp, int httpStatus, T data) {

    public ResponseDto(int httpStatus, T data) {
        this(LocalDateTime.now(), httpStatus, data);
    }

}
