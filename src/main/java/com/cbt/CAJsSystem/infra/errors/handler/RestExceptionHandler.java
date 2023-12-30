package com.cbt.CAJsSystem.infra.errors.handler;

import com.cbt.CAJsSystem.infra.errors.exceptions.BadRequestException;
import com.cbt.CAJsSystem.infra.errors.exceptions.BadRequestExceptionDetails;
import com.cbt.CAJsSystem.infra.errors.exceptions.ForbiddenException;
import com.cbt.CAJsSystem.infra.errors.exceptions.ForbiddenExceptionDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<BadRequestExceptionDetails> handlerBadRequestException(BadRequestException badRequestException){
        String detail = badRequestException.getMessage().split("\ndetail:")[0];
        String msg = badRequestException.getMessage().split("\ndetail:")[1];
        return new ResponseEntity<>(BadRequestExceptionDetails.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .detailedMessage(detail)
                .message(msg)
                .build(),HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ForbiddenExceptionDetails>handleDorbiddenException(ForbiddenException forbiddenException){
        return new ResponseEntity<>(ForbiddenExceptionDetails.builder()
                .code(HttpStatus.FORBIDDEN.value())
                .detailedMessage("Acesso Negado")
                .message(forbiddenException.getMessage())
                .build(),HttpStatus.FORBIDDEN);
    }
}
