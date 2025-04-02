package com.onclass.jpa.adapter;

import com.onclass.jpa.adapter.port.ITechnologyRepository;
import com.onclass.jpa.config.DBErrorMessage;
import com.onclass.jpa.config.DBException;
import com.onclass.jpa.entity.TechnologyEntity;
import com.onclass.jpa.helper.ITechnologyEntityMapper;
import com.onclass.model.technology.Technology;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TechnologyR2dbcAdapterTest {

    @Mock
    private ITechnologyRepository technologyRepository;
    @Mock
    private ITechnologyEntityMapper technologyEntityMapper;
    @InjectMocks
    private TechnologyR2dbcAdapter adapter;

    private Technology technology;
    private TechnologyEntity technologyEntity;

    @BeforeEach
    void setUp() {
        technology = new Technology();
        technology.setId(1);
        technology.setName("Java");
        technology.setDescription("Lenguaje de programación");

        technologyEntity = new TechnologyEntity();
        technologyEntity.setId(1);
        technologyEntity.setName("Java");
        technologyEntity.setDescription("Lenguaje de programación");
    }

    @Test
    void testSaveTechnology_success() {
        when(technologyEntityMapper.toEntity(any(Technology.class))).thenReturn(technologyEntity);
        when(technologyRepository.save(any(TechnologyEntity.class))).thenReturn(Mono.just(technologyEntity));
        when(technologyEntityMapper.toModel(any(TechnologyEntity.class))).thenReturn(technology);

        Mono<Technology> result = adapter.saveTechnology(technology);

        StepVerifier.create(result)
                .expectNextMatches(savedTech -> savedTech.getId() == 1 && savedTech.getName().equals("Java"))
                .verifyComplete();
    }

    @Test
    void testSaveTechnology_duplicateKeyError() {
        when(technologyEntityMapper.toEntity(any(Technology.class))).thenReturn(technologyEntity);
        when(technologyRepository.save(any(TechnologyEntity.class)))
                .thenReturn(Mono.error(new DuplicateKeyException("Duplicate key")));

        Mono<Technology> result = adapter.saveTechnology(technology);

        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof DBException &&
                                ((DBException) throwable).getErrorMessage() == DBErrorMessage.TECHNOLOGY_ALREADY_EXISTS)
                .verify();
    }

    @Test
    void testFindByName_success() {
        when(technologyRepository.findByName(any(String.class))).thenReturn(Mono.just(technologyEntity));
        when(technologyEntityMapper.toModel(any(TechnologyEntity.class))).thenReturn(technology);

        Mono<Technology> result = adapter.findByName("Java");

        StepVerifier.create(result)
                .expectNextMatches(foundTech -> foundTech.getName().equals("Java"))
                .verifyComplete();
    }

    @Test
    void testFindByName_notFound() {
        when(technologyRepository.findByName(any(String.class)))
                .thenReturn(Mono.empty());

        Mono<Technology> result = adapter.findByName("NonExistingTech");

        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof DBException &&
                                ((DBException) throwable).getErrorMessage() == DBErrorMessage.TECHNOLOGY_NOT_FOUND)
                .verify();
    }

    @Test
    void testFindAllTechnologiesPaged_withValidParameters() {

        Technology technology = new Technology();
        technology.setId(1);
        technology.setName("Java");
        technology.setDescription("Lenguaje de programación");

        TechnologyEntity technologyEntity = new TechnologyEntity();
        technologyEntity.setId(1);
        technologyEntity.setName("Java");
        technologyEntity.setDescription("Lenguaje de programación");

        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("name").ascending());
        when(technologyRepository.findAllBy(pageRequest))
                .thenReturn(Flux.just(technologyEntity));

        when(technologyEntityMapper.toModel(any(TechnologyEntity.class)))
                .thenReturn(technology);

        Flux<Technology> resultFlux = adapter.findAllTechnologiesPaged(0, 10, "asc");

        StepVerifier.create(resultFlux)
                .expectNextMatches(t ->
                        t.getId() == 1 &&
                                "Java".equals(t.getName()) &&
                                "Lenguaje de programación".equals(t.getDescription()))
                .expectComplete()
                .verify();
    }

    @Test
    void testFindAllTechnologiesPaged_withEmptyResult() {
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("name").ascending());
        when(technologyRepository.findAllBy(pageRequest))
                .thenReturn(Flux.empty());

        Flux<Technology> resultFlux = adapter.findAllTechnologiesPaged(0, 10, "asc");

        StepVerifier.create(resultFlux)
                .expectComplete()
                .verify();
    }

}