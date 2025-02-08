package ru.prod.prod2025java;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import redis.clients.jedis.params.SetParams;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.UUID;

import static ru.prod.prod2025java.Prod2025JavaApplication.redis;

public class Util {
    private Util() {}

    private static final long AUTH_TOKEN_EXPIRE_TIME = 3600;
    //private static final Algorithm JWT_ALGORITHM = Algorithm.HMAC512(System.getenv("RANDOM_SECRET"));
    private static final Algorithm JWT_ALGORITHM = Algorithm.HMAC512("vG2ucq18F9Ru37QxvB480Y6o7lK4cAD51T70G4MZ6zvFRhW8McHLuKzzDe0v53oJhq3mV2u2pjL6rxXXwEQHV0X1Zf96VgqKAg1Hm1FZt8blR00mTYmTonGlyP8zACxR");

    public static String toJson(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String getToken(String type, UUID uuid) {
        type += uuid.toString();
        String token = JWT.create()
                .withExpiresAt(Instant.now().plusSeconds(AUTH_TOKEN_EXPIRE_TIME))
                .withClaim("uuid", uuid.toString())
                // Generate unique token even if sign-in was in the same second
                .withClaim("dummy", new SecureRandom().nextLong())
                .sign(JWT_ALGORITHM);
        redis.set(type, token, SetParams.setParams().ex(AUTH_TOKEN_EXPIRE_TIME));
        return token;
    }

    public static String getUserToken(UUID uuid) {
        return getToken("u", uuid);
    }

    public static String getBusinessToken(UUID uuid) {
        return getToken("b", uuid);
    }

    private static UUID checkToken(String type, String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        DecodedJWT jwt;
        try {
            jwt = JWT.decode(token);
        } catch (Throwable t) {
            return null;
        }
        // If expired
        if (jwt.getExpiresAtAsInstant().compareTo(Instant.now()) < 0) {
            return null;
        }
        String uuid = jwt.getClaim("uuid").asString();
        if (uuid == null) {
            return null;
        }
        // If token was reset (new sign in was since generating this token)
        if (!redis.get(type + uuid).equals(token)) {
            return null;
        }
        return UUID.fromString(uuid);
    }

    public static UUID checkUserToken(String token) {
        return checkToken("u", token);
    }

    public static UUID checkBusinessToken(String token) {
        return checkToken("b", token);
    }
}
