package ru.prod.prod2025java.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.prod.prod2025java.entities.DbUser;

import java.util.List;
import java.util.UUID;

@Repository
public interface Users extends CrudRepository<DbUser, Integer> {
    List<DbUser> getByEmail(String email);

    boolean existsByEmail(String email);

    DbUser getById(UUID id);
}
