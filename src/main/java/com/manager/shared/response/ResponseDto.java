package com.manager.shared.response;

import java.time.LocalDateTime;

public record ResponseDto<T>(LocalDateTime timeStamp, int httpStatus, T data) {

}
