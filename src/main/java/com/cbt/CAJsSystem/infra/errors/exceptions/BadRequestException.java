package com.cbt.CAJsSystem.infra.errors.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message, String detail){
        super(message+"\ndetail:"+detail);
    }
}
