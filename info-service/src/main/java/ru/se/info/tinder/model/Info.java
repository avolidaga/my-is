package ru.se.info.tinder.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.NotNull;

@Table("info")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Info {

    @Id
    @Column("location_id")
    private Long id;

    @NotNull
    private String name;
    private String description;
    private Double temperature;
}
