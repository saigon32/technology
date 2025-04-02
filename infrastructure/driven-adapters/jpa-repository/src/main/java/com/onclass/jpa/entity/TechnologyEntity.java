package com.onclass.jpa.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("technology")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TechnologyEntity {
    @Id
    private int id;
    private String name;
    private String description;
}
