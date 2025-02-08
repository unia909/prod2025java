package ru.prod.prod2025java;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@Jacksonized
@NoArgsConstructor
@AllArgsConstructor
public class Promo {
    @JsonAlias("promo_id")
    private UUID promoId;
    @JsonAlias("company_id")
    private UUID companyId;
    @JsonAlias("companyName")
    private String companyName;
    private String description;
    @JsonAlias("promo_common")
    private String promoCommon;
    @JsonAlias("promo_unique")
    private List<String> promoUnique;
    private PromoMode mode;
    @JsonAlias("max_count")
    private long maxCount;
    @JsonAlias("like_count")
    private long likeCount;
    @JsonAlias("used_count")
    private long usedCount;
    @NotNull
    private Target target;
    @JsonAlias("image_url")
    private String imageUrl;
    @JsonAlias("active_from")
    private Date activeFrom;
    @JsonAlias("active_until")
    private Date activeUntil;
    private boolean active;
}
