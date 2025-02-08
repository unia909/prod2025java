package ru.prod.prod2025java;

import jakarta.annotation.PostConstruct;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.JedisPooled;
import ru.prod.prod2025java.repositories.*;

@RestController
@RequestMapping("/api")
@SpringBootApplication
public class Prod2025JavaApplication {
    static JedisPooled redis;
    public static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    public static Prod2025JavaApplication app;

    @Autowired
    public Users users;
    @Autowired
    public Businesses businesses;
    @Autowired
    public Promos promos;
    @Autowired
    public PromoActivations promoActivations;
    @Autowired
    public PromoLikes promoLikes;

    @GetMapping(value="/ping")
    public String ping() {
        return "";
    }

    public static void main(String[] args) {
        String redisHost = System.getenv("REDIS_HOST");
        int redisPort;
        if (redisHost == null) {
            redisHost = "localhost";
            redisPort = 6379;
        } else {
            redisPort = Integer.parseInt(System.getenv("REDIS_PORT"));
        }
        redis = new JedisPooled(redisHost, redisPort);

        String postgresUrl = System.getenv("POSTGRES_JDBC_URL");
        if (postgresUrl == null) {
            postgresUrl = "jdbc:postgresql://localhost:5432/db";
        }
        System.setProperty("spring.datasource.url", postgresUrl);

        SpringApplication.run(Prod2025JavaApplication.class, args);
    }

    @PostConstruct
    public void onStartup() {
        app = this;
        // If not prod
        if (System.getProperty("os.name").startsWith("Windows")) {
            users.deleteAll();
            businesses.deleteAll();
            promos.deleteAll();
            promoActivations.deleteAll();
        }
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .csrf(csrf -> csrf.disable())
                .build();
    }
}
