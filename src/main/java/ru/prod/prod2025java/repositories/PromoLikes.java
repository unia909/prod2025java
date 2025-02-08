package ru.prod.prod2025java.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.prod.prod2025java.entities.DbPromoLikes;

import java.util.UUID;

public interface PromoLikes extends CrudRepository<DbPromoLikes, UUID> {
    long countByPromoId(UUID promoId);
}
