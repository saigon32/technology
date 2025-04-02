package com.onclass.model.technology.gateways;

import com.onclass.model.technology.Technology;
import reactor.core.publisher.Flux;

public interface ITechnologyServicePort {
    Flux<Technology> createTechnologies(Flux<Technology> technologies);
    Flux<Technology> listTechnologiesPaged(int page, int size, String sortOrder);
}
