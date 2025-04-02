package com.onclass.api.helper.request.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TechnologyResponseDto {
    private int id;
    private String name;
    private String description;
}
