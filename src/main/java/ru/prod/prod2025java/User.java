package ru.prod.prod2025java;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
public class User {
    @Size(min = 1, max = 100)
    private String name;

    @Size(min = 1, max = 120)
    private String surname;

    @Size(min = 8, max = 120)
    @Email
    private String email;

    @Size(max = 350)
    @URL
    @Nullable
    private String avatarUrl;

    private UserTarget other;
}
