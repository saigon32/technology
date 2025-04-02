package com.onclass.usecase.technology;

import com.onclass.model.exception.BusinessErrorMessage;
import com.onclass.model.exception.BusinessException;
import com.onclass.model.technology.Technology;
import com.onclass.model.technology.gateways.ITechnologyPersistencePort;
import com.onclass.usecase.technology.validations.TechnologyCreateValidations;
import com.onclass.usecase.technology.validations.TechnologyPageValidations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static com.onclass.model.exception.BusinessErrorMessage.INVALID_PAGE_SIZE;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TechnologyUseCaseTest {
    @Mock
    private ITechnologyPersistencePort persistencePort;
    @Mock
    private TechnologyCreateValidations technologyCreateValidations;
    @Mock
    private TechnologyPageValidations technologyPageValidations;

    private TechnologyUseCase technologyUseCase;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        technologyUseCase = new TechnologyUseCase(persistencePort, technologyCreateValidations, technologyPageValidations);

        Technology technology = new Technology();
        technology.setId(1);
        technology.setName("Java");
        technology.setDescription("Lenguaje de programación");

        lenient().when(technologyCreateValidations.validateTechnology(any(Technology.class)))
                .thenReturn(Mono.just(technology));
        lenient().when(technologyPageValidations.validatePageParameters(anyInt(), anyInt(), anyString()))
                .thenReturn(Mono.empty());
        lenient().when(persistencePort.saveTechnology(any(Technology.class)))
                .thenReturn(Mono.just(technology));
    }

    @Test
    void testCreateTechnologies() {
        Flux<Technology> inputFlux = Flux.just(new Technology());

        Flux<Technology> resultFlux = technologyUseCase.createTechnologies(inputFlux);

        StepVerifier.create(resultFlux)
                .expectNextMatches(technology ->
                        technology.getId() == 1 &&
                                "Java".equals(technology.getName()) &&
                                "Lenguaje de programación".equals(technology.getDescription()))
                .expectComplete()
                .verify();
    }

    @Test
    void testCreateTechnologies_whenTechnologyIsNull() {
        Flux<Technology> inputFlux = Flux.empty();

        Flux<Technology> resultFlux = technologyUseCase.createTechnologies(inputFlux);

        StepVerifier.create(resultFlux)
                .expectComplete()
                .verify();
    }

    @Test
    void testCreateTechnologies_whenDescriptionIsTooLong() {
        BusinessException expectedException = new BusinessException(BusinessErrorMessage.DESCRIPTION_CHARACTERS_EXCEED);
        when(technologyCreateValidations.validateTechnology(any(Technology.class)))
                .thenReturn(Mono.error(expectedException));

        Technology technology = new Technology();
        technology.setName("Java");
        technology.setDescription("A".repeat(91));

        Flux<Technology> inputFlux = Flux.just(technology);
        Flux<Technology> resultFlux = technologyUseCase.createTechnologies(inputFlux);

        StepVerifier.create(resultFlux)
                .expectErrorMatches(throwable -> throwable instanceof BusinessException &&
                        throwable.getMessage().equals(expectedException.getMessage())) // Compara el mensaje de error
                .verify();
    }

    @Test
    void testCreateTechnologies_whenNameIsTooLong() {
        BusinessException expectedException = new BusinessException(BusinessErrorMessage.NAME_CHARACTERS_EXCEED);
        when(technologyCreateValidations.validateTechnology(any(Technology.class)))
                .thenReturn(Mono.error(expectedException));

        Technology technology = new Technology();
        technology.setName("Java".repeat(51));
        technology.setDescription("A");

        Flux<Technology> inputFlux = Flux.just(technology);
        Flux<Technology> resultFlux = technologyUseCase.createTechnologies(inputFlux);

        StepVerifier.create(resultFlux)
                .expectErrorMatches(throwable -> throwable instanceof BusinessException &&
                        throwable.getMessage().equals(expectedException.getMessage()))
                .verify();
    }


    @Test
    void testListTechnologiesPaged_withValidParameters() {
        Technology technology = new Technology();
        technology.setId(1);
        technology.setName("Java");
        technology.setDescription("Lenguaje de programación");

        when(technologyPageValidations.validatePageParameters(anyInt(), anyInt(), anyString()))
                .thenReturn(Mono.empty());

        when(persistencePort.findAllTechnologiesPaged(0, 10, "asc"))
                .thenReturn(Flux.just(technology));

        Flux<Technology> resultFlux = technologyUseCase.listTechnologiesPaged(0, 10, "asc");

        StepVerifier.create(resultFlux)
                .expectNextMatches(t ->
                        t.getId() == 1 &&
                                "Java".equals(t.getName()) &&
                                "Lenguaje de programación".equals(t.getDescription()))
                .expectComplete()
                .verify();
    }

    @Test
    void testListTechnologiesPaged_withInvalidPageNumber() {
        BusinessException expectedException = new BusinessException(BusinessErrorMessage.INVALID_PAGE_NUMBER);
        when(technologyPageValidations.validatePageParameters(anyInt(), anyInt(), anyString()))
                .thenReturn(Mono.error(expectedException));

        Flux<Technology> resultFlux = technologyUseCase.listTechnologiesPaged(-1, 10, "asc");

        StepVerifier.create(resultFlux)
                .expectErrorMatches(throwable -> throwable instanceof BusinessException &&
                        throwable.getMessage().equals(expectedException.getMessage()))
                .verify();
    }

    @Test
    void testListTechnologiesPaged_withInvalidSizeNumber() {
        BusinessException expectedException = new BusinessException(BusinessErrorMessage.INVALID_PAGE_SIZE);
        when(technologyPageValidations.validatePageParameters(anyInt(), anyInt(), anyString()))
                .thenReturn(Mono.error(expectedException));

        Flux<Technology> resultFlux = technologyUseCase.listTechnologiesPaged(0, 0, "asc");

        StepVerifier.create(resultFlux)
                .expectErrorMatches(throwable -> throwable instanceof BusinessException &&
                        throwable.getMessage().equals(expectedException.getMessage()))
                .verify();
    }

    @Test
    void testListTechnologiesPaged_withInvalidSortOrder() {
        BusinessException expectedException = new BusinessException(BusinessErrorMessage.INVALID_SORT_ORDER);
        when(technologyPageValidations.validatePageParameters(anyInt(), anyInt(), anyString()))
                .thenReturn(Mono.error(expectedException));

        Flux<Technology> resultFlux = technologyUseCase.listTechnologiesPaged(0, 5, "p");

        StepVerifier.create(resultFlux)
                .expectErrorMatches(throwable -> throwable instanceof BusinessException &&
                        throwable.getMessage().equals(expectedException.getMessage()))
                .verify();
    }

}