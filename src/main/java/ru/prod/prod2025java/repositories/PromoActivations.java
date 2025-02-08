package ru.prod.prod2025java.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.prod.prod2025java.entities.DbPromoActivations;

import java.util.UUID;

@Repository
public interface PromoActivations extends CrudRepository<DbPromoActivations, Integer> {
    long countByPromoId(UUID promoId);
}
