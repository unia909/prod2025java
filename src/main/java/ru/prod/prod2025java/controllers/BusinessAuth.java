package ru.prod.prod2025java.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.prod.prod2025java.Responses;
import ru.prod.prod2025java.SignInRequest;
import ru.prod.prod2025java.TokenResponse;
import ru.prod.prod2025java.Util;
import ru.prod.prod2025java.entities.DbBusiness;
import ru.prod.prod2025java.validators.Password;

import java.util.List;
import java.util.UUID;

import static ru.prod.prod2025java.Prod2025JavaApplication.app;
import static ru.prod.prod2025java.Util.toJson;

@RestController
@RequestMapping(value = "/api/business/auth", consumes = "application/json", produces = "application/json")
public class BusinessAuth {
    @Data
    public static final class SignUpRequest {
        @Size(min = 5, max = 50)
        private String name;

        @Email
        private String email;

        @Password
        private String password;
    }

    @Data
    @Builder
    @Jacksonized
    private static final class SignUpResponse {
        private String token;
        private UUID id;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@Valid @RequestBody SignUpRequest business) {
        if (app.businesses.existsByEmail(business.getEmail())) {
            return ResponseEntity.status(409).body(Responses.EMAIL_ALREADY_REGISTERED);
        }
        String password = BCrypt.hashpw(business.getPassword(), BCrypt.gensalt());

        DbBusiness dbBusiness = new DbBusiness();
        dbBusiness.setEmail(business.getEmail());
        dbBusiness.setPassword(password);
        dbBusiness.setName(business.getName());
        app.businesses.save(dbBusiness);

        return ResponseEntity.ok(toJson(new SignUpResponse(Util.getBusinessToken(dbBusiness.getId()), dbBusiness.getId())));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<String> signIn(@Valid @RequestBody SignInRequest business) {
        List<DbBusiness> list = app.businesses.getByEmail(business.getEmail());
        if (list.size() != 1) {
            return ResponseEntity.status(401).body(Responses.SIGN_IN_ERROR);
        }
        DbBusiness dbBusiness = list.getFirst();
        String hashedPassword = dbBusiness.getPassword();
        if (!BCrypt.checkpw(business.getPassword(), hashedPassword)) {
            return ResponseEntity.status(401).body(Responses.SIGN_IN_ERROR);
        }

        return ResponseEntity.ok(toJson(new TokenResponse(Util.getBusinessToken(dbBusiness.getId()))));
    }
}
