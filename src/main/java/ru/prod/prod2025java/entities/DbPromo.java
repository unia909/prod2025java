package ru.prod.prod2025java.entities;

import jakarta.persistence.*;
import lombok.Data;
import ru.prod.prod2025java.PromoMode;
import ru.prod.prod2025java.Target;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "promos")
public class DbPromo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @SequenceGenerator(name = "promosIdSeq", sequenceName = "promos_id_seq", allocationSize = 1)
    private long serialId;

    private UUID businessId;
    private String description;
    private String imageUrl;
    private int mode;
    private long maxCount;
    private Date activeFrom;
    private Date activeUntil;
    private long activations;
    @Embedded
    private Target target;
    private String promoCommon;
    @ElementCollection
    private List<String> promoUnique;

    public boolean getActive() {
        if (activeFrom != null && activeFrom.after(Date.from(Instant.now()))) {
            return false;
        }
        if (activeUntil != null && activeUntil.before(Date.from(Instant.now()))) {
            return false;
        }
        if (mode == PromoMode.COMMON.ordinal()) {
            return activations < maxCount;
        } else {
            return activations < promoUnique.size();
        }
    }
}
