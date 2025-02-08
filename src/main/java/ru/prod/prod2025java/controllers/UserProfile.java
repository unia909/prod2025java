package ru.prod.prod2025java.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.prod.prod2025java.Responses;
import ru.prod.prod2025java.User;
import ru.prod.prod2025java.UserTarget;
import ru.prod.prod2025java.Util;
import ru.prod.prod2025java.entities.DbUser;

import java.util.UUID;

import static ru.prod.prod2025java.Prod2025JavaApplication.app;
import static ru.prod.prod2025java.Util.toJson;

@RestController
@RequestMapping(value = "/api/user/profile", produces = "application/json")
public class UserProfile {
    @GetMapping
    public ResponseEntity<String> getUserProfile(@RequestHeader("Authorization") String auth) {
        UUID userId = Util.checkUserToken(auth);
        if (userId == null) {
            return ResponseEntity.status(401).body(Responses.NO_AUTH);
        }
        DbUser user = app.users.getById(userId);
        return ResponseEntity.ok(toJson(user));
    }
}
