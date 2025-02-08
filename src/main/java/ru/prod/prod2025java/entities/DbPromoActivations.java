package ru.prod.prod2025java.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "promo_activations")
public class DbPromoActivations {
    @Id
    private UUID promoId;
    private String country;
    private int activations;
}
