package com.onclass.model.technology.gateways;

import com.onclass.model.technology.Technology;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ITechnologyPersistencePort {
    Mono<Technology> findById(Long id);

    Mono<Technology> saveTechnology(Technology technology);

    Mono<Technology> findByName(String name);

    Flux<Technology> findAllTechnologiesPaged(int page, int size, String sortOrder);
}
