package iths.glenn.drick.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import iths.glenn.drick.entity.DrinkEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DrinkStorage extends JpaRepository<DrinkEntity, String> { }
