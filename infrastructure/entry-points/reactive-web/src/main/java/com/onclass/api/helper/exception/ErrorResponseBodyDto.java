package com.onclass.api.helper.exception;

import lombok.Builder;

@Builder
public record ErrorResponseBodyDto (String message){
}
