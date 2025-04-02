package com.onclass.jpa.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DBErrorMessage {
    TECHNOLOGY_ALREADY_EXISTS("Tecnología ya existe", 400),
    UNEXPECTED_EXCEPTION("Sucedió un error inesperado", 500),
    NAME_CHARACTERS_EXCEED("The description cannot exceed 50 characters", 404),
    TECHNOLOGY_NOT_FOUND("Tecnología no existe", 404);
    private final String message;
    private final int httpStatusCode;
}
