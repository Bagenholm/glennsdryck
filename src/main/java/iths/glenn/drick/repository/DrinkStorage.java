package iths.glenn.drick.repository;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DrinkStorage {
    List findAll();
}
