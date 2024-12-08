package ru.se.info.tinder.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("fabric_texture")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FabricTexture {
    @Id
    @Column("fabric_texture_id")
    private Long id;

    @Column("fabric_texture_name")
    private String name;

    @Column("fabric_texture_description")
    private String description;
}
