package ru.prod.prod2025java.controllers;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.prod.prod2025java.*;
import ru.prod.prod2025java.entities.DbPromo;
import ru.prod.prod2025java.validators.UniqueTokens;
import ru.prod.prod2025java.validators.ValidateTarget;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static ru.prod.prod2025java.Prod2025JavaApplication.app;
import static ru.prod.prod2025java.Util.toJson;

@RestController
@RequestMapping(value = "/api/business/promo", produces = "application/json")
public class BusinessPromo {
    @Data
    public static final class CreatePromoRequest {
        @Size(min = 10, max = 100)
        @NotBlank
        private String description;

        @Range(min = 0)
        @JsonAlias("max_count")
        private long maxCount;

        private PromoMode mode;

        @ValidateTarget
        private Target target;

        @Size(min = 5, max = 30)
        @JsonAlias("promo_common")
        private String promoCommon;

        @Size(min = 1, max = 5000)
        @UniqueTokens
        @JsonAlias("promo_unique")
        private List<String> promoUnique;

        @Size(max = 350)
        @URL
        private String imageUrl;

        @JsonAlias("active_from")
        private Date activeFrom;

        @JsonAlias("active_until")
        private Date activeUntil;
    }

    @Data
    @AllArgsConstructor
    private static final class CreatePromoResponse {
        private UUID id;
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<String> createPromo(@RequestHeader("Authorization") String auth,
                                              @Valid @RequestBody CreatePromoRequest promo) {
        UUID businessId = Util.checkBusinessToken(auth);
        if (businessId == null) {
            return ResponseEntity.status(401).body(Responses.NO_AUTH);
        }
        if (!Prod2025JavaApplication.validator.validate(promo).isEmpty()) {
            return ResponseEntity.badRequest().body(Responses.BAD_REQUEST);
        }

        DbPromo dbPromo = new DbPromo();
        dbPromo.setBusinessId(businessId);
        dbPromo.setDescription(promo.getDescription());
        dbPromo.setMaxCount(promo.getMaxCount());
        dbPromo.setMode(promo.getMode().ordinal());
        dbPromo.setTarget(promo.getTarget());
        dbPromo.setPromoCommon(promo.getPromoCommon());
        dbPromo.setPromoUnique(promo.getPromoUnique());
        dbPromo.setImageUrl(promo.getImageUrl());
        dbPromo.setActiveFrom(promo.getActiveFrom());
        dbPromo.setActiveUntil(promo.getActiveUntil());
        app.promos.save(dbPromo);

        return ResponseEntity.status(201).body(toJson(new CreatePromoResponse(dbPromo.getId())));
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getPromo(@RequestHeader("Authorization") String auth,
                                           @PathVariable("id") UUID id) {
        UUID businessId = Util.checkBusinessToken(auth);
        if (businessId == null) {
            return ResponseEntity.status(401).body(Responses.NO_AUTH);
        }

        Optional<DbPromo> promo = app.promos.findById(id);
        if (promo.isEmpty()) {
            return ResponseEntity.status(404).body(Responses.PROMO_NOT_FOUND);
        }
        DbPromo dbPromo = promo.get();

        Promo promo1 = new Promo();
        promo1.setPromoId(dbPromo.getId());
        promo1.setCompanyId(dbPromo.getBusinessId());
        promo1.setCompanyName(app.businesses.getNameById(dbPromo.getBusinessId()));
        promo1.setDescription(dbPromo.getDescription());
        promo1.setPromoCommon(dbPromo.getPromoCommon());
        promo1.setPromoUnique(dbPromo.getPromoUnique());
        promo1.setMode(PromoMode.valueOf(dbPromo.getMode()));
        promo1.setMaxCount(dbPromo.getMaxCount());
        promo1.setLikeCount(app.promoLikes.countByPromoId(dbPromo.getId()));
        promo1.setUsedCount(app.promoActivations.countByPromoId(dbPromo.getId()));
        promo1.setTarget(dbPromo.getTarget());
        promo1.setImageUrl(dbPromo.getImageUrl());
        promo1.setActiveFrom(dbPromo.getActiveFrom());
        promo1.setActiveUntil(dbPromo.getActiveUntil());
        promo1.setActive(dbPromo.getActive());

        return ResponseEntity.ok(toJson(dbPromo));
    }

    @GetMapping
    public ResponseEntity<String> getPromos(@RequestHeader("Authorization") String auth,
                                            @RequestParam(required = false) Integer limit,
                                            @RequestParam(required = false) Integer offset,
                                            @RequestParam(required = false, name = "sort_by") String sortBy,
                                            @RequestParam(required = false) String country)
    {
        UUID businessId = Util.checkBusinessToken(auth);
        if (businessId == null) {
            return ResponseEntity.status(401).body(Responses.NO_AUTH);
        }

        if (sortBy == null) {
            sortBy = "serialId";
        } else switch (sortBy) {
            case "active_from":
                sortBy = "active_from";
                break;
            case "active_until":
                sortBy = "active_until";
                break;
            default:
                return ResponseEntity.status(403).body(Responses.BAD_REQUEST);
        }
        final String orderBy = sortBy;

        Specification<DbPromo> spec = Specification.where((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("businessId"), businessId));
        if (country != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.or(
                            root.get("target").get("country").isNull(),
                            criteriaBuilder.equal(criteriaBuilder.lower(root.get("target").get("country")), country)
                    ));
        }
        spec = spec.and((root, query, criteriaBuilder) -> {
                query.orderBy(criteriaBuilder.desc(root.get(orderBy)));
                return null;
        });

        ChunkRequest chunkRequest = new ChunkRequest(
                offset == null ? 0 : offset.intValue(),
                limit == null ? Integer.MAX_VALUE : limit.intValue()
        );
        List<DbPromo> list = app.promos.findAll(spec, chunkRequest).stream().toList();
        return ResponseEntity.status(200)
                .header("X-Total-Count", Long.toString(app.promos.count(spec)))
                .body(toJson(list));
    }
}





