package iths.glenn.drick.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import iths.glenn.drick.entity.DrinkEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Repository
public interface DrinkStorage extends JpaRepository<DrinkEntity, String>, Serializable {

    /*@Query(value = "SELECT * FROM drinks WHERE productname IN (SELECT productname FROM drinks GROUP BY productname HAVING count(*) > 1)", nativeQuery = true))
    List<DrinkEntity> findAllSharedDrinks(); */

    @Query(value = "SELECT d FROM DrinkEntity d WHERE d.drinkKey LIKE ?1% ORDER BY alcohol_per_price DESC ")
    List<DrinkEntity> findBestApkFromStore(String store);

    @Query(value = "SELECT d FROM DrinkEntity d WHERE d.volume = ?1")
    List<DrinkEntity> findByVolume(float volume);

    @Query(value = "SELECT d FROM DrinkEntity d")
    List<DrinkEntity> findAllDrinks(Sort sort);

    @Query(value = "SELECT d FROM DrinkEntity d WHERE (INSTR(d.name, ?1) > 0 OR INSTR(?1, d.name) > 0 AND d.drinkKey NOT LIKE 'stena%')")
    List<DrinkEntity> findByPartialNameNotStena(String name);

    @Query(value = "SELECT d FROM DrinkEntity d WHERE d.drinkKey LIKE ?1%")
    List<DrinkEntity> findByStore(String storeName);

    List<DrinkEntity> findAllByTypeEquals(String type, Pageable alcoholPerPrice);

    List<DrinkEntity> findAllByStoreNameEquals(String store, Pageable alcoholPerPrice);

    List<DrinkEntity> findAllByStoreNameEqualsAndTypeEquals(String store, String type, Pageable alcoholPerPrice);

    List<DrinkEntity> findAllByNameContaining(String name);
}
