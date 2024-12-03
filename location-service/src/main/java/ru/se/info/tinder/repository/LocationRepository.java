package ru.se.info.tinder.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import ru.se.info.tinder.model.Location;

public interface LocationRepository extends R2dbcRepository<Location, Long> {

}
