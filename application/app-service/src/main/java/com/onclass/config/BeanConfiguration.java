package com.onclass.config;

import com.onclass.jpa.adapter.TechnologyR2dbcAdapter;
import com.onclass.jpa.adapter.port.ITechnologyRepository;
import com.onclass.jpa.helper.ITechnologyEntityMapper;
import com.onclass.model.technology.gateways.ITechnologyPersistencePort;
import com.onclass.model.technology.gateways.ITechnologyServicePort;
import com.onclass.usecase.technology.TechnologyUseCase;
import com.onclass.usecase.technology.validations.TechnologyCreateValidations;
import com.onclass.usecase.technology.validations.TechnologyPageValidations;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {
    private final ITechnologyRepository technologyRepository;
    private final ITechnologyEntityMapper technologyEntityMapper;

    @Bean
    public ITechnologyPersistencePort technologyPersistencePort() {
        return new TechnologyR2dbcAdapter(technologyRepository, technologyEntityMapper);
    }

    @Bean
    public TechnologyCreateValidations validations() {
        return new TechnologyCreateValidations();
    }

    @Bean
    TechnologyPageValidations technologyPageValidations(){return new TechnologyPageValidations();}

    @Bean
    public ITechnologyServicePort technologyServicePort(ITechnologyPersistencePort technologyPersistencePort, TechnologyCreateValidations technologyCreateValidations, TechnologyPageValidations technologyPageValidations) {
        return new TechnologyUseCase(technologyPersistencePort, technologyCreateValidations, technologyPageValidations);
    }


}
