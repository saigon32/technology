package com.onclass.usecase.technology.validations;

import com.onclass.model.exception.BusinessErrorMessage;
import com.onclass.model.exception.BusinessException;
import com.onclass.model.technology.Technology;
import reactor.core.publisher.Mono;
import static com.onclass.usecase.technology.Constants.MAX_DESCRIPTION_LENGTH;
import static com.onclass.usecase.technology.Constants.MAX_NAME_LENGTH;

public class TechnologyCreateValidations {

    public Mono<Technology> validateTechnology(Technology technology) {
        return validateName(technology)
                .then(validateDescription(technology))
                .then(Mono.just(technology));
    }

    private Mono<Void> validateName(Technology technology) {
        if (technology.getName().length() > MAX_NAME_LENGTH) {
            return Mono.error(new BusinessException(BusinessErrorMessage.NAME_CHARACTERS_EXCEED));
        }
        if (technology.getName().isEmpty()) {
            return Mono.error(new BusinessException(BusinessErrorMessage.MANDATORY_NAME));
        }
        return Mono.empty();
    }

    private Mono<Void> validateDescription(Technology technology) {
        if (technology.getDescription().isEmpty()) {
            return Mono.error(new BusinessException(BusinessErrorMessage.MANDATORY_DESCRIPTION));
        }
        if (technology.getDescription().length() > MAX_DESCRIPTION_LENGTH) {
            return Mono.error(new BusinessException(BusinessErrorMessage.DESCRIPTION_CHARACTERS_EXCEED));
        }
        return Mono.empty();
    }


}
