package ru.prod.prod2025java;

import lombok.Data;

@Data
public class SignInRequest {
    private String email;
    private String password;
}
