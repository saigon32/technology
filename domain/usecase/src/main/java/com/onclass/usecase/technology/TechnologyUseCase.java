package com.onclass.usecase.technology;

import com.onclass.model.technology.Technology;
import com.onclass.model.technology.gateways.ITechnologyPersistencePort;
import com.onclass.model.technology.gateways.ITechnologyServicePort;
import com.onclass.usecase.technology.validations.TechnologyCreateValidations;
import com.onclass.usecase.technology.validations.TechnologyPageValidations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class TechnologyUseCase implements ITechnologyServicePort {
    private final ITechnologyPersistencePort persistencePort;
    private final TechnologyCreateValidations technologyCreateValidations;
    private final TechnologyPageValidations technologyPageValidations;

    public TechnologyUseCase(ITechnologyPersistencePort persistencePort,
                             TechnologyCreateValidations technologyCreateValidations,
                             TechnologyPageValidations technologyPageValidations) {
        this.persistencePort = persistencePort;
        this.technologyCreateValidations = technologyCreateValidations;
        this.technologyPageValidations = technologyPageValidations;
    }

    @Override
    public Mono<Technology> findById(String id) {
        return persistencePort.findById(Long.parseLong(id));
    }

    public Flux<Technology> createTechnologies(Flux<Technology> technologies) {
        return technologies
                .flatMap(technologyCreateValidations::validateTechnology)
                .flatMap(persistencePort::saveTechnology);
    }

    public Flux<Technology> listTechnologiesPaged(int page, int size, String sortOrder) {
        return technologyPageValidations.validatePageParameters(page, size, sortOrder)
                .thenMany(persistencePort.findAllTechnologiesPaged(page, size, sortOrder));
    }


}
