package iths.glenn.drick.repository;

import iths.glenn.drick.entity.TripEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripStorage extends JpaRepository<TripEntity, String> {
    List<TripEntity> findAllByCityEquals(String title);
}
