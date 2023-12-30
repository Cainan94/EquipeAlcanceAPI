package com.cbt.EquipeAlcance.infra.errors.exceptions;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ForbiddenExceptionDetails {
    private int code;
    private String message;
    private String detailedMessage;
}
