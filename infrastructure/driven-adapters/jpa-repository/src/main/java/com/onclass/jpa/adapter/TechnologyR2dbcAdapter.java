package com.onclass.jpa.adapter;

import com.onclass.jpa.adapter.port.ITechnologyRepository;
import com.onclass.jpa.config.DBErrorMessage;
import com.onclass.jpa.config.DBException;
import com.onclass.jpa.entity.TechnologyEntity;
import com.onclass.jpa.helper.ITechnologyEntityMapper;
import com.onclass.model.technology.Technology;
import com.onclass.model.technology.gateways.ITechnologyPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class TechnologyR2dbcAdapter implements ITechnologyPersistencePort {

    private final ITechnologyRepository technologyRepository;
    private final ITechnologyEntityMapper technologyEntityMapper;

    @Override
    public Mono<Technology> findById(Long id) {
        return technologyRepository.findById(id)
                .flatMap(technologyEntity -> Mono.just(technologyEntityMapper.toModel(technologyEntity)))
                .switchIfEmpty(Mono.error(new DBException(DBErrorMessage.TECHNOLOGY_NOT_FOUND)));
    }



    @Override
    public Mono<Technology> saveTechnology(Technology technology) {
        TechnologyEntity entity = technologyEntityMapper.toEntity(technology);
        return technologyRepository.save(entity)
                .map(technologyEntityMapper::toModel)
                .onErrorResume(DuplicateKeyException.class, ex -> Mono.error(new DBException(DBErrorMessage.TECHNOLOGY_ALREADY_EXISTS)));
    }

    @Override
    public Mono<Technology> findByName(String name) {
        return technologyRepository.findByName(name)
                .flatMap(technologyEntity -> Mono.just(technologyEntityMapper.toModel(technologyEntity)))
                .switchIfEmpty(Mono.error(new DBException(DBErrorMessage.TECHNOLOGY_NOT_FOUND)));
    }
    @Override
    public Flux<Technology> findAllTechnologiesPaged(int page, int size, String sortOrder) {
        Sort sort = sortOrder.equalsIgnoreCase("desc") ? Sort.by("name").descending() : Sort.by("name").ascending();
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        return technologyRepository.findAllBy(pageRequest)
                .map(technologyEntityMapper::toModel);
    }


}
