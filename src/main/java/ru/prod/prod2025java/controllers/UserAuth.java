package ru.prod.prod2025java.controllers;

import jakarta.validation.Valid;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.prod.prod2025java.Responses;
import ru.prod.prod2025java.TokenResponse;
import ru.prod.prod2025java.User;
import ru.prod.prod2025java.Util;
import ru.prod.prod2025java.entities.DbUser;
import ru.prod.prod2025java.validators.Password;

import java.util.List;

import static ru.prod.prod2025java.Prod2025JavaApplication.app;
import static ru.prod.prod2025java.Util.toJson;

@RestController
@RequestMapping(value = "/api/user/auth", consumes = "application/json", produces = "application/json")
public class UserAuth {
    @Data
    @EqualsAndHashCode(callSuper=true)
    public static final class SignUpRequest extends User {
        @Password
        private String password;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@Valid @RequestBody SignUpRequest user) {
        if (app.users.existsByEmail(user.getEmail())) {
            return ResponseEntity.status(409).body(Responses.EMAIL_ALREADY_REGISTERED);
        }

        String password = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());

        DbUser dbUser = new DbUser();
        dbUser.setEmail(user.getEmail());
        dbUser.setPassword(password);
        dbUser.setName(user.getName());
        dbUser.setSurname(user.getSurname());
        dbUser.setAvatarUrl(user.getAvatarUrl());
        dbUser.setOther(user.getOther());
        app.users.save(dbUser);

        return ResponseEntity.ok(toJson(new TokenResponse(Util.getUserToken(dbUser.getId()))));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<String> signIn(@Valid @RequestBody SignUpRequest user) {
        List<DbUser> list = app.users.getByEmail(user.getEmail());
        if (list.size() != 1) {
            return ResponseEntity.status(401).body(Responses.SIGN_IN_ERROR);
        }
        DbUser dbUser = list.getFirst();
        String hashedPassword = dbUser.getPassword();
        if (!BCrypt.checkpw(user.getPassword(), hashedPassword)) {
            return ResponseEntity.status(401).body(Responses.SIGN_IN_ERROR);
        }
        return ResponseEntity.ok(toJson(new TokenResponse(Util.getUserToken(dbUser.getId()))));
    }
}
