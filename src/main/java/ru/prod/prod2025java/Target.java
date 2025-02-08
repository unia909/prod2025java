package ru.prod.prod2025java;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.persistence.Embeddable;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import ru.prod.prod2025java.validators.Categories;
import ru.prod.prod2025java.validators.CountryName;

import java.util.List;

@Data
@Embeddable
public class Target {
    @Range(max = 100)
    @JsonAlias("age_from")
    private Integer ageFrom;

    @Range(max = 100)
    @JsonAlias("age_until")
    private Integer ageUntil;

    @CountryName
    private String country;

    @Categories
    private List<String> categories;
}
