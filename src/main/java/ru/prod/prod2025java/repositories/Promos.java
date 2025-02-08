package ru.prod.prod2025java.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.prod.prod2025java.entities.DbPromo;

import java.util.UUID;

@Repository
public interface Promos extends JpaRepository<DbPromo, UUID>, JpaSpecificationExecutor<DbPromo> {
}
