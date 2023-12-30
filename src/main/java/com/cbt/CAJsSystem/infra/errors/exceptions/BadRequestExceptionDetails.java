package com.cbt.CAJsSystem.infra.errors.exceptions;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BadRequestExceptionDetails {
    private int code;
    private String message;
    private String detailedMessage;
}
