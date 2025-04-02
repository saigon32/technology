package com.onclass.api.helper;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

public interface ITechnologyHandler {

    Mono<ServerResponse> createTechnologies(ServerRequest request);

    Mono<ServerResponse> listTechnologies(ServerRequest request);
}
