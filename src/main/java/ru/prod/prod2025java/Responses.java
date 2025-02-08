package ru.prod.prod2025java;

public class Responses {
    private Responses() {}

    public static final String OK = "{\"status\": \"ok\"}";
    public static final String SIGN_IN_ERROR = "{\"status\": \"error\", \"message\": \"Неверный email или пароль\"}";
    public static final String EMAIL_ALREADY_REGISTERED = "{\"status\": \"error\", \"message\": \"Такой email уже зарегистрирован.\"}";
    public static final String BAD_REQUEST = "{\"status\": \"error\", \"message\": \"Ошибка в данных запроса.\"}";
    public static final String NO_AUTH = "{\"status\": \"error\", \"message\": \"Пользователь не авторизован.\"}";
    public static final String PROMO_DENIED = "{\"status\": \"error\", \"message\": \"Промокод не принадлежит этой компании.\"}";
    public static final String COMMENT_DENIED = "{\"status\": \"error\", \"message\": \"Комментарий не принадлежит пользователю.\"}";
    public static final String PROMO_NOT_FOUND = "{\"status\": \"error\", \"message\": \"Промокод не найден.\"}";
    public static final String PROMO_FORBIDDEN = "{\"status\": \"error\", \"message\": \"Вы не можете использовать этот промокод.\"}";
}
