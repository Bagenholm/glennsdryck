package iths.glenn.drick.repository;

import iths.glenn.drick.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
    //@Query(value = "SELECT u FROM UserEntity WHERE u.username = ?1")
    UserEntity findByUsername(String username);
}