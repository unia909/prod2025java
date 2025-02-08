package ru.prod.prod2025java.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "promo_likes")
public class DbPromoLikes {
    @Id
    private UUID promoId;
    private UUID userId;
}
