package ru.se.info.tinder.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import ru.se.info.tinder.model.Location;

@Repository
public interface LocationRepository extends ReactiveCrudRepository<Location, Long> {

}
