package com.onclass.usecase.technology.validations;

import com.onclass.model.exception.BusinessException;
import com.onclass.model.exception.BusinessErrorMessage;
import reactor.core.publisher.Mono;

public class TechnologyPageValidations {

    public Mono<Void> validatePageParameters(int page, int size, String sortOrder) {
        return validatePage(page)
                .then(validateSize(size))
                .then(validateSortOrder(sortOrder));
    }

    private Mono<Void> validatePage(int page) {
        if (page < 0) {
            return Mono.error(new BusinessException(BusinessErrorMessage.INVALID_PAGE_NUMBER));
        }
        return Mono.empty();
    }

    private Mono<Void> validateSize(int size) {
        if (size <= 0) {
            return Mono.error(new BusinessException(BusinessErrorMessage.INVALID_PAGE_SIZE));
        }
        return Mono.empty();
    }

    private Mono<Void> validateSortOrder(String sortOrder) {
        if (!sortOrder.equalsIgnoreCase("asc") && !sortOrder.equalsIgnoreCase("desc")) {
            return Mono.error(new BusinessException(BusinessErrorMessage.INVALID_SORT_ORDER));
        }
        return Mono.empty();
    }
}
