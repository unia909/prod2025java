package ru.prod.prod2025java.controllers;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.prod.prod2025java.Responses;
import ru.prod.prod2025java.Util;
import ru.prod.prod2025java.entities.DbPromo;
import ru.prod.prod2025java.entities.DbUser;

import java.util.List;
import java.util.UUID;

import static ru.prod.prod2025java.Prod2025JavaApplication.app;
import static ru.prod.prod2025java.Util.toJson;

@RestController
@RequestMapping(value = "/api/user/feed", produces = "application/json")
public class UserFeed {
    @GetMapping
    public ResponseEntity<String> getUserFeed(@RequestHeader("Authorization") String auth,
                                              @RequestParam(value = "offset", required = false) Integer offset,
                                              @RequestParam(value = "limit", required = false) Integer limit,
                                              @RequestParam(value = "category", required = false) String category,
                                              @RequestParam(value = "active", required = false) Boolean active) {
        UUID userId = Util.checkUserToken(auth);
        if (userId == null) {
            return ResponseEntity.status(401).body(Responses.NO_AUTH);
        }

        DbUser user = app.users.getById(userId);
        int userAge = user.getOther().getAge();
        String userCountry = user.getOther().getCountry();

        Specification<DbPromo> spec = Specification.where((root, query, criteriaBuilder) ->
            criteriaBuilder.and(
                    criteriaBuilder.or(
                            root.get("target").get("ageFrom").isNull(),
                            criteriaBuilder.lessThanOrEqualTo(root.get("target").get("ageFrom"), userAge)
                    ),
                    criteriaBuilder.or(
                            root.get("target").get("ageUntil").isNull(),
                            criteriaBuilder.greaterThanOrEqualTo(root.get("target").get("ageUntil"), userAge)
                    ),
                    criteriaBuilder.or(
                            root.get("target").get("country").isNull(),
                            criteriaBuilder.equal(root.get("target").get("country"), userCountry)
                    )
            )
        );
        if (category != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(criteriaBuilder.lower(root.get("category")), category));
        }

        List<DbPromo> promos = app.promos.findAll(spec).stream().toList();
        int totalCount = promos.size();
        if (active != null) {
            promos = promos.stream().filter(promo -> promo.getActive() == active).toList();
        }
        if (offset != null) {
            promos = promos.subList(offset, promos.size());
        }
        if (limit != null && limit < promos.size()) {
            promos = promos.subList(0, limit);
        }
        return ResponseEntity.status(200)
                .header("X-Total-Count", Integer.toString(totalCount))
                .body(toJson(promos));
    }
}
