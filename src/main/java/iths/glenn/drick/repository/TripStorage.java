package iths.glenn.drick.repository;

import iths.glenn.drick.entity.TripEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripStorage extends JpaRepository<TripEntity, String> {

}
