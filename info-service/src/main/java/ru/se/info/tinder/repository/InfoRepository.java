package ru.se.info.tinder.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import ru.se.info.tinder.model.Info;

public interface InfoRepository extends R2dbcRepository<Info, Long> {

}
