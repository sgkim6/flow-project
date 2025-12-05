package com.example.flow.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    INVALID_EXTENSION(HttpStatus.BAD_REQUEST, "E001", "허용되지 않는 확장자입니다."), // 정규식 패턴 다를 경우 날리는거
    PINNED_EXTENSION_ALREADY_EXISTS(HttpStatus.CONFLICT, "E002", "이미 고정 확장자 리스트에 존재하는 확장자입니다."),
    EXTENSION_ALREADY_EXISTS(HttpStatus.CONFLICT, "E003", "이미 추가된 확장자입니다."),
    EXTENSION_NAME_TOO_LONG(HttpStatus.BAD_REQUEST, "E004", "확장자는 20자를 초과할 수 없습니다."),
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
