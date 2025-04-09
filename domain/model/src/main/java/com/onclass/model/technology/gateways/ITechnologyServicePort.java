package com.onclass.model.technology.gateways;

import com.onclass.model.technology.Technology;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ITechnologyServicePort {
    Mono<Technology> findById(String id);

    Flux<Technology> createTechnologies(Flux<Technology> technologies);

    Flux<Technology> listTechnologiesPaged(int page, int size, String sortOrder);
}
