package ru.se.info.tinder.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@Table(name = "images")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class ImageEntity {
    private String id;
    private String bucket;
}
