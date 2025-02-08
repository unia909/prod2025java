package ru.prod.prod2025java.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.prod.prod2025java.entities.DbBusiness;

import java.util.List;
import java.util.UUID;

@Repository
public interface Businesses extends CrudRepository<DbBusiness, Integer> {
    boolean existsByEmail(String email);

    List<DbBusiness> getByEmail(String email);

    @Query(value = "SELECT b.name FROM businesses b WHERE b.id = :id", nativeQuery = true)
    String getNameById(@Param("id") UUID id);
}
