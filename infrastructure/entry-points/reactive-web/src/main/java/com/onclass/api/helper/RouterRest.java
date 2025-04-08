package com.onclass.api.helper;

import com.onclass.api.helper.request.TechnologyHandlerImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {

    @Bean()
    @AddRouterRestInfo
    public RouterFunction<ServerResponse> routerFunction(TechnologyHandlerImpl handler) {
        return route(GET("/technologiesPaged"), handler::listTechnologies)
                .andRoute(POST("/technologies"), handler::createTechnologies)
                .andRoute(GET("/findById/{id}"), handler::findById);
    }

}
