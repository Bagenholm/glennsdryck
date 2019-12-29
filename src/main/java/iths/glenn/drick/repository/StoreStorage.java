package iths.glenn.drick.repository;

import iths.glenn.drick.entity.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreStorage extends JpaRepository<StoreEntity, String> {

}
