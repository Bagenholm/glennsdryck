package iths.glenn.drick.repository;

import iths.glenn.drick.entity.TripEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface TripStorage extends JpaRepository<TripEntity, String> {
    Set<TripEntity> findAllByCityEquals(String title);
    Set<TripEntity> findAllByCityContains(String cityName);
}
