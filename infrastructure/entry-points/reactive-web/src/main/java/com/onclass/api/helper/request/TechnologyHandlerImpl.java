package com.onclass.api.helper.request;

import com.onclass.api.helper.ITechnologyHandler;
import com.onclass.api.helper.mappers.ITechnologyRequestMapper;
import com.onclass.api.helper.mappers.ITechnologyResponseMapper;
import com.onclass.api.helper.request.dto.TechnologyRequestDto;
import com.onclass.jpa.config.DBErrorMessage;
import com.onclass.jpa.config.DBException;
import com.onclass.model.technology.gateways.ITechnologyServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.onclass.api.helper.exception.Constants.*;

@Component
@RequiredArgsConstructor
public class TechnologyHandlerImpl implements ITechnologyHandler {

    private final ITechnologyServicePort technologyService;
    private final ITechnologyRequestMapper technologyRequestMapper;
    private final ITechnologyResponseMapper technologyResponseMapper;

    @Override
    public Mono<ServerResponse> findById(ServerRequest request) {
        return technologyService.findById(request.pathVariable("id"))
                .flatMap(technologyResponseDtos ->
                        ServerResponse.ok().bodyValue(technologyResponseDtos))
                .switchIfEmpty(Mono.error(new DBException(DBErrorMessage.TECHNOLOGY_NOT_FOUND)));
    }

    @Override
    public Mono<ServerResponse> createTechnologies(ServerRequest request) {
        return request.bodyToFlux(TechnologyRequestDto.class)
                .map(technologyRequestMapper::toDomain)
                .transform(technologyService::createTechnologies)
                .map(technologyResponseMapper::toDto)
                .collectList()
                .flatMap(technologyResponseDtos ->
                        ServerResponse.ok().bodyValue(technologyResponseDtos));
    }

    @Override
    public Mono<ServerResponse> listTechnologies(ServerRequest request) {
        int page = Integer.parseInt(request.queryParam(PAGE).orElse(P));
        int size = Integer.parseInt(request.queryParam(SIZE).orElse(S));
        String sortOrder = request.queryParam(SORT_ORDER).orElse(S_O);

        return technologyService.listTechnologiesPaged(page, size, sortOrder)
                .collectList()
                .flatMap(technologies -> ServerResponse.ok().bodyValue(technologies));
    }

}
