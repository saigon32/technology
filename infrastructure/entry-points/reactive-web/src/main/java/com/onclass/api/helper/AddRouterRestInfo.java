package com.onclass.api.helper;

import com.onclass.api.helper.request.dto.TechnologyRequestDto;
import com.onclass.api.helper.request.dto.TechnologyResponseDto;
import com.onclass.model.technology.Technology;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;

import org.springframework.http.MediaType;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@RouterOperations({
        @RouterOperation(
                method = RequestMethod.POST,
                operation =
                @Operation(
                        description = "Add technologies",
                        operationId = "addTechnology",
                        tags = "technology",
                        requestBody =
                        @RequestBody(
                                description = "Technology to add",
                                required = true,
                                content = @Content(schema = @Schema(implementation = TechnologyRequestDto.class,
                                        requiredProperties = {"name", "desription"}))),
                        responses = {
                                @ApiResponse(
                                        responseCode = "200",
                                        description = "Add Technology response",
                                        content = {
                                                @Content(
                                                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                        schema = @Schema(implementation = Technology.class))
                                        }),
                                @ApiResponse(
                                        responseCode = "400",
                                        description = "Bad Request response",
                                        content = {
                                                @Content(
                                                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                        schema = @Schema(implementation = ErrorResponse.class))
                                        })
                        })),
        @RouterOperation(
                method = RequestMethod.GET,
                path = "/technologiesPaged",
                operation =
                @Operation(
                        description = "Get all technologies",
                        operationId = "getAllTechnologies",
                        tags = "technology",
                        parameters = {
                                @Parameter(
                                        name = "page",
                                        description = "Page number for pagination",
                                        required = false,
                                        example = "0",
                                        schema = @Schema(type = "integer", defaultValue = "0")
                                ),
                                @Parameter(
                                        name = "size",
                                        description = "Number of records per page",
                                        required = false,
                                        example = "10",
                                        schema = @Schema(type = "integer", defaultValue = "10")
                                ),
                                @Parameter(
                                        name = "sortOrder",
                                        description = "Sort order, either 'asc' or 'desc'",
                                        required = false,
                                        example = "asc",
                                        schema = @Schema(type = "string", defaultValue = "asc")
                                )
                        },
                        responses = {
                                @ApiResponse(
                                        responseCode = "200",
                                        description = "Get all players endpoint",
                                        content = {
                                                @Content(
                                                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                        array = @ArraySchema(schema = @Schema(implementation = TechnologyResponseDto.class)))
                                        })
                        }))
})
public @interface AddRouterRestInfo {
}
