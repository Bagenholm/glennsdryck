package iths.glenn.drick.repository;

import iths.glenn.drick.entity.DrinkEntity;
import iths.glenn.drick.entity.UserEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserStorage extends JpaRepository<UserEntity, String> {

    /*@Query(value = "SELECT * FROM drinks WHERE productname IN (SELECT productname FROM drinks GROUP BY productname HAVING count(*) > 1)", nativeQuery = true))
    List<DrinkEntity> findAllSharedDrinks(); */
    @Query("SELECT u FROM UserEntity u WHERE u.username = ?1")
    List<UserEntity> findByUsername(String username);

    @Query(value = "Select u FROM UserEntity u")
    List<UserEntity> findAllDrinks(Sort sort);

}
