package com.onclass.jpa.config;

import lombok.Getter;

@Getter
public class DBException extends RuntimeException {
    private final DBErrorMessage errorMessage;
    public DBException(DBErrorMessage dbErrorMessage) {
        super(dbErrorMessage.getMessage().toString());
        this.errorMessage = dbErrorMessage;
    }
}
