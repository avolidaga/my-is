package ru.se.ifmo.tinder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.se.ifmo.tinder.model.Location;

public interface LocationRepository extends JpaRepository<Location, Integer> {

}
