package ru.prod.prod2025java;

public enum PromoMode {
    COMMON,
    UNIQUE;

    public static PromoMode valueOf(int value) {
        return switch (value) {
            case 0 -> COMMON;
            case 1 -> UNIQUE;
            default -> null;
        };
    }
}
