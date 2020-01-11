package iths.glenn.drick.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import iths.glenn.drick.entity.DrinkEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Repository
public interface DrinkStorage extends JpaRepository<DrinkEntity, String> {

    /*@Query(value = "SELECT * FROM drinks WHERE productname IN (SELECT productname FROM drinks GROUP BY productname HAVING count(*) > 1)", nativeQuery = true))
    List<DrinkEntity> findAllSharedDrinks(); */
    @Query("SELECT d FROM DrinkEntity d WHERE d.volume = ?1")
    List<DrinkEntity> findByVolume(float volume);

    @Query(value = "Select d FROM DrinkEntity d")
    List<DrinkEntity> findAllDrinks(Sort sort);

}
